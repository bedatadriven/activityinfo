package org.sigmah.server.command.handler;

import org.activityinfo.shared.command.DeleteSiteAttachment;
import org.activityinfo.shared.command.handler.CommandHandlerAsync;
import org.activityinfo.shared.command.handler.ExecutionContext;
import org.activityinfo.shared.command.result.VoidResult;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.bedatadriven.rebar.sql.client.query.SqlUpdate;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;

public class DeleteSiteAttachmentHandler implements
		CommandHandlerAsync<DeleteSiteAttachment, VoidResult> {

	private AWSCredentials credentials;
	
	@Inject
	public DeleteSiteAttachmentHandler(AWSCredentials credentials) {
		this.credentials = credentials;
	}
	
	@Override
	public void execute(DeleteSiteAttachment command, ExecutionContext context,
			AsyncCallback<VoidResult> callback) {

		String bucketName = "site-attachments";
		String key = command.getBlobId();

		AmazonS3Client client = new AmazonS3Client(new BasicAWSCredentials(
				credentials.getAWSAccessKeyId(), credentials.getAWSSecretKey()));
		client.deleteObject(bucketName, key);

		SqlUpdate.delete("siteattachment")
				.where("blobid", command.getBlobId())
				.execute(context.getTransaction());

		callback.onSuccess(new VoidResult());
	}

}
