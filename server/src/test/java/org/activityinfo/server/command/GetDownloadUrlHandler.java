package org.activityinfo.server.command;

import java.net.URL;

import org.activityinfo.server.command.handler.CommandHandler;
import org.activityinfo.server.database.hibernate.entity.User;
import org.activityinfo.shared.command.GetDownloadUrl;
import org.activityinfo.shared.command.result.CommandResult;
import org.activityinfo.shared.command.result.S3UrlResult;
import org.activityinfo.shared.exception.CommandException;

import com.amazonaws.HttpMethod;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.ResponseHeaderOverrides;
import com.google.inject.Inject;

public class GetDownloadUrlHandler implements CommandHandler<GetDownloadUrl> {

	private AWSCredentials credentials;

	@Inject
	public GetDownloadUrlHandler(AWSCredentials credentials) {
		this.credentials = credentials;
	}

	@Override
	public CommandResult execute(GetDownloadUrl cmd, User user)
			throws CommandException {
		S3UrlResult downloadFileUrl = new S3UrlResult();

		String bucketName = "site-attachments";
		String key = cmd.getBlobId();

		AmazonS3Client client = new AmazonS3Client(new BasicAWSCredentials(
				credentials.getAWSAccessKeyId(), credentials.getAWSSecretKey()));
		
		ObjectMetadata metadata = client.getObjectMetadata(bucketName, key);
		
		ResponseHeaderOverrides responseHeaders = new ResponseHeaderOverrides();
		responseHeaders.setContentDisposition(metadata.getContentDisposition());
		
		GeneratePresignedUrlRequest request = new GeneratePresignedUrlRequest(bucketName, key, HttpMethod.GET);
		request.withResponseHeaders(responseHeaders);
		
		URL presignedUrl = client.generatePresignedUrl(request);
		downloadFileUrl.setUrl(presignedUrl.toString());

		return downloadFileUrl;
	}

}
