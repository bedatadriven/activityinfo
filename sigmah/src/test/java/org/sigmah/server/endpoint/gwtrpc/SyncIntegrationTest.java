/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.endpoint.gwtrpc;

import com.allen_sauer.gwt.log.client.Log;
import com.bedatadriven.rebar.sync.client.BulkUpdaterAsync;
import com.bedatadriven.rebar.sync.mock.MockBulkUpdater;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.sigmah.client.dispatch.AsyncMonitor;
import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.dispatch.remote.Authentication;
import org.sigmah.client.offline.install.Synchronizer;
import org.sigmah.server.dao.OnDataSet;
import org.sigmah.server.sync.TimestampHelper;
import org.sigmah.server.util.BeanMappingModule;
import org.sigmah.server.util.logging.LoggingModule;
import org.sigmah.shared.command.Command;
import org.sigmah.shared.command.result.CommandResult;
import org.sigmah.shared.command.result.SyncRegionUpdate;
import org.sigmah.shared.domain.AdminEntity;
import org.sigmah.shared.domain.Location;
import org.sigmah.shared.domain.LocationType;
import org.sigmah.shared.domain.User;
import org.sigmah.test.InjectionSupport;
import org.sigmah.test.MockHibernateModule;
import org.sigmah.test.Modules;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.sql.*;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

@RunWith(InjectionSupport.class)
@Modules({
        MockHibernateModule.class,
        BeanMappingModule.class,
        GwtRpcModule.class,
        LoggingModule.class
})
public class SyncIntegrationTest {

    @Inject
    private CommandServlet servlet;

    @Inject
    private EntityManagerFactory emf;

    private User user;
    private Dispatcher dispatcher;
    private Connection localDbConnection;
    private BulkUpdaterAsync updater;

    @Before
    public void setUp() throws SQLException, ClassNotFoundException {
        user = new User();
        user.setId(1);
        dispatcher = new MockDispatcher();

        Class.forName("org.sqlite.JDBC");
        localDbConnection = DriverManager.getConnection("jdbc:sqlite::memory:");
        updater = new MockBulkUpdater(localDbConnection);

        Log.setCurrentLogLevel(Log.LOG_LEVEL_DEBUG);
    }

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
    }


    private void synchronize() {
        Synchronizer syncr = new Synchronizer(dispatcher, localDbConnection, updater,
                new Authentication(1, "X", "akbertram@gmail.com"));
        syncr.start();
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
        EntityManager entityManager = emf.createEntityManager();
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
        EntityManager entityManager = emf.createEntityManager();
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
        Statement stmt = localDbConnection.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        if(!rs.next()) {
            throw new AssertionError("No rows returned for '" + sql + "'");
        }
        return rs;
    }



    private class MockDispatcher implements Dispatcher {
        @Override
        public <T extends CommandResult> void execute(Command<T> command, AsyncMonitor monitor, AsyncCallback<T> callback) {
            List<CommandResult> results = servlet.handleCommands(user, Collections.<Command>singletonList(command));
            CommandResult result = results.get(0);

            if(result instanceof SyncRegionUpdate) {
                System.out.println(((SyncRegionUpdate) result).getSql());
            }

            if(result instanceof Exception) {
                throw new Error((Throwable) result);
            } else {
                callback.onSuccess((T) result);
            }
        }
    }
}
