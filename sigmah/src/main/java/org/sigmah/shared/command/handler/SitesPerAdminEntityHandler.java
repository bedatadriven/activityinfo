package org.sigmah.shared.command.handler;

import java.util.Map;

import org.sigmah.shared.command.GetAdminEntities;
import org.sigmah.shared.command.SitesPerAdminEntity;
import org.sigmah.shared.command.SitesPerAdminEntity.SitesPerAdminEntityResult;
import org.sigmah.shared.command.SitesPerAdminEntity.SitesPerAdminEntityResult.AmountPerAdminEntity;
import org.sigmah.shared.command.result.AdminEntityResult;
import org.sigmah.shared.dao.Filter;
import org.sigmah.shared.dto.AdminEntityDTO;
import org.sigmah.shared.report.model.DimensionType;

import com.bedatadriven.rebar.sql.client.SqlResultCallback;
import com.bedatadriven.rebar.sql.client.SqlResultSet;
import com.bedatadriven.rebar.sql.client.SqlResultSetRow;
import com.bedatadriven.rebar.sql.client.SqlTransaction;
import com.google.common.collect.Maps;
import com.google.gwt.user.client.rpc.AsyncCallback;

/** 1. Find all adminentities for given activity with amount of sites
 *  2. Grab all adminEntities for
 *  3. Return adminentities with respective sitecount indexed by AdminEntityId  */
public class SitesPerAdminEntityHandler implements CommandHandlerAsync<SitesPerAdminEntity, SitesPerAdminEntityResult>{
	
	public SitesPerAdminEntityHandler() {
	}

	@Override
	public void execute(SitesPerAdminEntity command, final ExecutionContext context, final AsyncCallback<SitesPerAdminEntityResult> callback) {
		StringBuilder sb = new StringBuilder()
			.append("select count(*) as c, adminentity.adminentityid as id from site")
			.append(" left join location on site.locationid = location.locationid")
			.append(" left join locationadminlink on site.locationid = locationadminlink.locationid")
			.append(" left join adminentity on locationadminlink.adminentityid = adminentity.adminentityid")
			.append(" where site.activityid="+ command.getActivityId() +" group by adminentity.adminentityId");
		
		context.getTransaction().executeSql(sb.toString(), new SqlResultCallback() {
			
			@Override
			public void onSuccess(SqlTransaction tx, SqlResultSet results) {
				// Grab adminentities' id + sitecount per AdminEntity
				SitesPerAdminEntityResult result = new SitesPerAdminEntityResult();
				final Map<Integer, Integer> adminEntities = Maps.newHashMap();
				for (SqlResultSetRow row : results.getRows()) {
					if (!row.isNull("c") && !row.isNull("id")) {
						int amount = row.getInt("c");
						int id = row.getInt("id");
						adminEntities.put(id, amount);
					}
				}
				
				// Get concrete AdminEntities
				GetAdminEntities getAdminEntities = new GetAdminEntities();
				Filter filter = new Filter();
				filter.addRestriction(DimensionType.AdminLevel, adminEntities.keySet());
				getAdminEntities.setFilter(filter);
				context.execute(getAdminEntities, new AsyncCallback<AdminEntityResult>() {
					@Override
					public void onFailure(Throwable caught) {
						callback.onFailure(caught);
					}

					@Override
					public void onSuccess(AdminEntityResult result) {
						Map<Integer, AmountPerAdminEntity> adminEntitiesWithAmount = Maps.newHashMap();
						for (AdminEntityDTO adminEntity : result.getData()) {
							Integer amount = adminEntities.get(adminEntity.getId());
							if (amount != null) {
								adminEntitiesWithAmount.put(adminEntity.getId(), new AmountPerAdminEntity(amount, adminEntity));
							}
						}
						callback.onSuccess(new SitesPerAdminEntityResult(adminEntitiesWithAmount));
					}
				});
				
			}
		});
	}
}
