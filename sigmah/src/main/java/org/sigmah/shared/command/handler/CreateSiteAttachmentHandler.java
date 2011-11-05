package org.sigmah.shared.command.handler;

import org.sigmah.shared.command.CreateSiteAttachment;
import org.sigmah.shared.command.handler.CommandHandlerAsync;
import org.sigmah.shared.command.handler.ExecutionContext;
import org.sigmah.shared.command.result.VoidResult;

import com.bedatadriven.rebar.sql.client.SqlTransaction;
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
			.value("uploadedby", "umad@gmail.com")
			.execute(context.getTransaction());
		
		callback.onSuccess(new VoidResult());
	}
	

}
