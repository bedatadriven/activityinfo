package org.activityinfo.server.report.output;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.util.Date;

import org.activityinfo.server.authentication.SecureTokenGenerator;
import org.activityinfo.server.util.config.DeploymentConfiguration;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.StorageClass;
import com.google.inject.Inject;

public class S3StorageProvider implements ImageStorageProvider {
	private String bucket;
	private AmazonS3Client client;
	
	@Inject
	public S3StorageProvider(AWSCredentials credentials,
			DeploymentConfiguration config) {
		super();
		this.client = new AmazonS3Client(credentials);
		this.bucket = config.getProperty("exports.bucket", "ai-generated");
	}

	@Override
	public ImageStorage getImageUrl(String suffix) throws IOException {
		
		String key = SecureTokenGenerator.generate();
		
		// generate url
		URL url = client.generatePresignedUrl(bucket, key, new Date( new Date().getTime() + 1000 * 60 * 120 ) );

		return new ImageStorage(url.toString(), new S3OutputStream(key, suffix));
	}

	private class S3OutputStream extends OutputStream {
		
		private final OutputStream out;
		private String key;
		private File tempFile;
		private String suffix;
		
		public S3OutputStream(String key, String suffix) throws IOException {
			super();
			this.key = key;
			this.tempFile = File.createTempFile("report", "activityinfo");
		    this.out = new FileOutputStream(tempFile);
		    this.suffix = suffix;
		    
		}

		@Override
		public void write(int b) throws IOException {
			out.write(b);
		}

		@Override
		public void flush() throws IOException {
			out.flush();
		}

		@Override
		public void write(byte[] b, int off, int len) throws IOException {
			out.write(b, off, len);
		}

		@Override
		public void write(byte[] b) throws IOException {
			out.write(b);
		}
		

		@Override
		public void close() throws IOException {
			out.close();
			
			ObjectMetadata metadata = new ObjectMetadata();
			metadata.setContentDisposition("attachment; filename=\"activityinfo." + suffix + "\"");
			
			PutObjectRequest put = new PutObjectRequest(bucket, key, tempFile);
			put.setStorageClass(StorageClass.ReducedRedundancy);
			put.setMetadata(metadata);
			
			client.putObject(put);
			tempFile.delete();
		}		
	}
	
}
