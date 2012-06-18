package org.activityinfo.server.attachment;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.activityinfo.server.command.DispatcherSync;
import org.activityinfo.shared.command.CreateSiteAttachment;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;
import org.apache.log4j.Logger;

import com.amazonaws.HttpMethod;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.ResponseHeaderOverrides;
import com.amazonaws.services.s3.model.S3Object;
import com.google.common.io.ByteStreams;
import com.google.gwt.http.client.URL;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class AttachmentServlet extends HttpServlet {

	private AWSCredentials credentials;
	private DispatcherSync dispatcher;
	
	private static final Logger LOGGER = Logger.getLogger(AttachmentServlet.class);
	
	@Inject
	public AttachmentServlet(AWSCredentials credentials,
			DispatcherSync dispatcher) {
		this.credentials = credentials;
		this.dispatcher = dispatcher;
	}
	

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		String bucketName = "site-attachments";
		String key = req.getParameter("blobId");

		AmazonS3Client client = new AmazonS3Client(new BasicAWSCredentials(
				credentials.getAWSAccessKeyId(), credentials.getAWSSecretKey()));
		
	
		ObjectMetadata metadata = client.getObjectMetadata(bucketName, key);
		resp.setHeader("Content-Disposition", metadata.getContentDisposition());
		resp.setHeader("Content-type", metadata.getContentType());
		
		S3Object object = client.getObject(bucketName, key);
		InputStream in = object.getObjectContent();
		ByteStreams.copy(in, resp.getOutputStream());
		in.close();
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		try {
			String key = request.getParameter("blobId");
			Integer siteId = Integer.valueOf(request.getParameter("siteId"));
			String bucketName = "site-attachments";
	
			FileItem fileItem = getFirstUploadFile(request);
			
			String fileName = fileItem.getName();
			InputStream uploadingStream = fileItem.getInputStream();
			
			ObjectMetadata metadata = new ObjectMetadata();
			metadata.setContentType(fileItem.getContentType());
			metadata.setContentLength(fileItem.getSize());
			metadata.setContentDisposition("Content-Disposition: attachment; filename=\"" + fileName + "\"");
			
			AmazonS3Client client = new AmazonS3Client(new BasicAWSCredentials(
					credentials.getAWSAccessKeyId(), credentials.getAWSSecretKey()));
			client.putObject(new PutObjectRequest(bucketName, key, uploadingStream, metadata));
	
			CreateSiteAttachment siteAttachment = new CreateSiteAttachment();
			siteAttachment.setSiteId(siteId);
			siteAttachment.setBlobId(key);
			siteAttachment.setFileName(fileName);
			siteAttachment.setBlobSize(fileItem.getSize());
			siteAttachment.setContentType(fileItem.getContentType());
	
			dispatcher.execute(siteAttachment);
		} catch(Exception e) {
			LOGGER.error("Error handling upload", e);
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
	}

	private FileItem getFirstUploadFile(HttpServletRequest request) throws FileUploadException {
		boolean isMultipart = ServletFileUpload.isMultipartContent(request);
		if (isMultipart) {

			FileItemFactory factory = new DiskFileItemFactory();
			ServletFileUpload upload = new ServletFileUpload(factory);
			
			List<FileItem> items = upload.parseRequest(request);
			
			for(FileItem item : items) {
				return item;
			}
		}
		throw new RuntimeException("No upload provided");
	}
}