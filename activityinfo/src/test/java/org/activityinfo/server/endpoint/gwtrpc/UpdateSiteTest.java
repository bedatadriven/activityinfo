package org.activityinfo.server.endpoint.gwtrpc;

import org.activityinfo.server.dao.OnDataSet;
import org.activityinfo.server.domain.Site;
import org.activityinfo.shared.command.GetSites;
import org.activityinfo.shared.command.UpdateEntity;
import org.activityinfo.shared.command.result.ListResult;
import org.activityinfo.shared.dto.SiteDTO;
import org.activityinfo.shared.exception.CommandException;
import org.activityinfo.test.InjectionSupport;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;
import java.util.Map;


@RunWith(InjectionSupport.class)
@OnDataSet("/dbunit/sites-simple1.db.xml")
public class UpdateSiteTest extends CommandTestCase {


    @Test
    public void testUpdate() throws CommandException {
        // retrieve from the server
        ListResult<SiteDTO> result = execute(GetSites.byId(1));

        SiteDTO model = result.getData().get(0);

        // modify and generate command
        model.setComments("NEW <b>Commentaire</b>");
        model.setLocationName("NEWNAME");
        model.setAttributeValue(1, true);
        model.setAttributeValue(2, null);
        model.setAttributeValue(3, true);
        model.setAttributeValue(4, false);
        model.setIndicatorValue(2, 995.0);
        model.setAdminEntity(2, null);


        execute(new UpdateEntity(model, model.getProperties()));

        // retrieve the old one

        result = execute(GetSites.byId(1));
        SiteDTO secondRead = result.getData().get(0);

        // confirm that the changes are there
        Assert.assertEquals("site.comments", model.getComments(), secondRead.getComments());
        Assert.assertEquals("site.location.name", model.getLocationName(), secondRead.getLocationName());
        Assert.assertEquals("site.reportingPeriod[0].indicatorValue[0]", 995,
                secondRead.getIndicatorValue(2).intValue());

        Assert.assertEquals("site.attribute[1]", true, model.getAttributeValue(1));
        Assert.assertNull("site.attribute[2]", model.getAttributeValue(2));
        Assert.assertEquals("site.attribute[3]", true, model.getAttributeValue(1));
        Assert.assertEquals("site.attribute[4]", true, model.getAttributeValue(1));
    }

    public void testUpdatePartner() throws CommandException {
        // define changes for site id=2
        Map<String, Object> changes = new HashMap<String, Object>();
        changes.put("partnerId", 2);

        execute(new UpdateEntity("site", 2, changes));

        // assure that the change has been effected

        Site site = em.find(Site.class, 2);
        Assert.assertEquals("partnerId", 2, site.getPartner().getId());
    }

}
