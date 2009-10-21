package org.activityinfo.client.offline.dao;

import com.google.gwt.gears.client.database.Database;
import com.google.gwt.gears.client.database.ResultSet;
import com.google.gwt.gears.client.database.DatabaseException;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.js.JsonConverter;
import static com.extjs.gxt.ui.client.js.JsonConverter.encode;

import java.util.*;

import org.activityinfo.shared.dto.*;

/**
 * Persists the user-visible schema to and from the Gears database.
 *
 *
 * Note: only the properties specifically needed in queries are stored in
 * columns, the rest are stuffed into a JSON-encoded property bag in the properties column.
 * Hopefully this will make changes to the database structure easier to propogate down to the client.
 *
 * @author Alex Bertram
 */
public class SchemaDAO {

    private final Database gearsDb;

    public SchemaDAO(Database gearsDb) throws DatabaseException {
        this.gearsDb = gearsDb;

        createSchemaTables();
    }


    private abstract class ModelCreator<T extends ModelData> {

        public T create(ResultSet rs, Map<String,Object> properties) throws DatabaseException {
            T model = create(rs);
            for(Map.Entry<String,Object> property : properties.entrySet()) {
                model.set(property.getKey(), property.getValue());
            }
            return model;
        }

        public T create(ResultSet rs) throws DatabaseException {
            return null;
        }
    }



    private void createSchemaTables() throws DatabaseException {

        gearsDb.execute("create table if not exists SchemaUpdate (version integer, updateTime date)");

        gearsDb.execute("create table if not exists Country (id integer primary key, " +
                "name text, properties text)");

        gearsDb.execute("create table if not exists AdminLevel (id integer primary key, countryId integer, " +
                "parentLevelId integer, name text, properties text, generation integer)");

        gearsDb.execute("create table if not exists LocationType (id integer primary key, countryId integer, " +
                "name text, properties text)");

        gearsDb.execute("create table if not exists UserDatabase (id integer primary key, " +
                "countryId integer, name text, properties text)");

        gearsDb.execute("create table if not exists Partner (id integer primary key, name text, properties text)");

        gearsDb.execute("create table if not exists PartnerInDatabase (partnerId int, databaseId int)");

        gearsDb.execute("create table if not exists Activity (id integer primary key, " +
                "databaseId integer, name text, sortOrder int, properties text)");

        gearsDb.execute("create table if not exists Indicator (id integer primary key, activityId integer, " +
                "name text, sortOrder int, properties text)");

        gearsDb.execute("create table if not exists AttributeGroup (id integer primary key, activityId integer, " +
                "name text, sortOrder int, properties text)");

        gearsDb.execute("create table if not exists AttributeGroupInActivity " +
                "(activityId integer, attributeGroupId integer, sortOrder integer)");

        gearsDb.execute("create table if not exists Attribute(id integer, attributeGroupId integer, " +
                "name text, sortOrder int, properties text)");
    }

    private void insertCountry(CountryModel country) throws DatabaseException {

        gearsDb.execute("insert into Country (id, name, properties) values (?, ?, ?)",
                Integer.toString(country.getId()),
                country.getName(),
                encode(country.getProperties()).toString());

        for(AdminLevelModel level : country.getAdminLevels()) {
            insertAdminLevel(country, level);
        }

        for(LocationTypeModel locationType : country.getLocationTypes()) {
            insertLocationType(country, locationType);
        }
    }

    private void insertAdminLevel(CountryModel country, AdminLevelModel level) throws DatabaseException {
        gearsDb.execute("insert into AdminLevel (id, countryId, parentLevelId, name, properties, generation) " +
                " values (?, ?, ?, ?, ?, ?)",
                Integer.toString(level.getId()),
                Integer.toString(country.getId()),
                level.isRoot() ? "" : Integer.toString(level.getParentLevelId()),
                level.getName(),
                encode(level.getProperties()).toString(),
                "0");
    }

