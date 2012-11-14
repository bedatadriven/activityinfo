package org.activityinfo.server.attachment;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.channels.Channels;
import java.nio.charset.Charset;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;

import com.google.appengine.api.blobstore.BlobInfo;
import com.google.appengine.api.blobstore.BlobInfoFactory;
import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.files.AppEngineFile;
import com.google.appengine.api.files.FileService;
import com.google.appengine.api.files.FileServiceFactory;
import com.google.appengine.api.files.FileWriteChannel;
import com.google.appengine.api.files.GSFileOptions.GSFileOptionsBuilder;
import com.google.common.io.ByteStreams;

public class AppEngineAttachmentService implements AttachmentService {

	
	private BlobstoreService blobstoreService = BlobstoreServiceFactory.getBlobstoreService();

	@Override
	public void serveAttachment(String blobId, HttpServletResponse response) throws IOException {
		BlobKey blobKey = blobKey(blobId);
		blobstoreService.serve(blobKey, response);
	}

	private BlobKey blobKey(String blobId) {
		BlobKey blobKey = blobstoreService.createGsBlobKey("/gs/activityinfo-attachments/" + blobId);
		return blobKey;
	}

	@Override
	public void upload(String key, FileItem fileItem,
			InputStream uploadingStream) {
		try {
	
			GSFileOptionsBuilder builder = new GSFileOptionsBuilder()
			.setBucket("activityinfo-attachments")
			.setKey(key)
			.setContentDisposition("attachment; filename=\"" + fileItem.getName() + "\"")
			.setMimeType(fileItem.getContentType());
	
			FileService fileService = FileServiceFactory.getFileService();
			AppEngineFile writableFile = fileService.createNewGSFile(builder.build());
			boolean lock = true;
			FileWriteChannel writeChannel = fileService.openWriteChannel(writableFile, lock);
			OutputStream os = Channels.newOutputStream(writeChannel);
			ByteStreams.copy(fileItem.getInputStream(), os);
			os.flush();
			writeChannel.closeFinally();

		} catch(Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void delete(String key) {
		blobstoreService.delete(blobKey(key));
	}

	@Override
	public FileItemFactory createFileItemFactory() {
		return new FileItemFactory() {
			
			@Override
			public FileItem createItem(String fieldName, String contentType,
					boolean isFormField, String fileName) {
				return new InMemoryFileItem(fieldName, contentType, isFormField, fileName);
			}
		};
	}
	
	private static class InMemoryFileItem implements FileItem {

		private ByteArrayOutputStream baos = new ByteArrayOutputStream();
		private boolean formField;
		private String fieldName;
		private String contentType;
		private String fileName;
		
		public InMemoryFileItem(String fieldName, String contentType,
				boolean isFormField, String fileName) {
			this.fieldName = fieldName;
			this.contentType = contentType;
			this.formField = isFormField;
			this.fileName = fileName;
		}

		@Override
		public InputStream getInputStream() throws IOException {
			return new ByteArrayInputStream(baos.toByteArray());
		}

		@Override
		public String getContentType() {
			return contentType;
		}

		@Override
		public String getName() {
			return fileName;
		}

		@Override
		public boolean isInMemory() {
			return true;
		}

		@Override
		public long getSize() {
			return baos.size();
		}

		@Override
		public byte[] get() {
			return baos.toByteArray();
		}

		@Override
		public String getString(String encoding)
				throws UnsupportedEncodingException {
			return new String(baos.toByteArray(), Charset.forName(encoding));
		}

		@Override
		public String getString() {
			return new String(baos.toByteArray());
		}

		@Override
		public void write(File file) throws Exception {
			throw new UnsupportedOperationException();
		}

		@Override
		public void delete() {
			baos.reset();
		}

		@Override
		public String getFieldName() {
			return fieldName;
		}

		@Override
		public void setFieldName(String name) {
			this.fieldName = name;
		}

		@Override
		public boolean isFormField() {
			return formField;
		}

		@Override
		public void setFormField(boolean state) {
			this.formField = state;
		}

		@Override
		public OutputStream getOutputStream() throws IOException {
			return baos;
		}
		
	}
}
