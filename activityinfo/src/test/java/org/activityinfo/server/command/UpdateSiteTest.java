package org.activityinfo.server.command;

import org.activityinfo.shared.command.GetSites;
import org.activityinfo.shared.command.UpdateEntity;
import org.activityinfo.shared.command.result.ListResult;
import org.activityinfo.shared.dto.SiteModel;
import org.activityinfo.shared.exception.CommandException;
import org.activityinfo.server.domain.Site;
import org.junit.Assert;
import org.junit.Test;

import javax.persistence.EntityManager;
import java.util.Map;
import java.util.HashMap;


public class UpdateSiteTest extends CommandTestCase {


	@Test
	public void testUpdate() throws CommandException {
		
		// reset the state of the database
		populate("sites-simple1");
		
		// retrieve from the server
		setUser(1);
		ListResult<SiteModel> result = execute(GetSites.byId(1));
	
		SiteModel model = result.getData().get(0);
			
		// modify and generate command
		model.setComments("NEW <b>Commentaire</b>");
		model.setLocationName("NEWNAME");
		model.setAttributeValue(1, true);
        model.setAttributeValue(2, null);
		model.setAttributeValue(3, true);
        model.setAttributeValue(4, false);
        model.setIndicatorValue(2, 995.0);


		execute(new UpdateEntity(model, model.getProperties()));
				
		// retrieve the old one

		result = execute(GetSites.byId(1));
		SiteModel secondRead = result.getData().get(0);
		
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
        // reset the state of the database
		populate("sites-simple1");

		// retrieve from the server
		setUser(1);

        // define changes for site id=2
        Map<String,Object> changes = new HashMap<String, Object>();
        changes.put("partnerId", 2);

        execute(new UpdateEntity("site", 2, changes));

        // assure that the change has been effected
        EntityManager em = emf.createEntityManager();
        Site site = em.find(Site.class, 2);
        Assert.assertEquals("partnerId", 2, site.getPartner().getId());
    }
	
}
