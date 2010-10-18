/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.endpoint.gwtrpc;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.sigmah.server.dao.OnDataSet;
import org.sigmah.server.sync.TimestampHelper;
import org.sigmah.server.util.BeanMappingModule;
import org.sigmah.server.util.logging.LoggingModule;
import org.sigmah.shared.command.handler.LocalHandlerTestCase;
import org.sigmah.shared.domain.AdminEntity;
import org.sigmah.shared.domain.Location;
import org.sigmah.shared.domain.LocationType;
import org.sigmah.test.InjectionSupport;
import org.sigmah.test.MockHibernateModule;
import org.sigmah.test.Modules;

import javax.persistence.EntityManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Date;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;

@RunWith(InjectionSupport.class)
@Modules({
        MockHibernateModule.class,
        BeanMappingModule.class,
        GwtRpcModule.class,
        LoggingModule.class
})
public class SyncIntegrationTest extends LocalHandlerTestCase {

    @Test
    @OnDataSet("/dbunit/sites-simple1.db.xml")
    public void run() throws SQLException {
        synchronize();

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

    }


    @Test
    @OnDataSet("/dbunit/locations.db.xml")
    public void locationsAreChunked() throws SQLException {
        addLocationsToServerDatabase(53);
        synchronize();

        assertThat(Integer.valueOf(queryString("select count(*) from Location")), equalTo(53));
    }

    @Test
    @OnDataSet("/dbunit/locations.db.xml")
    public void timeStampSurvivesRoundTrip() {
        EntityManager entityManager = serverEntityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        Date now = new Date();
        Location loc = new Location();
        loc.setDateCreated(now);
        loc.setDateEdited(now);
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
        EntityManager entityManager = serverEntityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        Timestamp now = new Timestamp(new Date().getTime());
        for(int i=1;i<= count;++i) {
            Location loc = new Location();
            loc.setDateCreated(now);
            loc.setDateEdited(now);
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
        ResultSet rs = querySingleResult(sql);

        return rs.getString(1);
    }

    private Integer queryInt(String sql) throws SQLException {
        ResultSet rs = querySingleResult(sql);
        return rs.getInt(1);
    }

    private ResultSet querySingleResult(String sql) throws SQLException {
        Statement stmt = localConnection.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        if(!rs.next()) {
            throw new AssertionError("No rows returned for '" + sql + "'");
        }
        return rs;
    }
}
