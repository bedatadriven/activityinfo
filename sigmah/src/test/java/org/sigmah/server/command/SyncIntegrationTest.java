/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.command;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Map;

import javax.persistence.EntityManager;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.sigmah.client.offline.command.handler.KeyGenerator;
import org.sigmah.server.command.handler.sync.TimestampHelper;
import org.sigmah.server.database.OnDataSet;
import org.sigmah.server.database.hibernate.entity.AdminEntity;
import org.sigmah.server.database.hibernate.entity.Location;
import org.sigmah.server.database.hibernate.entity.LocationType;
import org.sigmah.server.endpoint.gwtrpc.GwtRpcModule;
import org.sigmah.server.util.beanMapping.BeanMappingModule;
import org.sigmah.server.util.logging.LoggingModule;
import org.sigmah.shared.command.Delete;
import org.sigmah.shared.command.GetSites;
import org.sigmah.shared.command.LocalHandlerTestCase;
import org.sigmah.shared.command.UpdateSite;
import org.sigmah.shared.command.result.SiteResult;
import org.sigmah.shared.dto.AttributeDTO;
import org.sigmah.shared.dto.SiteDTO;
import org.sigmah.shared.util.Collector;
import org.sigmah.test.InjectionSupport;
import org.sigmah.test.MockHibernateModule;
import org.sigmah.test.Modules;

import com.google.common.collect.Maps;
import com.google.inject.Inject;

@RunWith(InjectionSupport.class)
@Modules({
        MockHibernateModule.class,
        BeanMappingModule.class,
        GwtRpcModule.class,
        LoggingModule.class
})
public class SyncIntegrationTest extends LocalHandlerTestCase {
	private final KeyGenerator keyGenerator;

	@Inject
	public SyncIntegrationTest(KeyGenerator keyGenerator) {
		this.keyGenerator = keyGenerator;
	}
	
    private long nowIsh;
	private long awhileBack;

	@Test
	@Ignore("need to update rebar-sql")
    @OnDataSet("/dbunit/sites-simple1.db.xml")
    public void run() throws SQLException, InterruptedException {
        synchronizeFirstTime();
        
        Collector<Date> lastUpdate = Collector.newCollector();
        synchronizer.getLastUpdateTime(lastUpdate);
        
        assertThat(lastUpdate.getResult(), is(not(nullValue())));
        
        assertThat(queryString("select Name from Indicator where IndicatorId=103"),
                equalTo("Nb. of distributions"));
        assertThat(queryString("select Name from AdminLevel where AdminLevelId=1"),
                equalTo("Province"));
        assertThat(queryString("select Name from AdminEntity where AdminEntityId=21"),
                equalTo("Irumu"));
        assertThat(queryString("select Name from Location where LocationId=7"), equalTo("Shabunda"));
        assertThat(queryInt("select value from IndicatorValue where ReportingPeriodId=601 and IndicatorId=6"),
                equalTo(35));

        assertThat(queryInt("select PartnerId from partnerInDatabase where databaseid=2"), equalTo(1));

        assertThat(queryInt("select AttributeGroupId from AttributeGroupInActivity where ActivityId=2"),
                equalTo(1));
        
        assertThat(queryInt("select count(*) from LockedPeriod"), equalTo(4));
        assertThat(queryInt("select count(*) from LockedPeriod where ProjectId is not null"), equalTo(1));
        assertThat(queryInt("select count(*) from LockedPeriod where ActivityId is not null"), equalTo(1));
        assertThat(queryInt("select count(*) from LockedPeriod where UserDatabaseId is not null"), equalTo(2));
        
        /// now try updating a site remotely (from another client)
        // and veryify that we get the update after we synchronized
        
        Thread.sleep(1000);
        
        Map<String, Object> changes = Maps.newHashMap();
        changes.put(AttributeDTO.getPropertyName(1), true);
        changes.put("locationName", "newName");
        executeRemotely(new UpdateSite(1, changes));
        
        synchronize();
        
        SiteResult siteResult = executeLocally(GetSites.byId(1));
        SiteDTO s1 = siteResult.getData().get(0);
        
        assertThat(s1.getAttributeValue(1), equalTo(true));
        assertThat(s1.getAttributeValue(2), equalTo(true));
        assertThat(s1.getLocationName(), equalTo("newName"));
        
        // Try deleting a site
        
        executeRemotely(new Delete("Site", 1));
        
        synchronize();
        
        siteResult = executeLocally(GetSites.byId(1));
       
        assertThat(siteResult.getData().size(), equalTo(0));
        
        
    }


