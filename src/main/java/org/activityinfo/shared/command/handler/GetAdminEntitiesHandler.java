package org.activityinfo.shared.command.handler;

import java.util.ArrayList;
import java.util.List;

import org.activityinfo.shared.command.GetAdminEntities;
import org.activityinfo.shared.command.result.AdminEntityResult;
import org.activityinfo.shared.db.Tables;
import org.activityinfo.shared.dto.AdminEntityDTO;
import org.activityinfo.shared.report.model.DimensionType;
import org.activityinfo.shared.util.CollectionUtil;
import org.activityinfo.shared.util.mapping.Extents;

import com.bedatadriven.rebar.sql.client.SqlResultCallback;
import com.bedatadriven.rebar.sql.client.SqlResultSet;
import com.bedatadriven.rebar.sql.client.SqlResultSetRow;
import com.bedatadriven.rebar.sql.client.SqlTransaction;
import com.bedatadriven.rebar.sql.client.query.SqlQuery;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class GetAdminEntitiesHandler implements CommandHandlerAsync<GetAdminEntities, AdminEntityResult> {


	@Override
	public void execute(GetAdminEntities cmd, ExecutionContext context,
			final AsyncCallback<AdminEntityResult> callback) {
		
		SqlQuery query =
				SqlQuery.select("AdminEntity.adminEntityId",
					"AdminEntity.name",
					"AdminEntity.adminLevelId",
					"AdminEntity.adminEntityParentId",
					"x1","y1","x2","y2")
					.from(Tables.ADMIN_ENTITY, "AdminEntity");

		if (CollectionUtil.isNotEmpty(cmd.getCountryIds())) {
			query.leftJoin(Tables.ADMIN_LEVEL, "AdminLevel").on("AdminLevel.AdminLevelId=AdminEntity.AdminLevelId");
			query.where("AdminLevel.CountryId").in(cmd.getCountryIds());

			if(cmd.getParentId() == null && cmd.getLevelId() == null) {
				query.where("AdminLevel.ParentId is null");
			}
			
			query.orderBy("AdminLevel.CountryId");
		}
		
		query.orderBy("AdminEntity.name");
		

		if(cmd.getLevelId() != null) {
			query.where("AdminEntity.AdminLevelId").equalTo(cmd.getLevelId());
		}

		if(cmd.getParentId() != null) {
			query.where("AdminEntity.AdminEntityParentId").equalTo(cmd.getParentId());
		}

		if(cmd.getFilter() != null && cmd.getFilter().isRestricted(DimensionType.Activity)) {
			SqlQuery subQuery = SqlQuery.select("link.AdminEntityId")
					.from(Tables.LOCATION_ADMIN_LINK, "link")
					.leftJoin(Tables.LOCATION, "Location").on("link.LocationId = Location.LocationId")
					.leftJoin(Tables.SITE, "Site").on("Location.LocationId = Site.LocationId")
					.where("Site.ActivityId").in(cmd.getFilter().getRestrictions(DimensionType.Activity));

			query.where("AdminEntity.AdminEntityId").in(subQuery);
		}
		
		if (cmd.getFilter() != null && cmd.getFilter().isRestricted(DimensionType.AdminLevel)) {
			if(cmd.getLevelId() == null) {
				query.where("AdminEntityId").in(cmd.getFilter().getRestrictions(DimensionType.AdminLevel));
			} else {
				SqlQuery subQuery = SqlQuery.select("adminEntityId")
					.from(Tables.ADMIN_ENTITY, "AdminEntity")
					.where("AdminLevelId").equalTo(cmd.getLevelId())
					.where("AdminEntityId").in(cmd.getFilter().getRestrictions(DimensionType.AdminLevel));
				query.where("AdminEntity.AdminEntityId").in(subQuery);
			}
		}
		query.execute(context.getTransaction(), new SqlResultCallback() {

			@Override
			public void onSuccess(SqlTransaction tx, SqlResultSet results) {
				final List<AdminEntityDTO> entities = new ArrayList<AdminEntityDTO>();
				for(SqlResultSetRow row : results.getRows()) {
					entities.add(toEntity(row));
				}
				callback.onSuccess(new AdminEntityResult(entities));
			}
		});
	}

	public static AdminEntityDTO toEntity(SqlResultSetRow row) {
		
		AdminEntityDTO entity = new AdminEntityDTO();
		entity.setId(row.getInt("adminEntityId"));
		entity.setName(row.getString("name"));
		entity.setLevelId(row.getInt("adminLevelId"));
		if(!row.isNull("adminEntityParentId")) {
			entity.setParentId(row.getInt("adminEntityParentId"));
		}
		Extents bounds = Extents.empty();
		if(!row.isNull("x1")) {
			bounds.setMinLon(row.getDouble("x1"));
			bounds.setMinLat(row.getDouble("y1"));
			bounds.setMaxLon(row.getDouble("x2"));
			bounds.setMaxLat(row.getDouble("y2"));
			entity.setBounds(bounds);
		}
		return entity;
	}
}
