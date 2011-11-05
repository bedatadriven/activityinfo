package org.sigmah.shared.command.handler;

import org.sigmah.shared.command.DeleteSiteAttachment;
import org.sigmah.shared.command.handler.CommandHandlerAsync;
import org.sigmah.shared.command.handler.ExecutionContext;
import org.sigmah.shared.command.result.VoidResult;

import com.bedatadriven.rebar.sql.client.query.SqlQuery;
import com.bedatadriven.rebar.sql.client.query.SqlUpdate;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class DeleteSiteAttachmentHandler  implements CommandHandlerAsync<DeleteSiteAttachment, VoidResult>  {

	@Override
	public void execute(DeleteSiteAttachment command, ExecutionContext context,
			AsyncCallback<VoidResult> callback) {
		
//		SqlQuery.from("siteattachment")
//			.value("siteid", command.getSiteId())
//			.value("blobid", command.getBlobId())
//			.value("filename", command.getFileName())
//			.value("uploadedby", "umad@gmail.com")
//			.execute(context.getTransaction());
		
		callback.onSuccess(new VoidResult());		
	}

}
