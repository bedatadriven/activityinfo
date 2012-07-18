package org.activityinfo.server.command;

import org.activityinfo.server.database.OnDataSet;
import org.activityinfo.server.endpoint.gwtrpc.GwtRpcModule;
import org.activityinfo.server.util.beanMapping.BeanMappingModule;
import org.activityinfo.server.util.logging.LoggingModule;
import org.activityinfo.shared.command.CreateLocation;
import org.activityinfo.shared.command.CreateSite;
import org.activityinfo.shared.command.GetSites;
import org.activityinfo.shared.command.LocalHandlerTestCase;
import org.activityinfo.shared.command.result.CreateResult;
import org.activityinfo.shared.command.result.SiteResult;
import org.activityinfo.shared.dto.LocationDTO;
import org.activityinfo.shared.dto.SiteDTO;
import org.activityinfo.shared.exception.CommandException;
import org.activityinfo.test.InjectionSupport;
import org.activityinfo.test.MockHibernateModule;
import org.activityinfo.test.Modules;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

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
        

        // Confirm that paging works client side
        GetSites pagingRequest = new GetSites();
        pagingRequest.setLimit(1);
    	
        loadResult = executeLocally(pagingRequest);
        
    }
	
}
