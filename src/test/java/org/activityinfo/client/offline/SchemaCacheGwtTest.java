package org.activityinfo.client.offline;

import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.gears.client.database.Database;
import com.google.gwt.gears.client.Factory;

import org.activityinfo.client.mock.DummyData;
import org.activityinfo.client.offline.OfflineSchemaCache;
import org.activityinfo.client.offline.dao.SchemaDAO;
import org.activityinfo.shared.dto.ActivityModel;
import org.activityinfo.shared.dto.Schema;
import org.activityinfo.shared.dto.UserDatabaseDTO;
import org.junit.Test;

public class SchemaCacheGwtTest extends GWTTestCase {

    @Override
   public String getModuleName() {
       return "org.activityinfo.ApplicationTest";
   }


    @Test
    public void test() throws Exception {

        Database db = Factory.getInstance().createDatabase();
        db.open("test");

        SchemaDAO cache = new SchemaDAO(db);
        Schema original = DummyData.PEAR();
        cache.save(original);

        Schema cached = cache.load();

        assertEquals("db count", 1, cached.getDatabases().size());

        UserDatabaseDTO origDb = original.getDatabases().get(0);
        UserDatabaseDTO cachedDb = cached.getDatabases().get(0);

        assertEquals("db name", origDb.getName(), cachedDb.getName());
        assertEquals("db full name", origDb.getFullName(), cachedDb.getFullName());
        assertEquals("country", origDb.getCountry().getName(), cachedDb.getCountry().getName());
        assertEquals("country admin count", 3, cachedDb.getCountry().getAdminLevels().size());

        assertEquals(origDb.getCountry().getAdminLevels().get(0).getName(),
                     cachedDb.getCountry().getAdminLevels().get(0).getName());
        assertEquals(origDb.getCountry().getAdminLevels().get(1).getName(),
                     cachedDb.getCountry().getAdminLevels().get(1).getName());
        assertEquals(origDb.getCountry().getAdminLevels().get(2).getName(),
                     cachedDb.getCountry().getAdminLevels().get(2).getName());

        ActivityModel cachedAct = cached.getActivityById(91);
        ActivityModel origAct = original.getActivityById(91);

        assertEquals(origAct.getName(), cachedAct.getName());
        assertEquals(origAct.getAllowEdit(), cachedAct.getAllowEdit());
        assertEquals(origAct.getDatabase().getId(), cachedAct.getDatabase().getId());
        assertEquals(origAct.getLocationType().getId(), cachedAct.getLocationType().getId());

        assertEquals("partner count", origDb.getPartners().size(), cachedDb.getPartners().size());
        assertEquals(origDb.getPartners().size(), cachedDb.getPartners().size());
        assertEquals(origDb.getPartners().get(0).getName(), cachedDb.getPartners().get(0).getName());

    }



}
