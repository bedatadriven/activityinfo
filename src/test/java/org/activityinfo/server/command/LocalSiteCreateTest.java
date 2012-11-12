package org.activityinfo.server.command;

import java.util.Date;

import org.activityinfo.server.database.OnDataSet;
import org.activityinfo.server.endpoint.gwtrpc.GwtRpcModule;
import org.activityinfo.server.util.beanMapping.BeanMappingModule;
import org.activityinfo.server.util.logging.LoggingModule;
import org.activityinfo.shared.command.AddPartner;
import org.activityinfo.shared.command.CreateLocation;
import org.activityinfo.shared.command.CreateSite;
import org.activityinfo.shared.command.DeleteSite;
import org.activityinfo.shared.command.GetSites;
import org.activityinfo.shared.command.LocalHandlerTestCase;
import org.activityinfo.shared.command.RemovePartner;
import org.activityinfo.shared.command.result.CreateResult;
import org.activityinfo.shared.command.result.SiteResult;
import org.activityinfo.shared.dto.LocationDTO;
import org.activityinfo.shared.dto.PartnerDTO;
import org.activityinfo.shared.dto.SiteDTO;
import org.activityinfo.shared.exception.CommandException;
import org.activityinfo.test.InjectionSupport;
import org.activityinfo.test.MockHibernateModule;
import org.activityinfo.test.Modules;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;


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
    

    @Test
    @OnDataSet("/dbunit/sites-simple1.db.xml")
    public void delete() throws CommandException {
    	
    	synchronizeFirstTime();

        executeLocally(new DeleteSite(1));
        
        assertThat(executeLocally(GetSites.byId(1)).getTotalLength(), equalTo(0));
        
        synchronize();
        
        assertThat(executeRemotely(GetSites.byId(1)).getTotalLength(), equalTo(0));
        assertThat(executeLocally(GetSites.byId(1)).getTotalLength(), equalTo(0));

    	
    }
	

    @Test
    @OnDataSet("/dbunit/sites-simple1.db.xml")
    public void siteRemovePartnerConflict() {
    	
    	// FIRST U1 adds a new partner
    	
    	int databaseId = 1;
    	
    	PartnerDTO iom = new PartnerDTO();
    	iom.setName("IOM");
    	
    	CreateResult result = executeRemotely(new AddPartner(databaseId, iom));
    	iom.setId(result.getNewId());
    	
    	// Now U2 synchronizes, and adds a new site with this partner
    	
    	synchronizeFirstTime();
    	
    	SiteDTO site = new SiteDTO();
    	site.setId(3343234);
    	site.setActivityId(1);
    	site.setPartner(iom);
    	site.setDate1(new Date());
    	site.setDate2(new Date());
    	site.setLocationId(1);
    	
    	executeLocally(new CreateSite(site));
    	
    	// At T+3, U2 thinks better, removes IOM
    	
    	executeRemotely(new RemovePartner(databaseId, iom.getId()));
    	
    	// At T+4, U1 synchronizes, and IOM is removed, but site remains
    	
    	synchronize();
    	
    	// Verify that there is still a label for this partner
    	
    	SiteResult sites = executeLocally(GetSites.byId(site.getId()));
    	
    	assertThat(sites.getTotalLength(), equalTo(1));
    	assertThat(sites.getData().get(0).getName(), equalTo(site.getName()));
    
    }
    
}
