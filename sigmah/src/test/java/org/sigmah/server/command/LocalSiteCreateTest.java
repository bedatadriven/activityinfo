package org.sigmah.server.command;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.sigmah.server.dao.OnDataSet;
import org.sigmah.server.endpoint.gwtrpc.GwtRpcModule;
import org.sigmah.server.util.BeanMappingModule;
import org.sigmah.server.util.logging.LoggingModule;
import org.sigmah.shared.command.AddLocation;
import org.sigmah.shared.command.CreateSite;
import org.sigmah.shared.command.GetSites;
import org.sigmah.shared.command.LocalHandlerTestCase;
import org.sigmah.shared.command.result.CreateResult;
import org.sigmah.shared.command.result.SiteResult;
import org.sigmah.shared.dto.LocationDTO2;
import org.sigmah.shared.dto.SiteDTO;
import org.sigmah.shared.exception.CommandException;
import org.sigmah.test.InjectionSupport;
import org.sigmah.test.MockHibernateModule;
import org.sigmah.test.Modules;

@RunWith(InjectionSupport.class)
@Modules({
        MockHibernateModule.class,
        BeanMappingModule.class,
        GwtRpcModule.class,
        LoggingModule.class
})
public class LocalSiteCreateTest extends LocalHandlerTestCase {

    @Test
    @OnDataSet("/dbunit/sites-simple1.db.xml")
    public void createNew() throws CommandException {
    	
    	synchronizeFirstTime();
    	
        // create a new detached, client model
        SiteDTO newSite = SiteDTOs.newSite();
        
        LocationDTO2 location = LocationDTOs.newLocation();
        CreateResult createLocation = executeLocally(new AddLocation().setLocation(location));
        location.setId(createLocation.getNewId());

        newSite.setLocation(location);
        // create command

        CreateSite cmd = new CreateSite(newSite);

        // execute the command

        CreateResult result = executeLocally(cmd);


        // let the client know the command has succeeded
        newSite.setId(result.getNewId());


        // try to retrieve what we've created FROM OUR CLIENT SIDE DATABASE

        SiteResult loadResult = executeLocally(GetSites.byId(newSite.getId()));        
        

        Assert.assertEquals(1, loadResult.getData().size());

        SiteDTO secondRead = loadResult.getData().get(0);


        // confirm that the changes are there
        SiteDTOs.validateNewSite(secondRead);
        
        newRequest();
        
        // now Sync with the server
        synchronize();
        

        // Verify that 
        
    	
    }
	
}
