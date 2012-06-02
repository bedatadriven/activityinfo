/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.server.command;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.util.HashMap;
import java.util.Map;

import org.hamcrest.CoreMatchers.*;

import org.activityinfo.server.database.OnDataSet;
import org.activityinfo.server.database.hibernate.entity.LockedPeriod;
import org.activityinfo.server.database.hibernate.entity.Site;
import org.activityinfo.shared.command.GetSites;
import org.activityinfo.shared.command.UpdateEntity;
import org.activityinfo.shared.command.UpdateSite;
import org.activityinfo.shared.command.result.ListResult;
import org.activityinfo.shared.command.result.SiteResult;
import org.activityinfo.shared.dto.SiteDTO;
import org.activityinfo.shared.exception.CommandException;
import org.activityinfo.test.InjectionSupport;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.google.common.collect.Maps;


@RunWith(InjectionSupport.class)
@OnDataSet("/dbunit/sites-simple1.db.xml")
public class UpdateSiteTest extends CommandTestCase { 

    @Test
    public void testUpdate() throws CommandException {
        // retrieve from the server
        ListResult<SiteDTO> result = execute(GetSites.byId(1));

        SiteDTO original = result.getData().get(0);
        SiteDTO modified = original.copy();
        
        assertThat(modified.getId(), equalTo(original.getId()));
        
        // modify and generate command
        modified.setComments("NEW <b>Commentaire</b>");
        modified.setAttributeValue(1, true);
        modified.setAttributeValue(2, null);
        modified.setAttributeValue(3, true);
        modified.setAttributeValue(4, false);
        modified.setIndicatorValue(2, 995.0);
        modified.setAdminEntity(2, null);

        UpdateSite cmd = new UpdateSite(original, modified);
        assertThat((String)cmd.getChanges().get("comments"), equalTo(modified.getComments()));
        
		execute(cmd);

        // retrieve the old one

        result = execute(GetSites.byId(1));
        SiteDTO secondRead = result.getData().get(0);

        // confirm that the changes are there
        Assert.assertEquals("site.comments", modified.getComments(), secondRead.getComments());
        Assert.assertEquals("site.reportingPeriod[0].indicatorValue[0]", 995,
                secondRead.getIndicatorValue(2).intValue());

        Assert.assertEquals("site.attribute[1]", true, modified.getAttributeValue(1));
        Assert.assertEquals("site.attribute[3]", true, modified.getAttributeValue(3));
        Assert.assertEquals("site.attribute[4]", false, modified.getAttributeValue(4));
    }

    @Test
    public void testUpdatePreservesAdminMemberships() throws CommandException {
        
        Map<String, Object> changes = Maps.newHashMap();
        changes.put("comments", "new comments");
        
        execute(new UpdateSite(1, changes));

        // retrieve the old one
	
        SiteResult result = execute(GetSites.byId(1));
        SiteDTO secondRead = result.getData().get(0);

        assertThat(secondRead.getAdminEntity(1).getId(), equalTo(2));
        assertThat(secondRead.getAdminEntity(2).getId(), equalTo(12));
    }
    
    @Test
    public void testUpdatePartner() throws CommandException {
        // define changes for site id=2
        Map<String, Object> changes = new HashMap<String, Object>();
        changes.put("partnerId", 2);

        execute(new UpdateSite(2, changes));

        // assure that the change has been effected

        Site site = em.find(Site.class, 2);
        Assert.assertEquals("partnerId", 2, site.getPartner().getId());
    }
    
    @Test
    public void testUpdateLockedPeriod() throws CommandException {
        Map<String, Object> changes = new HashMap<String, Object>();
        changes.put("enabled", false);

        execute(new UpdateEntity("LockedPeriod", 1, changes));

        // assure that the change has been effected
        LockedPeriod lockedPeriod = em.find(LockedPeriod.class, 1);
        Assert.assertEquals("enabled", false, lockedPeriod.isEnabled());
    }
}