    @Test
    @OnDataSet("/dbunit/locations.db.xml")
    public void locationsAreChunked() throws SQLException, InterruptedException {
        addLocationsToServerDatabase(220);
        synchronizeFirstTime();

        assertThat(Integer.valueOf(queryString("select count(*) from Location")), equalTo(220));
        
        // update a location on the server
        serverEm.getTransaction().begin();
        Location location = (Location) serverEm.createQuery("select l from Location l where l.name = 'Penekusu 26'")
        					.getSingleResult();
        location.setAxe("Motown"); 
        location.setDateEdited(new Date());
        serverEm.getTransaction().commit();
        
        newRequest();
        synchronize();
       
        // todo: store milliseconds in mysql rather than as
        // date time which has resolution limited to 1 second
        Thread.sleep(1000);

        
        assertThat(queryInt("select count(*) from Location where Name='Penekusu 26'"), equalTo(1));
        assertThat(queryString("select axe from Location where Name='Penekusu 26'"), equalTo("Motown"));

        // now create a new location
        Location newLocation = new Location();
        int locationId = keyGenerator.generateInt();
        newLocation.setName("Bukavu");
        newLocation.setDateCreated(new Date());
        newLocation.setDateEdited(new Date());
        newLocation.setId(123456789);
        newLocation.setLocationType(serverEm.find(LocationType.class, 1));
        newLocation.setId(locationId);
        serverEm.getTransaction().begin();
        serverEm.persist(newLocation);
        serverEm.getTransaction().commit();
        
        newRequest();
               
        synchronize();
        
        assertThat(queryString("select name from Location where LocationId = " + locationId), 
        		equalTo("Bukavu"));
    }

    @Test
    @OnDataSet("/dbunit/locations.db.xml")
    public void timeStampSurvivesRoundTrip() {
        EntityManager entityManager = serverEntityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        Date now = new Date();
        Location loc = new Location();
        loc.setDateCreated(new Date(nowIsh+=1500));
        loc.setDateEdited(new Date(nowIsh+=1500));
        loc.setName("Penekusu");
        loc.setLocationType(entityManager.find(LocationType.class, 1));
        entityManager.persist(loc);
        entityManager.getTransaction().commit();
        entityManager.clear();

        entityManager.getTransaction().begin();
        Location loc2 = entityManager.find(Location.class, loc.getId());

        String tsString = TimestampHelper.toString(loc2.getDateCreated());
        Timestamp ts = TimestampHelper.fromString(tsString);

        assertFalse(loc2 == loc);
        assertThat((Timestamp)loc2.getDateCreated(), equalTo(ts));
        entityManager.getTransaction().commit();
        entityManager.clear();


        entityManager.getTransaction().begin();
        Location loc3 = entityManager.find(Location.class, loc.getId());

        assertFalse(ts.after(loc3.getDateCreated()));
        assertFalse(ts.before(loc3.getDateCreated()));


        entityManager.close();

    }

    private void addLocationsToServerDatabase(int count) {
       
    	nowIsh = new Date().getTime();
    	awhileBack = nowIsh - 100000;
    	
    	    	EntityManager entityManager = serverEntityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        for(int i=1;i<= count;++i) {
            Location loc = new Location();
            loc.setId(i);
            if(i%3 == 0) {
            	loc.setDateCreated(new Date(awhileBack+=15000));
            } else {
            	loc.setDateCreated(new Date(nowIsh+=15000));
            }
            loc.setDateEdited(new Date(nowIsh+=15000));
            loc.setName("Penekusu " + i);
            loc.getAdminEntities().add(entityManager.getReference(AdminEntity.class, 2));
            loc.getAdminEntities().add(entityManager.getReference(AdminEntity.class, 12));
            loc.setLocationType(entityManager.find(LocationType.class, 1));
            entityManager.persist(loc);
            entityManager.flush();

            assertTrue(loc.getId() != 0);
        }
        entityManager.getTransaction().commit();
        entityManager.close();
    }


    private String queryString(String sql) throws SQLException {
       return localDatabase.selectString(sql);
    }
    
    private Integer queryInt(String sql) throws SQLException {
    	return localDatabase.selectInt(sql);
    }

}
