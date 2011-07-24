package org.sigmah.server.endpoint.gwtrpc;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.sigmah.client.offline.command.CommandQueue;
import org.sigmah.client.offline.command.handler.KeyGenerator;
import org.sigmah.client.offline.command.handler.LocalCreateEntityHandler;
import org.sigmah.server.dao.OnDataSet;
import org.sigmah.server.util.BeanMappingModule;
import org.sigmah.server.util.logging.LoggingModule;
import org.sigmah.shared.command.CreateEntity;
import org.sigmah.shared.command.GetSites;
import org.sigmah.shared.command.handler.GetSitesHandler;
import org.sigmah.shared.command.handler.LocalHandlerTestCase;
import org.sigmah.shared.command.result.CreateResult;
import org.sigmah.shared.dao.SqlSiteTableDAO;
import org.sigmah.shared.dao.SqliteDialect;
import org.sigmah.shared.dto.SiteDTO;
import org.sigmah.shared.exception.CommandException;
import org.sigmah.test.InjectionSupport;
import org.sigmah.test.MockHibernateModule;
import org.sigmah.test.Modules;

import com.extjs.gxt.ui.client.data.PagingLoadResult;

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
    	
    	synchronize();
    	
        // create a new detached, client model
        SiteDTO newSite = SiteDTOs.newSite();

        // create command

        CreateEntity cmd = CreateEntity.Site(newSite);

        // execute the command

        LocalCreateEntityHandler handler = new LocalCreateEntityHandler(localConnection, new KeyGenerator(), new CommandQueue(localConnection));

        CreateResult result = handler.execute(cmd, null);


        // let the client know the command has succeeded
        newSite.setId(result.getNewId());


        // try to retrieve what we've created FROM OUR CLIENT SIDE DATABASE

        GetSitesHandler getSitesHandler = new GetSitesHandler(new SqlSiteTableDAO(localConnection, new SqliteDialect()));
        PagingLoadResult<SiteDTO> loadResult = (PagingLoadResult<SiteDTO>) getSitesHandler.execute(GetSites.byId(newSite.getId()), user);        
        

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