    private void insertLocationType(CountryModel country, LocationTypeModel type) throws DatabaseException {
        gearsDb.execute("insert into LocationType (id, countryId, name, properties) values (?, ?, ?, ?)",
                Integer.toString(type.getId()),
                Integer.toString(country.getId()),
                type.getName(),
                encode(type.getProperties()).toString());

    }

    private void insertDatabase(UserDatabaseDTO db) throws DatabaseException {

        gearsDb.execute("insert into UserDatabase (id, countryId, name, properties) values (?, ?, ?, ?)",
                Integer.toString(db.getId()),
                Integer.toString(db.getCountry().getId()),
                db.getName(),
                encode(db.getProperties()).toString());
    }

    private void insertPartner(PartnerModel partner) throws DatabaseException {

        gearsDb.execute("insert into Partner (id, name, properties) values (?, ?, ?)",
                Integer.toString(partner.getId()),
                partner.getName(),
                encode(partner.getProperties()).toString());
    }

    private void insertPartnerInDatabase(UserDatabaseDTO db, PartnerModel partner) throws DatabaseException {

        gearsDb.execute("insert into PartnerInDatabase (databaseId, partnerId) values (?, ?)",
                Integer.toString(db.getId()),
                Integer.toString(partner.getId()));
    }

    private void insertActivity(ActivityModel activity, int sortOrder)
            throws DatabaseException {

        gearsDb.execute("insert into Activity (id, databaseId, name, sortOrder, properties) values (?, ?, ?, ?, ?)",
                Integer.toString(activity.getId()),
                Integer.toString(activity.getDatabase().getId()),
                activity.getName(),
                Integer.toString(sortOrder),
                encode(activity.getProperties()).toString());

    }

    private void insertIndicator(ActivityModel activity, IndicatorModel indicator, int sortOrder)
            throws DatabaseException {

        gearsDb.execute("insert into Indicator (id, activityId, name, sortOrder, properties) values (?, ?, ?, ?, ?)",
                Integer.toString(indicator.getId()),
                Integer.toString(activity.getId()),
                indicator.getName(),
                Integer.toString(sortOrder),
                encode(indicator.getProperties()).toString());
    }

    private void insertAttributeGroup(ActivityModel activity, AttributeGroupModel attributeGroup, int sortOrder)
            throws DatabaseException {

        gearsDb.execute("insert into AttributeGroup (id, name, properties) values (?, ?, ?)",
                Integer.toString(attributeGroup.getId()),
                attributeGroup.getName(),
                encode(attributeGroup.getProperties()).toString());

    }

    private void insertAttribute(AttributeGroupModel attributeGroup, AttributeModel attribute, int sortOrder)
        throws DatabaseException {
        gearsDb.execute("insert into Attribute (id, attributeGroupId, name, sortOrder, properties) values (?,?,?,?,?)",
                Integer.toString(attribute.getId()),
                Integer.toString(attributeGroup.getId()),
                attribute.getName(),
                Integer.toString(sortOrder),
                encode(attribute.getProperties()).toString());
    }



//
//    private void serializePropertyMap(Map<String, Object> map) {
//        StringBuilder sb = new StringBuilder();
//        for(Map.Entry<String, Object> entry : map.entrySet()) {
//            String type = null;
//            String valueString = null;
//
//            if(entry.getValue() == null) {
//
//            } else if(entry.getValue() instanceof Integer) {
//                type = "i";
//                valueString = ((Integer)entry.getValue()).toString();
//            } else if(entry.getValue() instanceof String) {
//                type = Integer.toString(((String) entry.getValue()).length());
//                valueString = (String) entry.getValue();
//            } else if(entry.getValue() instanceof Double) {
//                type = "d";
//                valueString = ((Double)entry.getValue()).toString();
//            } else if(entry.getValue() instanceof Boolean) {
//                type = "b";
//                valueString = ((Boolean)entry.getValue()) ? "1" : "0";
//            }
//
//            if(type != null) {
//                sb.append(entry.getKey()).append("|").append(valueString);
//            }
//        }
//    }
//
//    private Map<String,Object> deserializePropertyMap(String stm) {
//        Map<String,Object> map = new HashMap<String,Object>();
//
//        int i=0;
//        while(i < stm.length()) {
//
//            int nextToken = stm.indexOf('|', i);
//            String property = stm.substring(i, nextToken);
//            i=nextToken+1;
//
//            nextToken = stm.indexOf('|', i);
//            String type = stm.substring(i, nextToken);
//            i=nextToken+1;
//
//
//            if("i".equals(type)) {
//                nextToken = stm.indexOf('|', i);
//                String type = stm.substring(i, nextToken)
//            }
//
//
//
//
//        }
//
//    }

