package org.activityinfo.server.report.output;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.channels.Channels;
import java.util.Date;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.files.AppEngineFile;
import com.google.appengine.api.files.FileService;
import com.google.appengine.api.files.FileServiceFactory;
import com.google.appengine.api.files.FileWriteChannel;

public class AppEngineStorageProvider implements ImageStorageProvider {

	private DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

	@Override
	public ImageStorage getImageUrl(String suffix) throws IOException {
		
		Key tempFileKey = datastore.allocateIds("TempFile", 1).getStart();
		
		String url = "/generated/" + tempFileKey.getId();
		
		return new ImageStorage(url, new TempOutputStream(tempFileKey));
	}
	
	private class TempOutputStream extends OutputStream {

		private OutputStream out;
		private AppEngineFile writableFile;
		private FileWriteChannel writeChannel;
		private FileService fileService;
		private Key tempFileKey;

		public TempOutputStream(Key tempFileKey) throws IOException {
			this.tempFileKey = tempFileKey;
			
			fileService = FileServiceFactory.getFileService();
		    writableFile = fileService.createNewBlobFile("application/octet");
		    writeChannel = fileService.openWriteChannel(writableFile, false);
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
			out.close();
			writeChannel.closeFinally();
			BlobKey blobKey = FileServiceFactory.getFileService().getBlobKey(writableFile);
			
			Entity entity = new Entity(tempFileKey);
			entity.setUnindexedProperty("created", new Date());
			entity.setUnindexedProperty("blobKey", blobKey);
			datastore.put(entity);
		}
	}

}
