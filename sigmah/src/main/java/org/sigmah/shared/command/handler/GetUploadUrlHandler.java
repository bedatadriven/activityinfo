package org.sigmah.shared.command.handler;

import java.net.URL;

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
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.ResponseHeaderOverrides;
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
		UploadUrlResult downloadFileUrl = new UploadUrlResult();

		String bucketName = "site-attachments";
		String key = cmd.getBlobId();

		AmazonS3Client client = new AmazonS3Client(new BasicAWSCredentials(
				credentials.getAWSAccessKeyId(), credentials.getAWSSecretKey()));

		ResponseHeaderOverrides responseHeaders = new ResponseHeaderOverrides();
		responseHeaders.setContentDisposition(ResponseHeaderOverrides.RESPONSE_HEADER_CONTENT_DISPOSITION);
		
		GeneratePresignedUrlRequest request = new GeneratePresignedUrlRequest(bucketName, key, HttpMethod.GET);
		request.withResponseHeaders(responseHeaders);
		
		URL presignedUrl = client.generatePresignedUrl(request);
		downloadFileUrl.setUrl(presignedUrl.toString());

		return downloadFileUrl;
	}

}
