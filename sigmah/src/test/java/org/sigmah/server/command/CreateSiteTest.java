/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.command;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

import java.util.GregorianCalendar;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.sigmah.server.database.OnDataSet;
import org.sigmah.shared.command.AddLocation;
import org.sigmah.shared.command.CreateSite;
import org.sigmah.shared.command.GetSites;
import org.sigmah.shared.command.exception.NotAuthorizedException;
import org.sigmah.shared.command.result.CreateResult;
import org.sigmah.shared.dto.AdminEntityDTO;
import org.sigmah.shared.dto.LocationDTO;
import org.sigmah.shared.dto.PartnerDTO;
import org.sigmah.shared.dto.ProjectDTO;
import org.sigmah.shared.dto.SiteDTO;
import org.sigmah.shared.exception.CommandException;
import org.sigmah.test.InjectionSupport;

import com.extjs.gxt.ui.client.data.PagingLoadResult;

@RunWith(InjectionSupport.class)
@OnDataSet("/dbunit/sites-simple1.db.xml")
public class CreateSiteTest extends CommandTestCase2 {

    @Test
    public void test() throws CommandException {
    	LocationDTO location = LocationDTOs.newLocation();
    	CreateResult createLocation = execute(new AddLocation().setLocation(location));
    	location.setId(createLocation.getNewId());
    	
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

        newSite.setActivityId(1);
        newSite.setPartner(new PartnerDTO(1, "Foobar"));
        newSite.setDate1((new GregorianCalendar(2008, 12, 1)).getTime());
        newSite.setDate2((new GregorianCalendar(2009, 1, 3)).getTime());
        newSite.setLocationName("Virunga");
        newSite.setAttributeValue(1, false);
        newSite.setAttributeValue(2, false);
        newSite.setProject(new ProjectDTO(1,"SomeProject"));
        newSite.setLocation(LocationDTOs.newLocation());

        // create command

        CreateSite cmd = new CreateSite(newSite);

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
