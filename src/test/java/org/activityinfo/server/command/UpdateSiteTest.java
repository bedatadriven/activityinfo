package org.activityinfo.server.command;

/*
 * #%L
 * ActivityInfo Server
 * %%
 * Copyright (C) 2009 - 2013 UNICEF
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.util.HashMap;
import java.util.Map;

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
        assertThat((String) cmd.getChanges().get("comments"),
            equalTo(modified.getComments()));

        execute(cmd);

        // retrieve the old one

        result = execute(GetSites.byId(1));
        SiteDTO secondRead = result.getData().get(0);

        // confirm that the changes are there
        Assert.assertEquals("site.comments", modified.getComments(),
            secondRead.getComments());
        Assert.assertEquals("site.reportingPeriod[0].indicatorValue[0]", 995,
            secondRead.getIndicatorValue(2).intValue());

        Assert.assertEquals("site.attribute[1]", true,
            modified.getAttributeValue(1));
        Assert.assertEquals("site.attribute[3]", true,
            modified.getAttributeValue(3));
        Assert.assertEquals("site.attribute[4]", false,
            modified.getAttributeValue(4));
    }

    @Test
    public void charsets() throws CommandException {
        // retrieve from the server
        ListResult<SiteDTO> result = execute(GetSites.byId(1));

        SiteDTO original = result.getData().get(0);
        SiteDTO modified = original.copy();

        assertThat(modified.getId(), equalTo(original.getId()));

        // modify and generate command
        // note that the character sequence below is two characters:
        // the first a simple unicode character and the second a code point
        // requiring 4-bytes.
        // http://www.charbase.com/20731-unicode-cjk-unified-ideograph

        // NOTE: for the moment, i'm rolling back utf8mb4 support becuase it
        // requires
        // Mysql-5.5 which is **PITA*** to get running on earlier versions of
        // ubuntu.
        // To be reapplied when suppport
        // modified.setComments("≥\ud841\udf31");

        modified.setComments("≥");

        System.out.println(modified.getComments());

        assertThat(
            modified.getComments().codePointCount(0,
                modified.getComments().length()), equalTo(1));

        UpdateSite cmd = new UpdateSite(original, modified);
        assertThat((String) cmd.getChanges().get("comments"),
            equalTo(modified.getComments()));

        execute(cmd);

        // retrieve the old one

        result = execute(GetSites.byId(1));
        SiteDTO secondRead = result.getData().get(0);

        // confirm that the changes are there
        assertThat(secondRead.getComments(), equalTo(modified.getComments()));
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
