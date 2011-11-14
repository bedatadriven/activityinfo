package org.sigmah.server.attachment;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.sigmah.server.command.DispatcherSync;
import org.sigmah.shared.command.CreateSiteAttachment;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class ServletAttachmentUpload extends HttpServlet {

	private AWSCredentials credentials;
	private DispatcherSync dispatcher;
	private File uploadFile;
	private CreateSiteAttachment siteAttachment;

	@Inject
	public ServletAttachmentUpload(AWSCredentials credentials,
			DispatcherSync dispatcher) {
		this.credentials = credentials;
		this.dispatcher = dispatcher;
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String key = request.getParameter("blobId");
		int siteId = Integer.valueOf(request.getParameter("siteId"));
		String bucketName = "site-attachments";

		String saveFile = "";
		String attachmentType = "";
		int endPos = 0;
		String contentType = request.getContentType();

		if ((contentType != null)
				&& (contentType.indexOf("multipart/form-data") >= 0)) {

			DataInputStream in = new DataInputStream(request.getInputStream());
			int formDataLength = request.getContentLength();
			byte dataBytes[] = new byte[formDataLength];
			int byteRead = 0;
			int totalBytesRead = 0;
			while (totalBytesRead < formDataLength) {
				byteRead = in.read(dataBytes, totalBytesRead, formDataLength);
				totalBytesRead += byteRead;
			}
			String file = new String(dataBytes);
			saveFile = file.substring(file.indexOf("filename=\"") + 10);
			saveFile = saveFile.substring(0, saveFile.indexOf("\n"));
			saveFile = saveFile.substring(saveFile.lastIndexOf("\\") + 1,
					saveFile.indexOf("\""));

			attachmentType = file.substring(file.indexOf("Content-Type:") + 14);
			attachmentType = attachmentType.substring(0,
					attachmentType.indexOf("\n"));

			int lastIndex = contentType.lastIndexOf("=");
			String boundary = contentType.substring(lastIndex + 1,
					contentType.length());
			int pos;
			pos = file.indexOf("filename=\"");
			pos = file.indexOf("\n", pos) + 1;
			pos = file.indexOf("\n", pos) + 1;
			pos = file.indexOf("\n", pos) + 1;
			int boundaryLocation = file.indexOf(boundary, pos) - 4;
			int startPos = ((file.substring(0, pos)).getBytes()).length;
			endPos = ((file.substring(0, boundaryLocation)).getBytes()).length;
			uploadFile = new File(saveFile);
			FileOutputStream fileOut = new FileOutputStream(uploadFile);
			fileOut.write(dataBytes, startPos, (endPos - startPos));
			fileOut.flush();
			fileOut.close();

		}

		AmazonS3Client client = new AmazonS3Client(new BasicAWSCredentials(
				credentials.getAWSAccessKeyId(), credentials.getAWSSecretKey()));
		client.putObject(new PutObjectRequest(bucketName, key, uploadFile));

		siteAttachment = new CreateSiteAttachment();
		siteAttachment.setSiteId(siteId);
		siteAttachment.setBlobId(key);
		siteAttachment.setFileName(saveFile);
		siteAttachment.setBlobSize(endPos);
		siteAttachment.setContentType(attachmentType);

		dispatcher.execute(siteAttachment);
	}
}