    public void save(Schema schema) throws DatabaseException {

        gearsDb.execute("delete from SchemaUpdate");
        gearsDb.execute("delete from UserDatabase");
        gearsDb.execute("delete from Country");
        gearsDb.execute("delete from AdminLevel");
        gearsDb.execute("delete from LocationType");
        gearsDb.execute("delete from Partner");
        gearsDb.execute("delete from PartnerInDatabase");
        gearsDb.execute("delete from Activity");
        gearsDb.execute("delete from Indicator");

        Set<Integer> countriesInserted = new HashSet<Integer>();
        Set<Integer> partnersInserted = new HashSet<Integer>();
        Set<Integer> attributeGroupsInserted = new HashSet<Integer>();

        for(UserDatabaseDTO db : schema.getDatabases()) {

            int sortOrder = 0;

            insertDatabase(db);

            CountryModel country = db.getCountry();
            if(!countriesInserted.contains(country.getId())) {
                insertCountry(country);
                countriesInserted.add(country.getId());
            }

            for(PartnerModel partner : db.getPartners()) {
                if(!partnersInserted.contains(partner.getId())) {
                    insertPartner(partner);
                    partnersInserted.add(partner.getId());
                }
                insertPartnerInDatabase(db, partner);
            }

            for(ActivityModel activity : db.getActivities()) {
                insertActivity(activity, sortOrder++);

                for(IndicatorModel indicator : activity.getIndicators()) {
                    insertIndicator(activity, indicator, sortOrder++);
                }

                for(AttributeGroupModel attributeGroup : activity.getAttributeGroups()) {
                    if(!attributeGroupsInserted.contains(attributeGroup.getId())) {
                        insertAttributeGroup(activity, attributeGroup, sortOrder++);
                        attributeGroupsInserted.add(attributeGroup.getId());
                        for(AttributeModel attribute: attributeGroup.getAttributes()) {
                            insertAttribute(attributeGroup, attribute, sortOrder++);
                        }
                    }
                    gearsDb.execute("insert into AttributeGroupInActivity (activityId, attributeGroupId, sortOrder) values(?,?, ?)",
                            Integer.toString(activity.getId()),
                            Integer.toString(attributeGroup.getId()),
                            Integer.toString(sortOrder++));

                }
            }
        }

        gearsDb.execute("insert into SchemaUpdate (version, updateTime) values (?, ?)",
                Long.toString(schema.getVersion()),
                new Date().toString());

    }



    private <T extends ModelData> Map<Integer, T> loadModels(String query, ModelCreator<T> creator)
            throws DatabaseException {
        Map<Integer, T> map = new HashMap<Integer,T>();
        ResultSet rs = gearsDb.execute(query);
        while(rs.isValidRow()) {

            Map<String, Object> properties = JsonConverter.decode(rs.getFieldAsString(0));

            T model = creator.create(rs, properties);

            map.put((Integer) properties.get("id"), model);

            rs.next();
        }
        return map;
    }

