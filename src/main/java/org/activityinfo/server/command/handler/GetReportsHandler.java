

package org.activityinfo.server.command.handler;

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

import java.util.List;

import org.activityinfo.shared.command.GetReports;
import org.activityinfo.shared.command.handler.CommandHandlerAsync;
import org.activityinfo.shared.command.handler.ExecutionContext;
import org.activityinfo.shared.command.result.ReportsResult;
import org.activityinfo.shared.db.Tables;
import org.activityinfo.shared.dto.ReportMetadataDTO;
import org.activityinfo.shared.report.model.EmailDelivery;

import com.bedatadriven.rebar.sql.client.SqlResultCallback;
import com.bedatadriven.rebar.sql.client.SqlResultSet;
import com.bedatadriven.rebar.sql.client.SqlResultSetRow;
import com.bedatadriven.rebar.sql.client.SqlTransaction;
import com.bedatadriven.rebar.sql.client.query.SqlQuery;
import com.google.common.collect.Lists;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * @author Alex Bertram
 * @see org.activityinfo.shared.command.GetReports
 */
public class GetReportsHandler implements CommandHandlerAsync<GetReports, ReportsResult> {


	@Override
	public void execute(final GetReports command, final ExecutionContext context,
			final AsyncCallback<ReportsResult> callback) {
    	// note that we are excluding reports with a null title-- these
    	// reports have not yet been explicitly saved by the user
    	
		SqlQuery mySubscriptions = 	
			SqlQuery.selectAll()
				.from(Tables.REPORT_SUBSCRIPTION)
				.where("userId").equalTo(context.getUser().getUserId());
		
		SqlQuery myDatabases = 
			SqlQuery.selectSingle("d.databaseid")
			.from(Tables.USER_DATABASE, "d")
			.leftJoin(
				SqlQuery.selectAll()
					.from(Tables.USER_PERMISSION)
					.where("userpermission.UserId")
						.equalTo(context.getUser().getId()), "p")
				.on("p.DatabaseId = d.DatabaseId")
				.where("d.ownerUserId").equalTo(context.getUser().getUserId())
					.or("p.AllowView").equalTo(1);
		
    	SqlQuery.select()
    		.appendColumn("r.reportTemplateId", "reportId")
    		.appendColumn("r.title", "title")
    		.appendColumn("r.ownerUserId", "ownerUserId")
    		.appendColumn("o.name", "ownerName")
    		.appendColumn("s.dashboard", "dashboard")
    		.appendColumn("s.emaildelivery", "emaildelivery")
    		.appendColumn("s.emailday", "emailday")
    		.appendColumn(
    				SqlQuery.selectSingle("max(defaultDashboard)")
    					.from(Tables.REPORT_VISIBILITY, "v")
    					.where("v.databaseId").in(myDatabases)
    					.whereTrue("v.reportid=r.reportTemplateId"),
    				"defaultDashboard")
    		.from(Tables.REPORT_TEMPLATE, "r")
    		.leftJoin(Tables.USER_LOGIN, "o").on("o.userid=r.ownerUserId")
    		.leftJoin(mySubscriptions, "s").on("r.reportTemplateId=s.reportId")
    		.whereTrue("r.title is not null")
    		.where("r.ownerUserId").equalTo(context.getUser().getId())
    			.or("r.reportTemplateId").in(
    					SqlQuery.select("reportId") 
    						.from(Tables.REPORT_VISIBILITY, "v")
    						.where("v.databaseid").in( myDatabases ))
    		.execute(context.getTransaction(), new SqlResultCallback() {
				
				@Override
				public void onSuccess(final SqlTransaction tx, final SqlResultSet results) {
			        List<ReportMetadataDTO> dtos = Lists.newArrayList();
		
			        for(SqlResultSetRow row : results.getRows()) {
			        	   ReportMetadataDTO dto = new ReportMetadataDTO();
			               dto.setId(row.getInt("reportId"));
			               dto.setAmOwner(row.getInt("ownerUserId") == context.getUser().getId());
			               dto.setOwnerName(row.getString("ownerName"));
			               dto.setTitle(row.getString("title"));
			               dto.setEditAllowed(dto.getAmOwner());
			               if(!row.isNull("emaildelivery")) {
			            	   dto.setEmailDelivery(EmailDelivery.valueOf(row.getString("emaildelivery")));
			               }
			               if(row.isNull("emailday")) {
			            	   dto.setDay(1);
			               } else {
			            	   dto.setDay(row.getInt("emailday"));
			               }
			               if(row.isNull("dashboard")) {
			            	   // inherited from database-wide visibility
			            	   dto.setDashboard(!row.isNull("defaultDashboard") && row.getBoolean("defaultDashboard"));
			               } else {
			            	   dto.setDashboard(row.getBoolean("dashboard"));
			               }
			               dtos.add(dto);
			        }
			        
			        callback.onSuccess(new ReportsResult(dtos));
				}
			});
    		
    }
}
