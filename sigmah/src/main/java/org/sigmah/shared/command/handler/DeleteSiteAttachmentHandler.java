package org.sigmah.shared.command.handler;

import org.sigmah.shared.command.DeleteSiteAttachment;
import org.sigmah.shared.command.result.VoidResult;

import com.bedatadriven.rebar.sql.client.query.SqlUpdate;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class DeleteSiteAttachmentHandler implements
		CommandHandlerAsync<DeleteSiteAttachment, VoidResult> {

	@Override
	public void execute(DeleteSiteAttachment command, ExecutionContext context,
			AsyncCallback<VoidResult> callback) {

		SqlUpdate.delete("siteattachment")
				.where("blobid", command.getBlobId())
				.execute(context.getTransaction());

		callback.onSuccess(new VoidResult());
	}

}
