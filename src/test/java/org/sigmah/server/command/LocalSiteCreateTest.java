package org.sigmah.server.command;

import org.activityinfo.shared.command.CreateLocation;
import org.activityinfo.shared.command.CreateSite;
import org.activityinfo.shared.command.GetSites;
import org.activityinfo.shared.command.result.CreateResult;
import org.activityinfo.shared.command.result.SiteResult;
import org.activityinfo.shared.dto.LocationDTO;
import org.activityinfo.shared.dto.SiteDTO;
import org.activityinfo.shared.exception.CommandException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.sigmah.server.database.OnDataSet;
import org.sigmah.server.endpoint.gwtrpc.GwtRpcModule;
import org.sigmah.server.util.beanMapping.BeanMappingModule;
import org.sigmah.server.util.logging.LoggingModule;
import org.sigmah.shared.command.LocalHandlerTestCase;
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
        
        LocationDTO location = LocationDTOs.newLocation();
        executeLocally(new CreateLocation(location));

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
