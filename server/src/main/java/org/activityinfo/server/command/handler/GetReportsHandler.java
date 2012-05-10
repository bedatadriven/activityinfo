/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.server.command.handler;

import java.util.List;

import javax.persistence.EntityManager;

import org.activityinfo.shared.command.GetReports;
import org.activityinfo.shared.command.handler.CommandHandlerAsync;
import org.activityinfo.shared.command.handler.ExecutionContext;
import org.activityinfo.shared.command.result.ReportsResult;
import org.activityinfo.shared.dto.ReportMetadataDTO;
import org.activityinfo.shared.report.model.EmailDelivery;

import com.bedatadriven.rebar.sql.client.SqlResultCallback;
import com.bedatadriven.rebar.sql.client.SqlResultSet;
import com.bedatadriven.rebar.sql.client.SqlResultSetRow;
import com.bedatadriven.rebar.sql.client.SqlTransaction;
import com.bedatadriven.rebar.sql.client.query.SqlQuery;
import com.google.common.collect.Lists;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;

/**
 * @author Alex Bertram
 * @see org.activityinfo.shared.command.GetReports
 */
public class GetReportsHandler implements CommandHandlerAsync<GetReports, ReportsResult> {


	@Override
	public void execute(GetReports command, final ExecutionContext context,
			final AsyncCallback<ReportsResult> callback) {
    	// note that we are excluding reports with a null title-- these
    	// reports have not yet been explicitly saved by the user
    	
		SqlQuery mySubscriptions = 	
			SqlQuery.selectAll()
				.from("reportsubscription")
				.where("userId").equalTo(context.getUser().getUserId());
		
		SqlQuery myDatabases = 
			SqlQuery.selectSingle("d.databaseid")
			.from("userdatabase", "d")
			.leftJoin(
				SqlQuery.selectAll()
					.from("UserPermission")
					.where("UserPermission.UserId")
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
    					.from("reportvisibility", "v")
    					.where("v.databaseId").in(myDatabases)
    					.whereTrue("v.reportid=r.reportTemplateId"),
    				"defaultDashboard")
    		.from("reporttemplate", "r")
    		.leftJoin("userlogin o").on("o.userid=r.ownerUserId")
    		.leftJoin(mySubscriptions, "s").on("r.reportTemplateId=s.reportId")
    		.whereTrue("r.title is not null")
    		.where("r.ownerUserId").equalTo(context.getUser().getId())
    			.or("r.reportTemplateId").in(
    					SqlQuery.select("reportId") 
    						.from("reportvisibility", "v")
    						.where("v.databaseid").in( myDatabases ))
    		.execute(context.getTransaction(), new SqlResultCallback() {
				
				@Override
				public void onSuccess(SqlTransaction tx, SqlResultSet results) {
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
