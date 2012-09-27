package org.activityinfo.server.report.output;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.channels.Channels;
import java.security.SecureRandom;
import java.util.Date;

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

public class AppEngineStorageProvider implements ImageStorageProvider {

	private DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
	private SecureRandom random = new SecureRandom();
	@Override
	public ImageStorage getImageUrl(String mimeType, String suffix) throws IOException {
		
		Key tempFileKey = KeyFactory.createKey("TempFile", Long.toString(Math.abs(random.nextLong()), 16));
		
		String url = "/generated/" + tempFileKey.getName();
		
		return new ImageStorage(url, new TempOutputStream(mimeType, suffix, tempFileKey));
	}
	
	private class TempOutputStream extends OutputStream {

		private OutputStream out;
		private AppEngineFile writableFile;
		private FileWriteChannel writeChannel;
		private FileService fileService;
		private Key tempFileKey;
		private String mimeType;

		public TempOutputStream(String mimeType, String suffix, Key tempFileKey) throws IOException {
			this.tempFileKey = tempFileKey;
			this.mimeType = mimeType;
			
			fileService = FileServiceFactory.getFileService();
		    writableFile = fileService.createNewBlobFile(mimeType, "activityinfo" + suffix);
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
			datastore.put(entity);
		}
	}

}
