package org.activityinfo.server.report.output;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.channels.Channels;
import java.security.SecureRandom;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.files.AppEngineFile;
import com.google.appengine.api.files.FileService;
import com.google.appengine.api.files.FileServiceFactory;
import com.google.appengine.api.files.FileWriteChannel;
import com.google.inject.Provider;

public class AppEngineStorageProvider implements StorageProvider {

	private DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
	private SecureRandom random = new SecureRandom();
	private Provider<HttpServletRequest> request;
	
	public AppEngineStorageProvider(Provider<HttpServletRequest> request) {
		this.request = request;
	}
	
	@Override
	public TempStorage allocateTemporaryFile(String mimeType, String filename) throws IOException {
		
		Key tempFileKey = KeyFactory.createKey("TempFile", Long.toString(Math.abs(random.nextLong()), 16));
		
		StringBuilder url = new StringBuilder();
		url.append(request.get().isSecure() ? "https" : "http");
		url.append("://");
		url.append(request.get().getServerName());
		url.append(":");
		url.append(request.get().getServerPort());
		url.append("/generated/");
		url.append(tempFileKey.getName());
		url.append("/");
		url.append(filename);
		
		return new TempStorage(url.toString(), 
				new TempOutputStream(mimeType, filename, tempFileKey));
	}
	
	private class TempOutputStream extends OutputStream {

		private OutputStream out;
		private AppEngineFile writableFile;
		private FileWriteChannel writeChannel;
		private FileService fileService;
		private Key tempFileKey;
		private String mimeType;
		private String filename;

		public TempOutputStream(String mimeType, String filename, Key tempFileKey) throws IOException {
			this.tempFileKey = tempFileKey;
			this.mimeType = mimeType;
			this.filename = filename;
			
			fileService = FileServiceFactory.getFileService();
		    writableFile = fileService.createNewBlobFile(mimeType, filename);
		    writeChannel = fileService.openWriteChannel(writableFile, true);
		    out = Channels.newOutputStream(writeChannel);
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
			writeChannel.closeFinally();
			BlobKey blobKey = FileServiceFactory.getFileService().getBlobKey(writableFile);
			
			Entity entity = new Entity(tempFileKey);
			entity.setUnindexedProperty("mimeType", mimeType);
			entity.setUnindexedProperty("created", new Date());
			entity.setUnindexedProperty("blobKey", blobKey);
			entity.setUnindexedProperty("filename", filename);
			datastore.put(entity);
		}
	}

}
