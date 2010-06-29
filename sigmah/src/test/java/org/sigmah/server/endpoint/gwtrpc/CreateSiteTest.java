package org.sigmah.server.endpoint.gwtrpc;

import com.extjs.gxt.ui.client.data.PagingLoadResult;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.sigmah.server.dao.OnDataSet;
import org.sigmah.shared.command.CreateEntity;
import org.sigmah.shared.command.GetSites;
import org.sigmah.shared.command.result.CreateResult;
import org.sigmah.shared.dto.AdminEntityDTO;
import org.sigmah.shared.dto.PartnerDTO;
import org.sigmah.shared.dto.SiteDTO;
import org.sigmah.shared.exception.CommandException;
import org.sigmah.test.InjectionSupport;

import java.util.GregorianCalendar;

@RunWith(InjectionSupport.class)
@OnDataSet("/dbunit/sites-simple1.db.xml")
public class CreateSiteTest extends CommandTestCase {


    @Test
    public void test() throws CommandException {
        // create a new detached, client model
        SiteDTO newSite = new SiteDTO();

        newSite.setActivityId(1);
        newSite.setStatus(-1);
        newSite.setPartner(new PartnerDTO(1, "Foobar"));
        newSite.setDate1((new GregorianCalendar(2008, 12, 1)).getTime());
        newSite.setDate2((new GregorianCalendar(2009, 1, 3)).getTime());
        newSite.setLocationName("Virunga");
        newSite.setLocationAxe("Goma - Rutshuru");
        newSite.setX(27.432);
        newSite.setY(1.23);
        newSite.setIndicatorValue(1, 996.0);
        newSite.setIndicatorValue(2, null);
        newSite.setAttributeValue(1, true);
        newSite.setAttributeValue(2, false);
        newSite.setComments("huba huba");

        // create command

        CreateEntity cmd = CreateEntity.Site(newSite);

        // execute the command

        setUser(1);

        CreateResult result = (CreateResult) execute(cmd);


        // let the client know the command has succeeded
        newSite.setId(result.getNewId());
        //cmd.onCompleted(result);


        // try to retrieve what we've created

        PagingLoadResult<SiteDTO> loadResult = execute(GetSites.byId(newSite.getId()));

        Assert.assertEquals(1, loadResult.getData().size());

        SiteDTO secondRead = loadResult.getData().get(0);


        // confirm that the changes are there
        Assert.assertEquals("site.location.name", newSite.getLocationName(), secondRead.getLocationName());
        Assert.assertEquals("site.attribute[1]", true, secondRead.getAttributeValue(1));
        Assert.assertEquals("site.reportingPeriod[0].indicatorValue[0]", 996.0, secondRead.getIndicatorValue(1), 0.1);
        Assert.assertEquals("site.comments", newSite.getComments(), secondRead.getComments());
        Assert.assertEquals("site.status", newSite.getStatus(), secondRead.getStatus());
        Assert.assertEquals("site.partner", newSite.getPartner().getId(), secondRead.getPartner().getId());
    }


    @Test
    public void testAdminBoundCreate() throws CommandException {
        // create a new detached, client model
        SiteDTO newSite = new SiteDTO();

        newSite.setActivityId(4);
        newSite.setStatus(1);
        newSite.setPartner(new PartnerDTO(1, "Foobar"));
        newSite.setDate1((new GregorianCalendar(2008, 12, 1)).getTime());
        newSite.setDate2((new GregorianCalendar(2009, 1, 3)).getTime());
        newSite.setAdminEntity(1, new AdminEntityDTO(1, 2, "Sud Kivu"));
        newSite.setAdminEntity(2, new AdminEntityDTO(2, 11, "Walungu"));
        newSite.setAdminEntity(3, null);
        newSite.setX(27.432);
        newSite.setY(1.23);
        newSite.setComments("huba huba");

        // create command

        CreateEntity cmd = CreateEntity.Site(newSite);

        // execute the command

        setUser(1);

        CreateResult result = (CreateResult) execute(cmd);
        newSite.setId(result.getNewId());


        // try to retrieve what we've created

        PagingLoadResult<SiteDTO> loadResult = execute(GetSites.byId(newSite.getId()));

        Assert.assertEquals(1, loadResult.getData().size());

        SiteDTO secondRead = loadResult.getData().get(0);


        // confirm that the changes are there
        Assert.assertEquals("site.location.name", "Walungu", secondRead.getLocationName());
    }


    @Test
    public void testAllAttribsFalse() throws CommandException {
        // create a new detached, client model
        SiteDTO newSite = new SiteDTO();

        newSite.setActivityId(1);
        newSite.setStatus(-1);
        newSite.setPartner(new PartnerDTO(1, "Foobar"));
        newSite.setDate1((new GregorianCalendar(2008, 12, 1)).getTime());
        newSite.setDate2((new GregorianCalendar(2009, 1, 3)).getTime());
        newSite.setLocationName("Virunga");
        newSite.setAttributeValue(1, false);
        newSite.setAttributeValue(2, false);

        // create command

        CreateEntity cmd = CreateEntity.Site(newSite);

        // execute the command

        setUser(1);

        CreateResult result = (CreateResult) execute(cmd);


        // let the client know the command has succeeded
        newSite.setId(result.getNewId());
        //cmd.onCompleted(result);


        // try to retrieve what we've created

        PagingLoadResult<SiteDTO> loadResult = execute(GetSites.byId(newSite.getId()));

        Assert.assertEquals(1, loadResult.getData().size());

        SiteDTO secondRead = loadResult.getData().get(0);


        // confirm that the changes are there
        Assert.assertEquals("site.attribute[2]", false, secondRead.getAttributeValue(1));
        Assert.assertEquals("site.attribute[2]", false, secondRead.getAttributeValue(2));
    }


}
