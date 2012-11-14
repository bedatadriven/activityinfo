package org.activityinfo.server.command.handler;

import org.activityinfo.server.attachment.AttachmentService;
import org.activityinfo.shared.command.DeleteSiteAttachment;
import org.activityinfo.shared.command.handler.CommandHandlerAsync;
import org.activityinfo.shared.command.handler.ExecutionContext;
import org.activityinfo.shared.command.result.VoidResult;
import org.activityinfo.shared.db.Tables;

import com.bedatadriven.rebar.sql.client.query.SqlUpdate;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;

public class DeleteSiteAttachmentHandler implements
		CommandHandlerAsync<DeleteSiteAttachment, VoidResult> {

	private AttachmentService attachmentService;
	
	@Inject
	public DeleteSiteAttachmentHandler(AttachmentService attachmentService) {
		super();
		this.attachmentService = attachmentService;
	}

	@Override
	public void execute(DeleteSiteAttachment command, ExecutionContext context,
			AsyncCallback<VoidResult> callback) {

		attachmentService.delete(command.getBlobId());
		
		SqlUpdate.delete(Tables.SITE_ATTACHMENT)
				.where("blobid", command.getBlobId())
				.execute(context.getTransaction());

		callback.onSuccess(new VoidResult());
	}

}
