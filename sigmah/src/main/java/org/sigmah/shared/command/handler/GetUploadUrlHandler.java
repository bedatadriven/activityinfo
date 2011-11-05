package org.sigmah.shared.command.handler;

import java.net.URL;

import org.sigmah.server.command.handler.CommandHandler;
import org.sigmah.server.database.hibernate.entity.User;
import org.sigmah.shared.command.GetUploadUrl;
import org.sigmah.shared.command.result.CommandResult;
import org.sigmah.shared.command.result.UploadUrlResult;
import org.sigmah.shared.exception.CommandException;

import com.amazonaws.HttpMethod;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;

public class GetUploadUrlHandler implements CommandHandler<GetUploadUrl> {

	private String url;
	public GetUploadUrlHandler(){
		
	}
	
	@Override
	public CommandResult execute(GetUploadUrl cmd, User user)
			throws CommandException {
		UploadUrlResult uploadUrl = new UploadUrlResult();
		String awsAccessKeyId = "AKIAJSFOHLS57UUY5JPQ";
        String awsSecretAccessKey = "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";
        
        String bucketName = "site-attachments";
        String key = "SiteAttachments";
        
        AmazonS3Client client = new AmazonS3Client(new BasicAWSCredentials(awsAccessKeyId, awsSecretAccessKey));
        
        GeneratePresignedUrlRequest request = 
        	    new GeneratePresignedUrlRequest(bucketName, key, HttpMethod.PUT);
        	URL presignedUrl = client.generatePresignedUrl(request);
        
        uploadUrl.setUrl(presignedUrl.toString());
		
		return uploadUrl;
	}

}