    public Schema load() throws DatabaseException {

        Schema schema = new Schema();

        ResultSet rs = gearsDb.execute("select version, updateTime from SchemaUpdate");
        if(!rs.isValidRow()) {
            return null;  // no schema has been saved
        }

        schema.setVersion(rs.getFieldAsLong(0));

        final Map<Integer, CountryModel> countries = loadModels("select properties from Country", new ModelCreator<CountryModel>() {
            public CountryModel create(ResultSet rs) {
                return new CountryModel();
            }
        });

        final Map<Integer, AttributeGroupModel> attributeGroups = loadModels("select properties from AttributeGroup",
                new ModelCreator<AttributeGroupModel>() {
                    @Override
                    public AttributeGroupModel create(ResultSet rs) throws DatabaseException {
                        return new AttributeGroupModel();
                    }
                });

        loadModels("select properties, attributeGroupId from Attribute order by sortOrder",
                new ModelCreator<AttributeModel>() {
                    @Override
                    public AttributeModel create(ResultSet rs) throws DatabaseException {
                        AttributeModel attribute = new AttributeModel();
                        attributeGroups.get(rs.getFieldAsInt(1)).getAttributes().add(attribute);
                        return attribute;
                    }
                });



        loadModels("select properties, countryId from AdminLevel",
                new ModelCreator<AdminLevelModel>() {
                    @Override
                    public AdminLevelModel create(ResultSet rs) throws DatabaseException {
                        AdminLevelModel level = new AdminLevelModel();
                        CountryModel country = countries.get(rs.getFieldAsInt(1));
                        country.getAdminLevels().add(level);
                        return level;
                    }
                });

        loadModels("select properties, countryId from LocationType order by name",
                new ModelCreator<LocationTypeModel>() {
                    @Override
                    public LocationTypeModel create(ResultSet rs) throws DatabaseException {
                        LocationTypeModel type = new LocationTypeModel();
                        CountryModel country = countries.get(rs.getFieldAsInt(1));
                        country.getLocationTypes().add(type);
                        return type;
                    }
                });

        final Map<Integer, UserDatabaseDTO> databases = loadModels(
                "select properties, countryId from UserDatabase order by name",
                new ModelCreator<UserDatabaseDTO>() {
                    public UserDatabaseDTO create(ResultSet rs) throws DatabaseException {
                        UserDatabaseDTO db = new UserDatabaseDTO();
                        db.setCountry(countries.get(rs.getFieldAsInt(1)));
                        return db;
                    }
                });

        loadModels("select p.properties, d.id from Partner p " +
                "left join PartnerInDatabase link on (p.id = link.partnerId) " +
                "left join UserDatabase d on (link.databaseId = d.id) order by p.name",
                new ModelCreator<PartnerModel>() {
                    public PartnerModel create(ResultSet rs) throws DatabaseException {
                        PartnerModel partner = new PartnerModel();
                        UserDatabaseDTO db = databases.get(rs.getFieldAsInt(1));
                        db.getPartners().add(partner);
                        return partner;
                    }
               });


        final Map<Integer, ActivityModel> activities = loadModels(
                "select properties, databaseId from Activity order by sortOrder",
                new ModelCreator<ActivityModel>() {
                    public ActivityModel create(ResultSet rs, Map<String,Object> properties) throws DatabaseException {
                        ActivityModel activity = new ActivityModel(properties);
                        UserDatabaseDTO db = databases.get(rs.getFieldAsInt(1));
                        activity.setDatabase(db);

                        db.getActivities().add(activity);
                        return activity;
                    }
                });

        loadModels("select properties, activityId from Indicator order by sortOrder",
                new ModelCreator<IndicatorModel>() {
                    public IndicatorModel create(ResultSet rs) throws DatabaseException {
                        IndicatorModel indicator = new IndicatorModel();
                        ActivityModel activity = activities.get(rs.getFieldAsInt(1));
                        activity.getIndicators().add(indicator);
                        return indicator;
                    }
                });


        rs = gearsDb.execute("select activityid, attributeGroupId from AttributeGroupInActivity order by sortOrder");
        while(rs.isValidRow()) {
            activities.get(rs.getFieldAsInt(0)).getAttributeGroups().add(
                    attributeGroups.get(rs.getFieldAsInt(1)));
            rs.next();
        }

        schema.getDatabases().addAll(databases.values());

        return schema;

    }




}
