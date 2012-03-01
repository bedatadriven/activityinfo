package org.sigmah.shared.command.handler;

import org.sigmah.shared.command.CreateSiteAttachment;
import org.sigmah.shared.command.result.VoidResult;

import com.bedatadriven.rebar.sql.client.query.SqlInsert;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class CreateSiteAttachmentHandler implements CommandHandlerAsync<CreateSiteAttachment, VoidResult> {
	
	@Override
	public void execute(CreateSiteAttachment command, ExecutionContext context,
			AsyncCallback<VoidResult> callback) {
		
		SqlInsert.insertInto("siteattachment")
			.value("siteid", command.getSiteId())
			.value("blobid", command.getBlobId())
			.value("filename", command.getFileName())
			.value("uploadedby", context.getUser().getUserId())
			.value("blobsize", command.getBlobSize())
			.value("contenttype", command.getContentType())
			.execute(context.getTransaction());
		
		callback.onSuccess(new VoidResult());
	}
	

}
