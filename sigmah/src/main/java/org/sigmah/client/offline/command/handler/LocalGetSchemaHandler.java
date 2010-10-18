/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.offline.command.handler;

import com.google.inject.Inject;
import org.sigmah.client.dispatch.remote.Authentication;
import org.sigmah.shared.command.GetSchema;
import org.sigmah.shared.command.handler.CommandHandler;
import org.sigmah.shared.dao.SqlQueryBuilder;
import org.sigmah.shared.domain.User;
import org.sigmah.shared.dto.*;
import org.sigmah.shared.exception.CommandException;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.sigmah.shared.dao.SqlQueryBuilder.select;

public class LocalGetSchemaHandler implements CommandHandler<GetSchema> {

    private Connection connection;
    private Authentication auth;

    @Inject
    public LocalGetSchemaHandler(Connection connection, Authentication auth) {
        this.connection = connection;
        this.auth = auth;
    }

    @Override
    public SchemaDTO execute(GetSchema cmd, User user) throws CommandException {
        return new SchemaBuilder().build();
    }

    private class SchemaBuilder {
        final List<UserDatabaseDTO> databaseList = new ArrayList<UserDatabaseDTO>();
        final Map<Integer, UserDatabaseDTO> databaseMap = new HashMap<Integer, UserDatabaseDTO>();

        final Map<Integer, CountryDTO> countries = new HashMap<Integer, CountryDTO>();
        final Map<Integer, PartnerDTO> partners = new HashMap<Integer, PartnerDTO>();
        final Map<Integer, ActivityDTO> activities = new HashMap<Integer, ActivityDTO>();
        final Map<Integer, AttributeGroupDTO> attributeGroups = new HashMap<Integer, AttributeGroupDTO>();

        public void loadCountries() {
            select("CountryId, Name, X1, Y1, X2, Y2").from("Country").forEachResult(connection, new SqlQueryBuilder.ResultHandler() {
                @Override
                public void handle(ResultSet rs) throws SQLException {
                    CountryDTO country = new CountryDTO();
                    country.setId(rs.getInt(1));
                    country.setName(rs.getString(2));

                    BoundingBoxDTO bounds = new BoundingBoxDTO();
                    bounds.setX1(rs.getDouble(3));
                    bounds.setY1(rs.getDouble(4));
                    bounds.setX2(rs.getDouble(5));
                    bounds.setY2(rs.getDouble(6));
                    country.setBounds(bounds);

                    countries.put(country.getId(), country);
                }
            });
        }

        public void loadLocationTypes() {
            select("LocationTypeId, Name, BoundAdminLevelId, CountryId")
                .from("LocationType")
                .forEachResult(connection, new SqlQueryBuilder.ResultHandler() {

                @Override
                public void handle(ResultSet rs) throws SQLException {
                    LocationTypeDTO type = new LocationTypeDTO();
                    type.setId(rs.getInt(1));
                    type.setName(rs.getString(2));


                    int boundAdminLevelId = rs.getInt(3);
                    if(!rs.wasNull()) {
                        type.setBoundAdminLevelId(boundAdminLevelId);
                    }

                    int countryId = rs.getInt(4);
                    countries.get(countryId).getLocationTypes().add(type);
                }
            });
        }

        public void loadAdminLevels() {
            select("AdminLevelId, Name, ParentId, CountryId")
                .from("AdminLevel")
                .forEachResult(connection, new SqlQueryBuilder.ResultHandler() {

                @Override
                public void handle(ResultSet rs) throws SQLException {
                    AdminLevelDTO level = new AdminLevelDTO();
                    level.setId(rs.getInt(1));
                    level.setName(rs.getString(2));

                    int parentId = rs.getInt(3);
                    if(!rs.wasNull()) {
                        level.setParentLevelId(parentId);
                    }

                    int countryId = rs.getInt(4);
                    countries.get(countryId).getAdminLevels().add(level);
                }
            });
        }

        public void loadDatabases() {
            select("d.DatabaseId, d.Name, d.FullName, d.OwnerUserId, d.CountryId, o.Name, o.Email, " +
                    "p.AllowViewAll, p.AllowEdit, p.AllowEditAll, " +
                    "p.AllowManageUsers, p.AllowManageAllUsers, p.AllowDesign")
                    .from("UserDatabase d " +
                            "LEFT JOIN UserPermission p ON (d.DatabaseId = p.DatabaseId) " +
                            "LEFT JOIN UserLogin o ON (d.OwnerUserId = o.UserId)")
                    .orderBy("d.Name")
                    .forEachResult(connection, new SqlQueryBuilder.ResultHandler() {
                @Override
                public void handle(ResultSet rs) throws SQLException {
                    UserDatabaseDTO db = new UserDatabaseDTO();
                    db.setId(rs.getInt(1));
                    db.setName(rs.getString(2));
                    db.setFullName(rs.getString(3));
                    db.setAmOwner(rs.getInt(4) == auth.getUserId());
                    db.setCountry(countries.get(rs.getInt(5)));
                    db.setOwnerName(rs.getString(6));
                    db.setOwnerEmail(rs.getString(7));

                    db.setViewAllAllowed(db.getAmOwner() || rs.getBoolean(8));
                    db.setEditAllowed(db.getAmOwner() || rs.getBoolean(9));
                    db.setEditAllAllowed(db.getAmOwner() || rs.getBoolean(10));
                    db.setManageUsersAllowed(db.getAmOwner() || rs.getBoolean(11));
                    db.setManageAllUsersAllowed(db.getAmOwner() || rs.getBoolean(12));
                    db.setDesignAllowed(db.getAmOwner() || rs.getBoolean(13));

                    databaseMap.put(db.getId(), db);
                    databaseList.add(db);
                }
            });
        }

