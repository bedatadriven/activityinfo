package org.sigmah.shared.command.handler;

import java.util.ArrayList;
import java.util.List;

import org.sigmah.shared.command.GetSiteAttachments;
import org.sigmah.shared.command.handler.CommandHandlerAsync;
import org.sigmah.shared.command.handler.ExecutionContext;
import org.sigmah.shared.command.result.SiteAttachmentResult;
import org.sigmah.shared.dto.SiteAttachmentDTO;

import com.bedatadriven.rebar.sql.client.SqlException;
import com.bedatadriven.rebar.sql.client.SqlResultCallback;
import com.bedatadriven.rebar.sql.client.SqlResultSet;
import com.bedatadriven.rebar.sql.client.SqlResultSetRow;
import com.bedatadriven.rebar.sql.client.SqlTransaction;
import com.bedatadriven.rebar.sql.client.query.SqlQuery;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class GetSiteAttachmentsHandler implements CommandHandlerAsync<GetSiteAttachments, SiteAttachmentResult> {

	private List <SiteAttachmentDTO> dtos;
	
	@Override
	public void execute(GetSiteAttachments command, ExecutionContext context,
			AsyncCallback<SiteAttachmentResult> callback) {
		
		dtos = new  ArrayList<SiteAttachmentDTO>();
		
		SqlQuery.selectAll().from("siteattachment", "s")
			.where("s.siteid").equalTo(command.getSiteId())
			.execute(context.getTransaction(), new SqlResultCallback() {
				@Override
				public void onSuccess(SqlTransaction tx, SqlResultSet results) {
					for (SqlResultSetRow row : results.getRows()) {
						
						SiteAttachmentDTO dto = new SiteAttachmentDTO();
						
						dto.setAttachmentId(row.getInt("attachmentid"));
						dto.setSiteId(row.getInt("siteid"));
						dto.setBlobId(row.getString("blobid"));
						dto.setFileName(row.getString("filename"));
						dto.setUploadedBy(row.getString("uploadedby"));
						
						dtos.add(dto);
					}
				}

				@Override
				public boolean onFailure(SqlException e) {
					return super.onFailure(e);
				}
			});
		
		callback.onSuccess(new SiteAttachmentResult(dtos));
		
	}

}
