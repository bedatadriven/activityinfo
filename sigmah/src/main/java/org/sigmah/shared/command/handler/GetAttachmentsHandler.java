package org.sigmah.shared.command.handler;

import java.util.List;

import org.sigmah.shared.command.GetAttachments;
import org.sigmah.shared.command.GetAttachments.GetAttachmentsResult;
import org.sigmah.shared.dto.AttachmentDTO;

import com.bedatadriven.rebar.sql.client.SqlResultCallback;
import com.bedatadriven.rebar.sql.client.SqlResultSet;
import com.bedatadriven.rebar.sql.client.SqlResultSetRow;
import com.bedatadriven.rebar.sql.client.SqlTransaction;
import com.bedatadriven.rebar.sql.client.query.SqlQuery;
import com.google.common.collect.Lists;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class GetAttachmentsHandler implements CommandHandlerAsync<GetAttachments, GetAttachmentsResult>{
	@Override
	public void execute(final GetAttachments command, ExecutionContext context, final AsyncCallback<GetAttachmentsResult> callback) {
		SqlQuery.selectAll()
				.from("Attachment")
				.where("SiteId")
				.equalTo(command.getSiteId())
				
				.execute(context.getTransaction(), new SqlResultCallback() {
					@Override
					public void onSuccess(SqlTransaction tx, SqlResultSet results) {
						List<AttachmentDTO> attachments = Lists.newArrayList();
						for (SqlResultSetRow row : results.getRows()) {
							AttachmentDTO attachment = new AttachmentDTO();
							attachment.setId(row.getInt("AttachmentId"));
							attachment.setExtension(row.getString("extension"));
							attachment.setCreatedDate(row.getDate("createdDate"));
							attachment.setName(row.getString("name"));
							attachment.setSizeInKb(row.getInt("sizeInKb"));
							attachment.setSiteId(command.getSiteId());
							attachments.add(attachment);
						}
						callback.onSuccess(new GetAttachmentsResult(attachments));
					}
				});
	}
}
