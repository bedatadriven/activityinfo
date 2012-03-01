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
    private EntityManager em;

    @Inject
    public GetReportsHandler(EntityManager em) {
        this.em = em;
    }


	@Override
	public void execute(GetReports command, ExecutionContext context,
			final AsyncCallback<ReportsResult> callback) {
    	// note that we are excluding reports with a null title-- these
    	// reports have not yet been explicitly saved by the user
    	
    	SqlQuery.select()
    		.appendColumn("r.reportTemplateId", "reportId")
    		.appendColumn("r.title", "title")
    		.appendColumn("r.ownerUserId", "ownerUserId")
    		.appendColumn("s.dashboard", "dashboard")
    		.from("reporttemplate", "r")
    		.leftJoin("reportsubscription", "s").on("r.reportTemplateId=s.reportId")
    		.whereTrue("r.title is not null")
    		.where("r.ownerUserId").equalTo(context.getUser().getId())
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
			               dto.setDashboard(!row.isNull("dashboard") && row.getBoolean("dashboard"));
			               dtos.add(dto);
			        }
			        
			        callback.onSuccess(new ReportsResult(dtos));
				}
			});
    		
    }
}
