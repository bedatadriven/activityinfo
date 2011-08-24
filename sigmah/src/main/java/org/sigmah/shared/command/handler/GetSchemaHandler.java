package org.sigmah.shared.command.handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.sigmah.shared.command.GetSchema;
import org.sigmah.shared.dto.ActivityDTO;
import org.sigmah.shared.dto.AdminLevelDTO;
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
import com.bedatadriven.rebar.sql.client.SqlDatabase;
import com.bedatadriven.rebar.sql.client.SqlException;
import com.bedatadriven.rebar.sql.client.SqlResultCallback;
import com.bedatadriven.rebar.sql.client.SqlResultSet;
import com.bedatadriven.rebar.sql.client.SqlResultSetRow;
import com.bedatadriven.rebar.sql.client.SqlTransaction;
import com.bedatadriven.rebar.sql.client.SqlTransactionCallback;
import com.bedatadriven.rebar.sql.client.query.SqlQuery;
import com.bedatadriven.rebar.sql.client.util.RowHandler;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;


public class GetSchemaHandler implements CommandHandlerAsync<GetSchema, SchemaDTO> {

    private SqlDatabase database;
  
    @Inject
    public GetSchemaHandler(SqlDatabase database) {
		super();
		this.database = database;
	}

	@Override
	public void execute(GetSchema command, CommandContext context,
			AsyncCallback<SchemaDTO> callback) {

		new SchemaBuilder().build(context, callback);
	}
    

    private class SchemaBuilder {
        final List<UserDatabaseDTO> databaseList = new ArrayList<UserDatabaseDTO>();
        final Map<Integer, UserDatabaseDTO> databaseMap = new HashMap<Integer, UserDatabaseDTO>();

        final Map<Integer, CountryDTO> countries = new HashMap<Integer, CountryDTO>();
        final Map<Integer, PartnerDTO> partners = new HashMap<Integer, PartnerDTO>();
        final Map<Integer, ActivityDTO> activities = new HashMap<Integer, ActivityDTO>();
        final Map<Integer, AttributeGroupDTO> attributeGroups = new HashMap<Integer, AttributeGroupDTO>();
        final Map<Integer, ProjectDTO> projects = new HashMap<Integer, ProjectDTO>();

        SqlTransaction tx;
        CommandContext context;
       

		public void loadCountries() {
            SqlQuery.select()
            	.appendColumn("CountryId", "id")
            	.appendColumn("Name", "name")
            	.appendColumn("X1", "x1")
            	.appendColumn("y1", "y1")
            	.appendColumn("x2", "x2")
            	.appendColumn("y2", "y2")
            	.from("Country")
            	.execute(tx, new RowHandler() {

		                @Override
                public void handleRow(SqlResultSetRow rs) {
                    CountryDTO country = new CountryDTO();
                    country.setId(rs.getInt("id"));
                    country.setName(rs.getString("name"));

                    BoundingBoxDTO bounds = new BoundingBoxDTO(
                    		rs.getDouble("x1"),
                    		rs.getDouble("y1"),
                    		rs.getDouble("x2"),
                    		rs.getDouble("y2"));

                    country.setBounds(bounds);

                    countries.put(country.getId(), country);
                }
            });
        }

        public void loadLocationTypes() {
            SqlQuery.select("LocationTypeId, Name, BoundAdminLevelId, CountryId")
                .from("LocationType")
                .execute(tx, new RowHandler() {

                @Override
                public void handleRow(SqlResultSetRow row) {
                    LocationTypeDTO type = new LocationTypeDTO();
                    type.setId(row.getInt("LocationTypeId"));
                    type.setName(row.getString("Name"));

                    if(!row.isNull("BoundAdminLevelId")) {
                    	type.setBoundAdminLevelId(row.getInt("BoundAdminLevelId"));
                    }

                    int countryId = row.getInt("CountryId");
                    countries.get(countryId).getLocationTypes().add(type);
                }
            });
        }

        public void loadAdminLevels() {
            SqlQuery.select("AdminLevelId, Name, ParentId, CountryId")
                .from("AdminLevel")
                .execute(tx, new RowHandler() {
					
					@Override
					public void handleRow(SqlResultSetRow row) {
						AdminLevelDTO level = new AdminLevelDTO();
						level.setId(row.getInt("AdminLevelId"));
						level.setName(row.getString("Name"));
						level.setCountryId(row.getInt("CountryId"));

						if(!row.isNull("ParentId")) {
							level.setParentLevelId(row.getInt("ParentId"));
						}
						
						countries.get(level.getCountryId()).getAdminLevels().add(level);
					}
				});
        }

