package org.activityinfo.shared.command.handler;

/*
 * #%L
 * ActivityInfo Server
 * %%
 * Copyright (C) 2009 - 2013 UNICEF
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.activityinfo.shared.command.GetTargets;
import org.activityinfo.shared.command.result.TargetResult;
import org.activityinfo.shared.db.Tables;
import org.activityinfo.shared.dto.PartnerDTO;
import org.activityinfo.shared.dto.ProjectDTO;
import org.activityinfo.shared.dto.TargetDTO;
import org.activityinfo.shared.dto.TargetValueDTO;
import org.activityinfo.shared.dto.UserDatabaseDTO;

import com.bedatadriven.rebar.sql.client.SqlResultCallback;
import com.bedatadriven.rebar.sql.client.SqlResultSet;
import com.bedatadriven.rebar.sql.client.SqlResultSetRow;
import com.bedatadriven.rebar.sql.client.SqlTransaction;
import com.bedatadriven.rebar.sql.client.query.SqlQuery;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class GetTargetsHandler implements
		CommandHandlerAsync<GetTargets, TargetResult> {

	private final Map<Integer, List<TargetValueDTO>> targetValues = Maps.newHashMap();
	private final List<TargetDTO> targets = Lists.newArrayList();

	@Override
	public void execute(GetTargets command, ExecutionContext context,
			AsyncCallback<TargetResult> callback) {

		loadTargetValues(context, command.getDatabaseId());
		loadTargets(context, command.getDatabaseId());
		
		callback.onSuccess(new TargetResult(targets));
	}

	protected void loadTargets(ExecutionContext context, int databaseId) {
		SqlQuery.select("t.name", "t.targetId", "t.Date1", "t.Date2",
				"t.ProjectId", "t.PartnerId", "t.AdminEntityId", "t.DatabaseId")
				.appendColumn("a.name", "area")
				.appendColumn("pr.name", "projectName")
				.appendColumn("pt.name", "partnerName")
				.from(Tables.TARGET, "t")
				.leftJoin("adminentity", "a")
				.on("t.AdminEntityId = a.AdminEntityId")
				.leftJoin("project", "pr").on("t.ProjectId = pr.ProjectId")
				.leftJoin("partner", "pt").on("t.PartnerId = pt.PartnerId")
				.where("t.DatabaseId").equalTo(databaseId)
				.execute(context.getTransaction(), new SqlResultCallback() {
					@Override
					public void onSuccess(SqlTransaction tx,
							SqlResultSet results) {

						for (SqlResultSetRow row : results.getRows()) {
							TargetDTO target = new TargetDTO();
							target.setName(row.getString("name"));
							target.setId(row.getInt("targetId"));
							target.setDate1(row.getDate("Date1"));
							target.setDate2(row.getDate("Date2"));

							if(!row.isNull("PartnerId")){
								PartnerDTO partner = new PartnerDTO();
								partner.setId(row.getInt("PartnerId"));
								partner.setName(row.getString("partnerName"));
								target.setPartner(partner);
							}
							
							if(!row.isNull("ProjectId")){
								ProjectDTO project = new ProjectDTO();
								project.setId(row.getInt("ProjectId"));
								project.setName(row.getString("projectName"));
								target.setProject(project);
							}
							
							target.setArea(row.getString("area"));

							UserDatabaseDTO database = new UserDatabaseDTO();
							database.setId(row.getInt("DatabaseId"));
							target.setUserDatabase(database);

							target.setTargetValues(targetValues.get(target.getId()));
							targets.add(target);
						}
					}
				});
	}
	
	protected void loadTargetValues(ExecutionContext context, int databaseId){

		SqlQuery.select("v.targetId", "v.indicatorId","v.value").appendColumn("t.name").appendColumn("i.name", "iname")
		.from(Tables.TARGET_VALUE, "v")
		.leftJoin(Tables.TARGET, "t").on("v.targetId = t.targetId")
		.leftJoin(Tables.INDICATOR, "i").on("v.indicatorId = i.indicatorId")
		.execute(context.getTransaction(), new SqlResultCallback() {
			@Override
			public void onSuccess(SqlTransaction tx,
					SqlResultSet results) {
				
				for (SqlResultSetRow row : results.getRows()) {
					TargetValueDTO dto = new TargetValueDTO();
					dto.setValue(row.getDouble("value"));
					dto.setTargetId(row.getInt("targetId"));
					dto.setIndicatorId(row.getInt("indicatorId"));
					dto.setName(row.getString("iname"));
					
					List<TargetValueDTO> list = targetValues.get(dto.getTargetId());
					
					if(targetValues.get(dto.getTargetId()) == null){
						list = new ArrayList<TargetValueDTO>();													
					}
					
					list.add(dto);
					targetValues.put(dto.getTargetId(), list);	
				}
			}
		});
	}
}
