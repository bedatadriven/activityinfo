/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.command.handler;

import java.util.List;

import javax.persistence.EntityManager;

import org.sigmah.shared.command.GetReports;
import org.sigmah.shared.command.handler.CommandHandlerAsync;
import org.sigmah.shared.command.handler.ExecutionContext;
import org.sigmah.shared.command.result.ReportsResult;
import org.sigmah.shared.dto.ReportMetadataDTO;

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
 * @see org.sigmah.shared.command.GetReports
 */
public class GetReportsHandler implements CommandHandlerAsync<GetReports, ReportsResult> {


	@Override
	public void execute(GetReports command, ExecutionContext context,
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
    		.appendColumn("s.dashboard", "dashboard")
    		.appendColumn(
    				SqlQuery.selectSingle("max(defaultDashboard)")
    					.from("reportvisibility", "v")
    					.where("v.databaseId").in(myDatabases)
    					.whereTrue("v.reportid=r.reportTemplateId"),
    				"defaultDashboard")
    		.from("reporttemplate", "r")
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
			               dto.setAmOwner(true);
			               dto.setTitle(row.getString("title"));
			               dto.setEditAllowed(dto.getAmOwner());
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