        public void loadDatabases() {
            SqlQuery query = SqlQuery.select("d.DatabaseId")
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
            	.appendColumn("p.AllowManageAllUsers", "allowManageAllUsers")
            	.appendColumn("p.AllowDesign", "allowDesign")
            .from("UserDatabase d")
            .leftJoin( 
            		SqlQuery.select("*").from("UserPermission")
            			.where("UserPermission.UserId").equalTo(context.getUser().getId()), "p")
            			.on("p.DatabaseId = d.DatabaseId")
            .leftJoin("UserLogin o").on("d.OwnerUserId = o.UserId")
            .where("d.DateDeleted").isNull()
            .whereTrue("(o.userId = ? or p.AllowView = 1)")
            .appendParameter(context.getUser().getId())
            .orderBy("d.Name");
            
            
            
            query.execute(tx, new SqlResultCallback() {
				
				@Override
				public void onSuccess(SqlTransaction tx, SqlResultSet results) {
			
				    for(SqlResultSetRow row : results.getRows()) {
					    UserDatabaseDTO db = new UserDatabaseDTO();
	                    db.setId( row.getInt("DatabaseId") );
	                    db.setName( row.getString("Name") );
	                    db.setFullName( row.getString("FullName") );
	                    db.setAmOwner( row.getInt("OwnerUserId") == context.getUser().getId() );
	                    db.setCountry( countries.get( row.getInt("CountryId") ));
	                    db.setOwnerName( row.getString("OwnerName") );
	                    db.setOwnerEmail( row.getString("OwnerEmail") );
	
	                    db.setViewAllAllowed(db.getAmOwner() || row.getBoolean("allowViewAll") );
	                    db.setEditAllowed(db.getAmOwner() || row.getBoolean("allowEdit") );
	                    db.setEditAllAllowed(db.getAmOwner() || row.getBoolean("allowEditAll") );
	                    db.setManageUsersAllowed(db.getAmOwner() || row.getBoolean("allowManageUsers" ) );
	                    db.setManageAllUsersAllowed(db.getAmOwner() || row.getBoolean("allowManageAllUsers") );
	                    db.setDesignAllowed(db.getAmOwner() || row.getBoolean("allowDesign") );
	
	                    databaseMap.put(db.getId(), db);
	                    databaseList.add(db);				
					}
					
					if(!databaseMap.isEmpty()) {
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
        	SqlQuery.select("Name, ProjectId, Description, DatabaseId")
        			.from("Project")
        	
        			.execute(tx, new SqlResultCallback() {
						@Override
						public void onSuccess(SqlTransaction tx, SqlResultSet results) {
							for (SqlResultSetRow row : results.getRows()) {
								ProjectDTO project = new ProjectDTO();
								project.setName(row.getString("Name"));
								project.setId(row.getInt("ProjectId"));
								project.setDescription(row.getString("Description"));
								
								int databaseId =  row.getInt("DatabaseId");
								UserDatabaseDTO database = databaseMap.get(databaseId);
								database.getProjects().add(project);
								project.setUserDatabase(database);
								projects.put(project.getId(), project);
							}
						}
					});
		}

		protected void loadLockedPeriods() {
            SqlQuery.select("FromDate, ToDate, Enabled, Name, LockedPeriodId, UserDatabaseId, ActivityId, ProjectId")
					.from("LockedPeriod")
					
					.execute(tx, new SqlResultCallback() {
						
						@Override
						public void onSuccess(SqlTransaction tx, SqlResultSet results) {
							for (SqlResultSetRow row : results.getRows()) {
								LockedPeriodDTO lockedPeriod = new LockedPeriodDTO();
								
								lockedPeriod.setFromDate(row.getDate("FromDate"));
								lockedPeriod.setToDate(row.getDate("ToDate"));
								lockedPeriod.setEnabled(row.getBoolean("Enabled"));
								lockedPeriod.setName(row.getString("Name"));
								lockedPeriod.setId(row.getInt("LockedPeriodId"));
								
								boolean parentFound = false;
								
								if (!row.isNull("ActivityId")) {
									Integer activityId = row.getInt("ActivityId");
									ActivityDTO activity = activities.get(activityId);
									activity.getLockedPeriods().add(lockedPeriod);
									lockedPeriod.setActivity(activity);
									parentFound=true;
								}
								if (!row.isNull("UserDatabaseId")) { 
									Integer databaseId = row.getInt("UserDatabaseId");
									UserDatabaseDTO database = databaseMap.get(databaseId);
									database.getLockedPeriods().add(lockedPeriod);
									lockedPeriod.setUserDatabase(database);
									parentFound=true;
								}
								if (!row.isNull("ProjectID")) {
									Integer projectId = row.getInt("ProjectId");
									ProjectDTO project = projects.get(projectId);
									project.getLockedPeriods().add(lockedPeriod);
									lockedPeriod.setProject(project);
									parentFound=true;
								}
								
								if (!parentFound) {
									Log.debug("Zombie lockedPeriod: No parent (UserDatabase/Activity/Project) found for LockedPeriod with Id=" + lockedPeriod.getId()); 
								}
							}
						}

						@Override
						public boolean onFailure(SqlException e) {
							return super.onFailure(e);
						}
					});
		}
        
		private void joinPartnersToDatabases() {
            SqlQuery query = SqlQuery.select("d.DatabaseId","d.PartnerId", "p.Name")
                .from("PartnerInDatabase", "d")
                .leftJoin("Partner", "p").on("d.PartnerId = p.PartnerId");
            
            if(!GWT.isClient()) {
            	// on the server, limit the partners to those that are visible to
            	// the connected user
            	query.where("d.DatabaseId").in(databaseMap.values());
            }
            
            query.execute(tx, new RowHandler() {
					
					@Override
					public void handleRow(SqlResultSetRow row) {
	                    
	                    int partnerId = row.getInt("PartnerId");
	                    PartnerDTO partner = partners.get(partnerId);
	                    if(partner == null) {
	                    	partner = new PartnerDTO();
	                    	partner.setId(partnerId);
	                    	partner.setName(row.getString("Name"));
	                    	partners.put(partnerId, partner);
	                    }

	                    UserDatabaseDTO db = databaseMap.get( row.getInt("DatabaseId") );
	                    db.getPartners().add(partner);
					}
				});
    
        }
        
        public void loadActivities() {
            SqlQuery query = SqlQuery.select("ActivityId, Name, Category, LocationTypeId, ReportingFrequency, DatabaseId")
                .from("Activity")
                .orderBy("SortOrder");
            
            if(!GWT.isClient()) {
            	query.where("DateDeleted IS NULL");
            	query.where("DatabaseId").in(databaseMap.keySet());
            }
            
            query.execute(tx, new RowHandler() {


				@Override
				public void handleRow(SqlResultSetRow row) {
                    ActivityDTO activity = new ActivityDTO();
                    activity.setId( row.getInt("ActivityId") );
                    activity.setName( row.getString("Name") );
                    activity.setCategory( row.getString("Category") );
                    activity.setLocationTypeId( row.getInt("LocationTypeId") );
                    activity.setReportingFrequency( row.getInt("ReportingFrequency") );

                    int databaseId = row.getInt("DatabaseId");
                    UserDatabaseDTO database = databaseMap.get(databaseId);
                    activity.setDatabase(database);
                    database.getActivities().add(activity);
                    activities.put(activity.getId(), activity);
				}
            });
        }

		public void loadIndicators() {
            SqlQuery query = SqlQuery.select("IndicatorId, Name, Category, ListHeader, Description, Aggregation",
                     "Units, CollectIntervention, CollectMonitoring, ActivityId")
                    .from("Indicator")
                    .orderBy("SortOrder");
            
            if(!GWT.isClient()) {
            	query.where("DateDeleted IS NULL");
            	query.where("ActivityId").in(SqlQuery.select("ActivityId").from("Activity").where("databaseId")
            			.in(databaseMap.keySet()));
            			
            }
            
            query.execute(tx, new RowHandler() {

                @Override
                public void handleRow(SqlResultSetRow rs) {
                    IndicatorDTO indicator = new IndicatorDTO();
                    indicator.setId(rs.getInt("IndicatorId"));
                    indicator.setName(rs.getString("Name"));
                    indicator.setCategory(rs.getString("Category"));
                    indicator.setListHeader(rs.getString("ListHeader"));
                    indicator.setDescription(rs.getString("Description"));
                    indicator.setAggregation(rs.getInt("Aggregation"));
                    indicator.setUnits(rs.getString("Units"));

                    int activityId = rs.getInt("ActivityId");
                    ActivityDTO activity = activities.get(activityId);
                    if(activity != null) { // it may have been deleted
                    	activity.getIndicators().add(indicator);
                    }
                }
            });
        }

        public void loadAttributeGroups() {
            SqlQuery query = SqlQuery.select()
            		.appendColumn("AttributeGroupId", "id")
            		.appendColumn("Name", "name")
            		.appendColumn("MultipleAllowed", "multipleAllowed")
                    .from("AttributeGroup")
                    .orderBy("SortOrder");
            
            if(!GWT.isClient()) {
            	query.where("DateDeleted IS NULL");
            	query.where("AttributeGroupId").in(
            			SqlQuery.select("AttributeGroupId").from("AttributeGroupInActivity")
            				.where("ActivityId").in(SqlQuery.select("ActivityId").from("Activity")
            						.where("databaseId").in(databaseMap.keySet())));
            			
            }
            
            query.execute(tx, new RowHandler() {

                @Override
                public void handleRow(SqlResultSetRow rs)  {

                    AttributeGroupDTO group = new AttributeGroupDTO();
                    group.setId(rs.getInt("id"));
                    group.setName(rs.getString("name"));
                    group.setMultipleAllowed(rs.getBoolean("multipleAllowed"));

                    attributeGroups.put(group.getId(), group);
                }
            });
        }

        public void loadAttributes() {
            SqlQuery query = SqlQuery.select("AttributeId, Name, AttributeGroupId")
                .from("Attribute")
                .orderBy("SortOrder");
            
            if(!GWT.isClient()) {
            	query.where("DateDeleted IS NULL");
            	query.where("AttributeGroupId").in(
            			SqlQuery.select("AttributeGroupId").from("AttributeGroupInActivity")
            				.where("ActivityId").in(SqlQuery.select("ActivityId").from("Activity")
            						.where("databaseId").in(databaseMap.keySet())));
            			
            }
            
            query.execute(tx, new RowHandler() {

                @Override
                public void handleRow(SqlResultSetRow row) {

                    AttributeDTO attribute = new AttributeDTO();
                    attribute.setId(row.getInt("AttributeId"));
                    attribute.setName(row.getString("Name"));


                    int groupId = row.getInt("AttributeGroupId");
                    AttributeGroupDTO group = attributeGroups.get(groupId);
                    if (group != null) {
                    	group.getAttributes().add(attribute);
                    }
                }
            });
        }

        public void joinAttributesToActivities() {
            SqlQuery query = SqlQuery.select("J.ActivityId, J.AttributeGroupId")
                .from("AttributeGroupInActivity J " +
                        "INNER JOIN AttributeGroup G ON (J.AttributeGroupId = G.AttributeGroupId)")
                .orderBy("G.SortOrder");
            
            if(!GWT.isClient()) {
            	query.where("ActivityId").in(SqlQuery.select("ActivityId").from("Activity").where("databaseId")
            			.in(databaseMap.keySet()));
            			
            }
            
            query.execute(tx, new RowHandler() {
                    @Override
                    public void handleRow(SqlResultSetRow row) {

                        int groupId = row.getInt("AttributeGroupId");
                        
                        ActivityDTO activity = activities.get(row.getInt("ActivityId"));
                        if(activity != null) { // it may have been deleted
                        	activity.getAttributeGroups().add(attributeGroups.get(groupId));
                        }
                    }
                });
        }

        public void build(CommandContext context, final AsyncCallback<SchemaDTO> callback) {
        	this.context = context;
        	database.transaction(new SqlTransactionCallback() {
				
				@Override
				public void begin(SqlTransaction tx) {
					SchemaBuilder.this.tx = tx;
				    loadCountries();
		            loadLocationTypes();
		            loadAdminLevels();

		            loadDatabases();
			
				}

				@Override
				public void onError(SqlException e) {
					callback.onFailure(e);
				}

				@Override
				public void onSuccess() {
				    SchemaDTO schemaDTO = new SchemaDTO();
		            schemaDTO.setCountries(new ArrayList<CountryDTO>(countries.values()));
		            schemaDTO.setDatabases(databaseList);
		            
		            callback.onSuccess(schemaDTO);
				}
			});
        }
    }
}

