package org.sigmah.shared.command.handler;

import java.net.URL;

import org.sigmah.server.attachment.ServletAttachmentUpload;
import org.sigmah.server.command.handler.CommandHandler;
import org.sigmah.server.database.hibernate.entity.User;
import org.sigmah.shared.command.GetUploadUrl;
import org.sigmah.shared.command.result.CommandResult;
import org.sigmah.shared.command.result.UploadUrlResult;
import org.sigmah.shared.exception.CommandException;

import com.amazonaws.HttpMethod;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.google.inject.Inject;

public class GetUploadUrlHandler implements CommandHandler<GetUploadUrl> {

	private AWSCredentials credentials;

	@Inject
	public GetUploadUrlHandler(AWSCredentials credentials) {
		this.credentials = credentials;
	}

	@Override
	public CommandResult execute(GetUploadUrl cmd, User user)
			throws CommandException {
		UploadUrlResult uploadUrl = new UploadUrlResult();

		String bucketName = "site-attachments";
		String key = cmd.getBlobid();

		AmazonS3Client client = new AmazonS3Client(new BasicAWSCredentials(
				credentials.getAWSAccessKeyId(), credentials.getAWSSecretKey()));

		URL presignedUrl = client.generatePresignedUrl(bucketName, key, null, HttpMethod.GET);

		uploadUrl.setUrl(presignedUrl.toString());

		return uploadUrl;
	}

}
