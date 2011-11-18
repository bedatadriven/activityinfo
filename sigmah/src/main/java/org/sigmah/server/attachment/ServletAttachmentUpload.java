package org.sigmah.server.attachment;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import liquibase.util.file.FilenameUtils;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.sigmah.server.command.DispatcherSync;
import org.sigmah.shared.command.CreateSiteAttachment;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class ServletAttachmentUpload extends HttpServlet {

	private AWSCredentials credentials;
	private DispatcherSync dispatcher;
	private CreateSiteAttachment siteAttachment;
	private InputStream uploadingStream;
	private ObjectMetadata metadata;
	private FileItem fileItem = null;
	private String key;
	private String fileName;
	private int siteId;
	private String bucketName;
	
	@Inject
	public ServletAttachmentUpload(AWSCredentials credentials,
			DispatcherSync dispatcher) {
		this.credentials = credentials;
		this.dispatcher = dispatcher;
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		key = request.getParameter("blobId");
		siteId = Integer.valueOf(request.getParameter("siteId"));
		bucketName = "site-attachments";

		List items = null;

		boolean isMultipart = ServletFileUpload.isMultipartContent(request);
		if (isMultipart) {

			FileItemFactory factory = new DiskFileItemFactory();
			ServletFileUpload upload = new ServletFileUpload(factory);
			String optionalFileName = "";
			
			try {
				items = upload.parseRequest(request);
			} catch (FileUploadException e1) {
				e1.printStackTrace();
			}
			
			Iterator iterator = items.iterator();
			while (iterator.hasNext()) {
				FileItem fileItemTemp = (FileItem) iterator.next();
				if(fileItemTemp.getName()!= null){
					fileItem = fileItemTemp;
				}
			}

			if (fileItem != null) {
				fileName = fileItem.getName();

				if (fileItem.getSize() > 0) {
					
					fileName = FilenameUtils.getName(fileName);
					
					uploadingStream = fileItem.getInputStream();
				}
			}
		}

		metadata = new ObjectMetadata();
		metadata.setContentType(fileItem.getContentType());
		metadata.setContentLength(fileItem.getSize());
		AmazonS3Client client = new AmazonS3Client(new BasicAWSCredentials(
				credentials.getAWSAccessKeyId(), credentials.getAWSSecretKey()));
		client.putObject(new PutObjectRequest(bucketName, key, uploadingStream, metadata));

		siteAttachment = new CreateSiteAttachment();
		siteAttachment.setSiteId(siteId);
		siteAttachment.setBlobId(key);
		siteAttachment.setFileName(fileName);
		siteAttachment.setBlobSize(fileItem.getSize());
		siteAttachment.setContentType(fileItem.getContentType());

		dispatcher.execute(siteAttachment);
	}
}