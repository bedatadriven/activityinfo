package org.sigmah.shared.command.handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.sigmah.shared.command.GetSchema;
import org.sigmah.shared.dto.ActivityDTO;
import org.sigmah.shared.dto.AdminLevelDTO;
import org.sigmah.shared.dto.AnonymousUser;
import org.sigmah.shared.dto.AttributeDTO;
import org.sigmah.shared.dto.AttributeGroupDTO;
import org.sigmah.shared.dto.CountryDTO;
import org.sigmah.shared.dto.IndicatorDTO;
import org.sigmah.shared.dto.LocationTypeDTO;
import org.sigmah.shared.dto.LockedPeriodDTO;
import org.sigmah.shared.dto.PartnerDTO;
import org.sigmah.shared.dto.ProjectDTO;
import org.sigmah.shared.dto.SchemaDTO;
import org.sigmah.shared.dto.UserDatabaseDTO;
import org.sigmah.shared.util.mapping.BoundingBoxDTO;

import com.allen_sauer.gwt.log.client.Log;
import com.bedatadriven.rebar.sql.client.SqlResultCallback;
import com.bedatadriven.rebar.sql.client.SqlResultSet;
import com.bedatadriven.rebar.sql.client.SqlResultSetRow;
import com.bedatadriven.rebar.sql.client.SqlTransaction;
import com.bedatadriven.rebar.sql.client.query.SqlQuery;
import com.bedatadriven.rebar.sql.client.util.RowHandler;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class GetSchemaHandler implements
		CommandHandlerAsync<GetSchema, SchemaDTO> {


	@Override
	public void execute(GetSchema command, ExecutionContext context,
			AsyncCallback<SchemaDTO> callback) {

		new SchemaBuilder().build(context, callback);
	}

	private class SchemaBuilder {
		private final List<UserDatabaseDTO> databaseList = new ArrayList<UserDatabaseDTO>();
		private final List<CountryDTO> countryList = new ArrayList<CountryDTO>();
		
		private final Map<Integer, UserDatabaseDTO> databaseMap = new HashMap<Integer, UserDatabaseDTO>();

		private final Map<Integer, CountryDTO> countries = new HashMap<Integer, CountryDTO>();
		private final Map<Integer, PartnerDTO> partners = new HashMap<Integer, PartnerDTO>();
		private final Map<Integer, ActivityDTO> activities = new HashMap<Integer, ActivityDTO>();
		private final Map<Integer, AttributeGroupDTO> attributeGroups = new HashMap<Integer, AttributeGroupDTO>();
		private final Map<Integer, ProjectDTO> projects = new HashMap<Integer, ProjectDTO>();

		private SqlTransaction tx;
		private ExecutionContext context;

		public void loadCountries() {
			SqlQuery.select().appendColumn("CountryId", "id")
					.appendColumn("Name", "name").appendColumn("X1", "x1")
					.appendColumn("y1", "y1").appendColumn("x2", "x2")
					.appendColumn("y2", "y2").from("Country")
					.execute(tx, new RowHandler() {
						@Override
						public void handleRow(SqlResultSetRow rs) {
							CountryDTO country = new CountryDTO();
							country.setId(rs.getInt("id"));
							country.setName(rs.getString("name"));

							BoundingBoxDTO bounds = new BoundingBoxDTO(rs
									.getDouble("x1"), rs.getDouble("y1"), rs
									.getDouble("x2"), rs.getDouble("y2"));

							country.setBounds(bounds);

							countries.put(country.getId(), country);
							countryList.add(country);
						}
					});
		}

		public void loadLocationTypes() {
			SqlQuery.select("locationTypeId", "name", "boundAdminLevelId",
					"countryId").from("LocationType")
					.execute(tx, new RowHandler() {

						@Override
						public void handleRow(SqlResultSetRow row) {
							LocationTypeDTO type = new LocationTypeDTO();
							type.setId(row.getInt("locationTypeId"));
							type.setName(row.getString("name"));

							if (!row.isNull("boundAdminLevelId")) {
								type.setBoundAdminLevelId(row
										.getInt("boundAdminLevelId"));
							}

							int countryId = row.getInt("countryId");
							countries.get(countryId).getLocationTypes()
									.add(type);
						}
					});
		}

		public void loadAdminLevels() {
			SqlQuery.select("adminLevelId", "name", "parentId", "countryId")
					.from("AdminLevel").execute(tx, new RowHandler() {

						@Override
						public void handleRow(SqlResultSetRow row) {
							AdminLevelDTO level = new AdminLevelDTO();
							level.setId(row.getInt("adminLevelId"));
							level.setName(row.getString("name"));
							level.setCountryId(row.getInt("countryId"));

							if (!row.isNull("parentId")) {
								level.setParentLevelId(row.getInt("parentId"));
							}

							countries.get(level.getCountryId())
									.getAdminLevels().add(level);
						}
					});
		}

		public void loadDatabases() {
			SqlQuery query = SqlQuery
					.select("d.DatabaseId")
					.appendColumn("d.Name")
					.appendColumn("d.FullName")
					.appendColumn("d.OwnerUserId")
					.appendColumn("d.CountryId")
					.appendColumn("o.Name", "OwnerName")
					.appendColumn("o.Email", "OwnerEmail")
					.appendColumn("p.AllowViewAll", "allowViewAll")
					.appendColumn("p.AllowEdit", "allowEdit")
					.appendColumn("p.AllowEditAll", "allowEditAll")
					.appendColumn("p.AllowManageUsers", "allowManageUsers")
					.appendColumn("p.AllowManageAllUsers",
							"allowManageAllUsers")
					.appendColumn("p.AllowDesign", "allowDesign")
					.appendColumn("p.PartnerId", "partnerId")
					.from("UserDatabase d")
					.leftJoin(
							SqlQuery.selectAll()
									.from("UserPermission")
									.where("UserPermission.UserId")
										.equalTo(context.getUser().getId()), "p")
					.on("p.DatabaseId = d.DatabaseId").leftJoin("UserLogin o")
					.on("d.OwnerUserId = o.UserId").where("d.DateDeleted")
					.isNull().whereTrue("(o.userId = ? or p.AllowView = 1 or (d.DatabaseId in (select pa.DatabaseId from activity pa where pa.published>0)))")
					.appendParameter(context.getUser().getId())
					.orderBy("d.Name");

			query.execute(tx, new SqlResultCallback() {

				@Override
				public void onSuccess(SqlTransaction tx, SqlResultSet results) {

					for (SqlResultSetRow row : results.getRows()) {
						UserDatabaseDTO db = new UserDatabaseDTO();
						db.setId(row.getInt("DatabaseId"));
						db.setName(row.getString("Name"));
						db.setFullName(row.getString("FullName"));
						db.setAmOwner(row.getInt("OwnerUserId") == context
								.getUser().getId());
						db.setCountry(countries.get(row.getInt("CountryId")));
						db.setOwnerName(row.getString("OwnerName"));
						db.setOwnerEmail(row.getString("OwnerEmail"));

						if(context.getUser().getId() != AnonymousUser.USER_ID){
							
							db.setViewAllAllowed(db.getAmOwner()
									|| row.getBoolean("allowViewAll"));
							db.setEditAllowed(db.getAmOwner()
									|| row.getBoolean("allowEdit"));
							db.setEditAllAllowed(db.getAmOwner()
									|| row.getBoolean("allowEditAll"));
							db.setManageUsersAllowed(db.getAmOwner()
									|| row.getBoolean("allowManageUsers"));
							db.setManageAllUsersAllowed(db.getAmOwner()
									|| row.getBoolean("allowManageAllUsers"));
							db.setDesignAllowed(db.getAmOwner()
									|| row.getBoolean("allowDesign"));
							
							if(!db.getAmOwner()) {
								db.setMyPartnerId(row.getInt("partnerId"));
							}
						}else{
							db.setViewAllAllowed(db.getAmOwner()
									||false);
							db.setEditAllowed(db.getAmOwner()
									|| false);
							db.setEditAllAllowed(db.getAmOwner()
									||false);
							db.setManageUsersAllowed(db.getAmOwner()
									|| false);
							db.setManageAllUsersAllowed(db.getAmOwner()
									|| false);
							db.setDesignAllowed(db.getAmOwner()
									|| false);
							

						}

						databaseMap.put(db.getId(), db);
						databaseList.add(db);
					}

					if (!databaseMap.isEmpty()) {
						joinPartnersToDatabases();

						loadProjects();
						loadActivities();
						loadIndicators();
						loadAttributeGroups();
						loadAttributes();
						joinAttributesToActivities();
						loadLockedPeriods();
					}

				}
			});
		}

		protected void loadProjects() {
			SqlQuery.select("name", "projectId", "description", "databaseId")
					.from("Project")
					.where("databaseId").in(databaseMap.keySet())
					.execute(tx, new SqlResultCallback() {
						@Override
						public void onSuccess(SqlTransaction tx,
								SqlResultSet results) {
							for (SqlResultSetRow row : results.getRows()) {
								ProjectDTO project = new ProjectDTO();
								project.setName(row.getString("name"));
								project.setId(row.getInt("projectId"));
								project.setDescription(row
										.getString("description"));

								int databaseId = row.getInt("databaseId");
								UserDatabaseDTO database = databaseMap
										.get(databaseId);
								database.getProjects().add(project);
								project.setUserDatabase(database);
								projects.put(project.getId(), project);
							}
						}
					});
		}

		protected void loadLockedPeriods() {
			// TODO(ruud): load only what is visible to user 
			SqlQuery.select("fromDate", "toDate", "enabled", "name",
					"lockedPeriodId", "userDatabaseId", "activityId",
					"projectId").from("LockedPeriod")

			.execute(tx, new SqlResultCallback() {

				@Override
				public void onSuccess(SqlTransaction tx, SqlResultSet results) {
					for (SqlResultSetRow row : results.getRows()) {
						LockedPeriodDTO lockedPeriod = new LockedPeriodDTO();

						lockedPeriod.setFromDate(row.getDate("fromDate"));
						lockedPeriod.setToDate(row.getDate("toDate"));
						lockedPeriod.setEnabled(row.getBoolean("enabled"));
						lockedPeriod.setName(row.getString("name"));
						lockedPeriod.setId(row.getInt("lockedPeriodId"));

						boolean parentFound = false;

						if (!row.isNull("activityId")) {
							Integer activityId = row.getInt("activityId");
							ActivityDTO activity = activities.get(activityId);
							if (activity != null) { // activities can be
													// deleted...
								activity.getLockedPeriods().add(lockedPeriod);
								lockedPeriod.setParent(activity);
							}
							parentFound = true;
						}
						if (!row.isNull("userDatabaseId")) {
							Integer databaseId = row.getInt("userDatabaseId");
							UserDatabaseDTO database = databaseMap.get(databaseId);
							if (database != null) { // databases can be deleted
								database.getLockedPeriods().add(lockedPeriod);
								lockedPeriod.setParent(database);
							}
							parentFound = true;
						}
						if (!row.isNull("projectID")) {
							Integer projectId = row.getInt("projectId");
							ProjectDTO project = projects.get(projectId);
							project.getLockedPeriods().add(lockedPeriod);
							lockedPeriod.setParent(project);
							parentFound = true;
						}

						if (!parentFound) {
							Log.debug("Orphan lockedPeriod: No parent (UserDatabase/Activity/Project) found for LockedPeriod with Id="
									+ lockedPeriod.getId());
						}
					}
				}
			});
		}

		private void joinPartnersToDatabases() {
			SqlQuery query = SqlQuery
					.select("d.databaseId", "d.partnerId", "p.name", "p.fullName")
					.from("PartnerInDatabase", "d").leftJoin("Partner", "p")
					.on("d.PartnerId = p.PartnerId");

			if (!GWT.isClient()) {
				// on the server, limit the partners to those that are visible
				// to
				// the connected user
				query.where("d.databaseId").in(databaseMap.keySet());
			}

			query.execute(tx, new RowHandler() {

				@Override
				public void handleRow(SqlResultSetRow row) {

					int partnerId = row.getInt("partnerId");
					PartnerDTO partner = partners.get(partnerId);
					if (partner == null) {
						partner = new PartnerDTO();
						partner.setId(partnerId);
						partner.setName(row.getString("name"));
						partner.setFullName(row.getString("fullName"));
						partners.put(partnerId, partner);
					}

					UserDatabaseDTO db = databaseMap.get(row
							.getInt("databaseId"));
					if(db != null) { // databases can be deleted
						db.getPartners().add(partner);
					}
				}
			});

		}

		public void loadActivities() {
			SqlQuery query = SqlQuery
					.select("activityId", "name", "category", "locationTypeId",
							"reportingFrequency", "databaseId")
					.from("Activity").orderBy("SortOrder");

			if (!GWT.isClient()) {
				query.where("DateDeleted IS NULL");
				query.where("DatabaseId").in(databaseMap.keySet());
			}

			query.execute(tx, new RowHandler() {

				@Override
				public void handleRow(SqlResultSetRow row) {
					ActivityDTO activity = new ActivityDTO();
					activity.setId(row.getInt("activityId"));
					activity.setName(row.getString("name"));
					activity.setCategory(row.getString("category"));
					activity.setLocationTypeId(row.getInt("locationTypeId"));
					activity.setReportingFrequency(row
							.getInt("reportingFrequency"));

					int databaseId = row.getInt("databaseId");
					UserDatabaseDTO database = databaseMap.get(databaseId);
					activity.setDatabase(database);
					database.getActivities().add(activity);
					activities.put(activity.getId(), activity);
				}
			});
		}

		public void loadIndicators() {
			SqlQuery query = SqlQuery
					.select("indicatorId", "name", "category", "listHeader",
							"description", "aggregation", "units", "activityId")
					.from("Indicator").orderBy("SortOrder");

			if (!GWT.isClient()) {
				query.where("DateDeleted IS NULL");
				query.where("ActivityId").in(
						SqlQuery.select("ActivityId").from("Activity")
								.where("databaseId").in(databaseMap.keySet()));

			}

			query.execute(tx, new RowHandler() {

				@Override
				public void handleRow(SqlResultSetRow rs) {
					IndicatorDTO indicator = new IndicatorDTO();
					indicator.setId(rs.getInt("indicatorId"));
					indicator.setName(rs.getString("name"));
					indicator.setCategory(rs.getString("category"));
					indicator.setListHeader(rs.getString("listHeader"));
					indicator.setDescription(rs.getString("description"));
					indicator.setAggregation(rs.getInt("aggregation"));
					indicator.setUnits(rs.getString("units"));

					int activityId = rs.getInt("activityId");
					ActivityDTO activity = activities.get(activityId);
					if (activity != null) { // it may have been deleted
						activity.getIndicators().add(indicator);
					}
				}
			});
		}

		public void loadAttributeGroups() {
			SqlQuery query = SqlQuery.select()
					.appendColumn("AttributeGroupId", "id")
					.appendColumn("Name", "name")
					.appendColumn("multipleAllowed").from("AttributeGroup")
					.orderBy("SortOrder");

			if (!GWT.isClient()) {
				query.where("DateDeleted IS NULL");
				query.where("AttributeGroupId").in(
						SqlQuery.select("AttributeGroupId")
								.from("AttributeGroupInActivity")
								.where("ActivityId")
								.in(SqlQuery.select("ActivityId")
										.from("Activity").where("databaseId")
										.in(databaseMap.keySet())));

			}

			query.execute(tx, new RowHandler() {

				@Override
				public void handleRow(SqlResultSetRow rs) {

					AttributeGroupDTO group = new AttributeGroupDTO();
					group.setId(rs.getInt("id"));
					group.setName(rs.getString("name"));
					group.setMultipleAllowed(rs.getBoolean("multipleAllowed"));

					attributeGroups.put(group.getId(), group);
				}
			});
		}

		public void loadAttributes() {
			SqlQuery query = SqlQuery
					.select("attributeId", "name", "attributeGroupId")
					.from("Attribute").orderBy("SortOrder");

			if (!GWT.isClient()) {
				query.where("DateDeleted IS NULL");
				query.where("AttributeGroupId").in(
						SqlQuery.select("AttributeGroupId")
								.from("AttributeGroupInActivity")
								.where("ActivityId")
								.in(SqlQuery.select("ActivityId")
										.from("Activity").where("databaseId")
										.in(databaseMap.keySet())));

			}

			query.execute(tx, new RowHandler() {

				@Override
				public void handleRow(SqlResultSetRow row) {

					AttributeDTO attribute = new AttributeDTO();
					attribute.setId(row.getInt("attributeId"));
					attribute.setName(row.getString("name"));

					int groupId = row.getInt("attributeGroupId");
					AttributeGroupDTO group = attributeGroups.get(groupId);
					if (group != null) {
						group.getAttributes().add(attribute);
					}
				}
			});
		}

		public void joinAttributesToActivities() {
			SqlQuery query = SqlQuery
					.select("J.activityId", "J.attributeGroupId")
					.from("AttributeGroupInActivity J "
							+ "INNER JOIN AttributeGroup G ON (J.attributeGroupId = G.attributeGroupId)")
					.orderBy("G.SortOrder")
					.where("G.dateDeleted").isNull();

			if (!GWT.isClient()) {
				query.where("ActivityId").in(
						SqlQuery.select("ActivityId").from("Activity")
								.where("databaseId").in(databaseMap.keySet()));

			}

			query.execute(tx, new RowHandler() {
				@Override
				public void handleRow(SqlResultSetRow row) {

					int groupId = row.getInt("attributeGroupId");

					ActivityDTO activity = activities.get(row
							.getInt("activityId"));
					if (activity != null) { // it may have been deleted
						activity.getAttributeGroups().add(
								attributeGroups.get(groupId));
					}
				}
			});
		}


		public void build(ExecutionContext context,
				final AsyncCallback<SchemaDTO> callback) {
			this.context = context;
			this.tx = context.getTransaction();
	
			loadCountries();
			loadLocationTypes();
			loadAdminLevels();

			loadDatabases();
			
			SchemaDTO schemaDTO = new SchemaDTO();
			schemaDTO.setCountries(countryList);
			schemaDTO.setDatabases(databaseList);

			callback.onSuccess(schemaDTO);
		}
	}
}
