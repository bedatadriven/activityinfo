package org.activityinfo.server.attachment;

/*
 * #%L
 * ActivityInfo Server
 * %%
 * Copyright (C) 2009 - 2013 UNICEF
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.activityinfo.server.command.DispatcherSync;
import org.activityinfo.server.util.config.DeploymentConfiguration;
import org.activityinfo.shared.command.CreateSiteAttachment;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.google.common.io.ByteStreams;

public class S3AttachmentService implements AttachmentService {

	private AWSCredentials credentials;
	private String bucketName;	

	@Inject
	public S3AttachmentService(AWSCredentials credentials,
			DeploymentConfiguration config,
			DispatcherSync dispatcher) {
		this.credentials = credentials;
		bucketName = config.getProperty("bucket.site.attachments");
	}
	
	@Override
	public void serveAttachment(String key, HttpServletResponse resp) throws IOException {
		AmazonS3Client client = new AmazonS3Client(credentials);
		
		ObjectMetadata metadata = client.getObjectMetadata(bucketName, key);
		resp.setHeader("Content-Disposition", metadata.getContentDisposition());
		resp.setHeader("Content-type", metadata.getContentType());
		
		S3Object object = client.getObject(bucketName, key);
		InputStream in = object.getObjectContent();
		ByteStreams.copy(in, resp.getOutputStream());
		in.close();
		
	}

	@Override
	public void upload(String key, FileItem fileItem,
			InputStream uploadingStream) {
		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentType(fileItem.getContentType());
		metadata.setContentLength(fileItem.getSize());
		metadata.setContentDisposition("Content-Disposition: attachment; filename=\"" + fileItem.getName() + "\"");
		
		AmazonS3Client client = new AmazonS3Client(credentials);
		client.putObject(new PutObjectRequest(bucketName, key, uploadingStream, metadata));
	}

	@Override
	public FileItemFactory createFileItemFactory() {
		return new DiskFileItemFactory();
	}

	@Override
	public void delete(String key) {
		AmazonS3Client client = new AmazonS3Client(credentials);
		client.deleteObject(bucketName, key);
	}
	

}
