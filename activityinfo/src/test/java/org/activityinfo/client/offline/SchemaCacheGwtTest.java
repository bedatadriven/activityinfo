package org.activityinfo.client.offline;

import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.gears.client.database.Database;
import com.google.gwt.gears.client.Factory;
import com.extjs.gxt.ui.client.data.ModelData;

import org.activityinfo.client.mock.DummyData;
import org.activityinfo.client.offline.OfflineSchemaCache;
import org.activityinfo.client.offline.dao.SchemaDAO;
import org.activityinfo.shared.dto.ActivityModel;
import org.activityinfo.shared.dto.Schema;
import org.activityinfo.shared.dto.UserDatabaseDTO;
import org.activityinfo.shared.dto.AttributeGroupModel;
import org.junit.Test;

import java.util.Map;

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

        assertDbEquals(origDb, cachedDb);

        assertEquals(origDb.getCountry().getAdminLevels().get(0).getName(),
                     cachedDb.getCountry().getAdminLevels().get(0).getName());
        assertEquals(origDb.getCountry().getAdminLevels().get(1).getName(),
                     cachedDb.getCountry().getAdminLevels().get(1).getName());
        assertEquals(origDb.getCountry().getAdminLevels().get(2).getName(),
                     cachedDb.getCountry().getAdminLevels().get(2).getName());

    }

    public void assertDbEquals(UserDatabaseDTO original, UserDatabaseDTO cached) {
        assertModelsEquals(original, cached);

        assertEquals("number of partners", original.getPartners().size(), cached.getPartners().size());
        for(int i=0; i!=original.getPartners().size(); ++i) {
            assertModelsEquals(original.getPartners().get(i), cached.getPartners().get(i));
        }

        assertEquals("number of activities", original.getActivities().size(), cached.getActivities().size());
        for(int i=0; i!=original.getActivities().size(); ++i) {
            ActivityModel originalAct = original.getActivities().get(i);
            ActivityModel cachedAct = cached.getActivities().get(i);
            assertModelsEquals(originalAct, cachedAct);

            assertEquals("number of indicators", originalAct.getIndicators().size(), cachedAct.getIndicators().size() );
            for(int j=0;j!=originalAct.getIndicators().size();++j) {
                assertModelsEquals(originalAct.getIndicators().get(j), cachedAct.getIndicators().get(j));
            }

            assertEquals("number of attribute groups", originalAct.getAttributeGroups().size(), cachedAct.getAttributeGroups().size());
            for(int j=0;j!=originalAct.getAttributeGroups().size();++j) {
                AttributeGroupModel origGroup = originalAct.getAttributeGroups().get(j);
                AttributeGroupModel cachedGroup = cachedAct.getAttributeGroups().get(j);
                assertModelsEquals(origGroup, cachedGroup);
                assertEquals("number of atts", origGroup.getAttributes().size(), cachedGroup.getAttributes().size());
                for(int k=0;k!=origGroup.getAttributes().size();++k) {
                    assertModelsEquals(origGroup.getAttributes().get(k), cachedGroup.getAttributes().get(k));
                }
            }
        }
    }


    public void assertModelsEquals(ModelData original, ModelData cached) {
        for(Map.Entry<String,Object> prop : original.getProperties().entrySet()) {
            assertEquals("property " + prop.getKey() + " on '" + original.get("name") + ", " + original.getClass().getName(),
                    prop.getValue(), cached.get(prop.getKey()));
        }

    }
}