        private void loadPartners() {
            select("PartnerId, Name, FullName").from("Partner")
                    .forEachResult(connection, new SqlQueryBuilder.ResultHandler() {

                @Override
                public void handle(ResultSet rs) throws SQLException {
                    PartnerDTO partner = new PartnerDTO();
                    partner.setId(rs.getInt(1));
                    partner.setName(rs.getString(2));
                    partner.setFullName(rs.getString(3));

                    partners.put(partner.getId(), partner);
                }
            });
        }

        private void joinPartnersToDatabases() {
            select("DatabaseId, PartnerId")
                    .from("PartnerInDatabase")
                    .forEachResult(connection, new SqlQueryBuilder.ResultHandler() {
                @Override
                public void handle(ResultSet rs) throws SQLException {

                    int dbId = rs.getInt(1);
                    int partnerId = rs.getInt(2);

                    databaseMap.get(dbId).getPartners().add(
                        partners.get(partnerId));
                }
            });
        }


        public void loadActivities() {
            select("ActivityId, Name, Category, LocationTypeId, ReportingFrequency, DatabaseId")
                    .from("Activity")
                    .orderBy("SortOrder")
                    .forEachResult(connection, new SqlQueryBuilder.ResultHandler() {
                @Override
                public void handle(ResultSet rs) throws SQLException {
                    ActivityDTO activity = new ActivityDTO();
                    activity.setId(rs.getInt(1));
                    activity.setName(rs.getString(2));
                    activity.setCategory(rs.getString(3));
                    activity.setLocationTypeId(rs.getInt(4));
                    activity.setReportingFrequency(rs.getInt(5));

                    int databaseId = rs.getInt(6);
                    UserDatabaseDTO database = databaseMap.get(databaseId);
                    activity.setDatabase(database);
                    database.getActivities().add(activity);
                    activities.put(activity.getId(), activity);
                }
            });
        }

        public void loadIndicators() {
            select("IndicatorId, Name, Category, ListHeader, Description, Aggregation",
                     "Units, CollectIntervention, CollectMonitoring, ActivityId")
                    .from("Indicator")
                    .orderBy("SortOrder")
                    .forEachResult(connection, new SqlQueryBuilder.ResultHandler() {

                @Override
                public void handle(ResultSet rs) throws SQLException {
                    IndicatorDTO indicator = new IndicatorDTO();
                    indicator.setId(rs.getInt(1));
                    indicator.setName(rs.getString(2));
                    indicator.setCategory(rs.getString(3));
                    indicator.setListHeader(rs.getString(4));
                    indicator.setDescription(rs.getString(5));
                    indicator.setAggregation(rs.getInt(6));
                    indicator.setUnits(rs.getString(7));
                    indicator.setCollectIntervention(rs.getInt(8) != 0);
                    indicator.setCollectMonitoring(rs.getInt(9) != 0);

                    int activityId = rs.getInt(10);
                    ActivityDTO activity = activities.get(activityId);
                    activity.getIndicators().add(indicator);
                }
            });
        }

        public void loadAttributeGroups() {
            select("AttributeGroupId, Name, MultipleAllowed")
                    .from("AttributeGroup")
                    .orderBy("SortOrder")
                    .forEachResult(connection, new SqlQueryBuilder.ResultHandler() {

                @Override
                public void handle(ResultSet rs) throws SQLException {

                    AttributeGroupDTO group = new AttributeGroupDTO();
                    group.setId(rs.getInt(1));
                    group.setName(rs.getString(2));
                    group.setMultipleAllowed(rs.getInt(3) != 0);

                    attributeGroups.put(group.getId(), group);
                }
            });
        }

        public void loadAttributes() {
            select("AttributeId, Name, AttributeGroupId")
                .from("Attribute")
                .orderBy("SortOrder")
                .forEachResult(connection, new SqlQueryBuilder.ResultHandler() {

                @Override
                public void handle(ResultSet rs) throws SQLException {

                    AttributeDTO attribute = new AttributeDTO();
                    attribute.setId(rs.getInt(1));
                    attribute.setName(rs.getString(2));

                    int groupId = rs.getInt(3);
                    attributeGroups.get(groupId).getAttributes().add(attribute);
                }
            });
        }

        public void joinAttributesToActivities() {
            select("J.ActivityId, J.AttributeGroupId")
                .from("AttributeGroupInActivity J " +
                        "INNER JOIN AttributeGroup G ON (J.AttributeGroupId = G.AttributeGroupId)")
                .orderBy("G.SortOrder")
                .forEachResult(connection, new SqlQueryBuilder.ResultHandler() {
                    @Override
                    public void handle(ResultSet rs) throws SQLException {

                        int activityId = rs.getInt(1);
                        int groupId = rs.getInt(2);

                        activities.get(activityId).getAttributeGroups().add(
                                attributeGroups.get(groupId));
                    }
                });
        }

        public SchemaDTO build() {
            loadCountries();
            loadLocationTypes();
            loadAdminLevels();

            loadDatabases();

            loadPartners();
            joinPartnersToDatabases();

            loadActivities();
            loadIndicators();
            loadAttributeGroups();
            loadAttributes();
            joinAttributesToActivities();

            SchemaDTO schemaDTO = new SchemaDTO();
            schemaDTO.setCountries(new ArrayList<CountryDTO>(countries.values()));
            schemaDTO.setDatabases(new ArrayList<UserDatabaseDTO>(databaseMap.values()));
            return schemaDTO;
        }
    }
}
