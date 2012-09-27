package org.activityinfo.shared.command.handler;

import java.util.ArrayList;
import java.util.List;

import org.activityinfo.shared.command.GetSiteAttachments;
import org.activityinfo.shared.command.result.SiteAttachmentResult;
import org.activityinfo.shared.db.Tables;
import org.activityinfo.shared.dto.SiteAttachmentDTO;

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
			final AsyncCallback<SiteAttachmentResult> callback) {
		
		dtos = new  ArrayList<SiteAttachmentDTO>();
		
		SqlQuery.selectAll().from(Tables.SITE_ATTACHMENT, "s")
			.where("s.siteid").equalTo(command.getSiteId())
			.execute(context.getTransaction(), new SqlResultCallback() {
				@Override
				public void onSuccess(SqlTransaction tx, SqlResultSet results) {
					for (SqlResultSetRow row : results.getRows()) {
						
						SiteAttachmentDTO dto = new SiteAttachmentDTO();
						
						dto.setSiteId(row.getInt("siteid"));
						dto.setBlobId(row.getString("blobid"));
						dto.setFileName(row.getString("filename"));
						dto.setUploadedBy(row.getInt("uploadedby"));
						dto.setBlobSize(row.getInt("blobsize"));
						dto.setContentType(row.getString("contenttype"));
						
						dtos.add(dto);
					}
					callback.onSuccess(new SiteAttachmentResult(dtos));
				}
			});	
	}
}
