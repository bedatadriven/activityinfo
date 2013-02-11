/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.server.command;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

import java.util.GregorianCalendar;

import org.activityinfo.client.local.command.handler.KeyGenerator;
import org.activityinfo.server.database.OnDataSet;
import org.activityinfo.shared.command.CreateLocation;
import org.activityinfo.shared.command.CreateSite;
import org.activityinfo.shared.command.GetSites;
import org.activityinfo.shared.command.exception.NotAuthorizedException;
import org.activityinfo.shared.command.result.CreateResult;
import org.activityinfo.shared.dto.AdminEntityDTO;
import org.activityinfo.shared.dto.LocationDTO;
import org.activityinfo.shared.dto.PartnerDTO;
import org.activityinfo.shared.dto.ProjectDTO;
import org.activityinfo.shared.dto.SiteDTO;
import org.activityinfo.shared.exception.CommandException;
import org.activityinfo.test.InjectionSupport;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.extjs.gxt.ui.client.data.PagingLoadResult;

@RunWith(InjectionSupport.class)
@OnDataSet("/dbunit/sites-simple1.db.xml")
public class CreateSiteTest extends CommandTestCase2 {

    @Test
    public void test() throws CommandException {
    	LocationDTO location = LocationDTOs.newLocation();
    	execute(new CreateLocation(location));
    	
        SiteDTO newSite = SiteDTOs.newSite();
        newSite.setLocation(location);
        
        CreateSite cmd = new CreateSite(newSite);
        setUser(1);
        
        CreateResult result = execute(cmd);
        newSite.setId(result.getNewId());
        assertThat(result.getNewId(), not(equalTo(0)));
        PagingLoadResult<SiteDTO> loadResult = execute(GetSites.byId(newSite.getId()));
        Assert.assertEquals(1, loadResult.getData().size());
        SiteDTO secondRead = loadResult.getData().get(0);
        SiteDTOs.validateNewSite(secondRead);
    }
    
    @Test(expected=NotAuthorizedException.class)
    public void unauthorized() throws CommandException {
        // create a new detached, client model
        SiteDTO newSite = SiteDTOs.newSite();
        newSite.setPartner(new PartnerDTO(2, "Not NRC"));
        // create command
        CreateSite cmd = new CreateSite(newSite);

        // execute the command

        setUser(2); // bavon (only has access to his own partners in database 1)
        execute(cmd);
    }

	@Test
	@Ignore("WIP")
	public void testAdminBoundCreate() throws CommandException {
        // create a new detached, client model
        SiteDTO newSite = new SiteDTO();

        newSite.setActivityId(4);
        newSite.setPartner(new PartnerDTO(1, "Foobar"));
        newSite.setDate1((new GregorianCalendar(2008, 12, 1)).getTime());
        newSite.setDate2((new GregorianCalendar(2009, 1, 3)).getTime());
        newSite.setAdminEntity(1, new AdminEntityDTO(1, 2, "Sud Kivu"));
        newSite.setAdminEntity(2, new AdminEntityDTO(2, 11, "Walungu"));
        newSite.setAdminEntity(3, null);
        newSite.setX(27.432);
        newSite.setY(1.23);
        newSite.setComments("huba huba");
        newSite.setProject(new ProjectDTO(1,"SomeProject"));

        // create command

        CreateSite cmd = new CreateSite(newSite);

        // execute the command

        setUser(1);        
        newSite.setProject(new ProjectDTO(1,"SomeProject"));


        CreateResult result = execute(cmd);
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
        newSite.setId(new KeyGenerator().generateInt());
        newSite.setActivityId(1);
        newSite.setLocationId(1);
        newSite.setPartner(new PartnerDTO(1, "Foobar"));
        newSite.setDate1((new GregorianCalendar(2008, 12, 1)).getTime());
        newSite.setDate2((new GregorianCalendar(2009, 1, 3)).getTime());
        newSite.setLocationName("Virunga");
        newSite.setAttributeValue(1, false);
        newSite.setAttributeValue(2, false);
        newSite.setProject(new ProjectDTO(1,"SomeProject"));

        // create command

        CreateSite cmd = new CreateSite(newSite);
        assertThat((Integer)cmd.getProperties().get("locationId"), equalTo(1));

        // execute the command

        setUser(1);

        CreateResult result = execute(cmd);


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
