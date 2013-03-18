package org.activityinfo.server.util.blob;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.Channels;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreInputStream;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.files.AppEngineFile;
import com.google.appengine.api.files.FileReadChannel;
import com.google.appengine.api.files.FileService;
import com.google.appengine.api.files.FileServiceFactory;
import com.google.appengine.api.files.FileWriteChannel;
import com.google.appengine.api.files.FinalizationException;
import com.google.appengine.api.files.GSFileOptions;
import com.google.appengine.api.files.GSFileOptions.GSFileOptionsBuilder;
import com.google.appengine.api.files.LockException;
import com.google.common.io.ByteStreams;
import com.google.common.io.InputSupplier;

/**
 * Implementation of the BlobService interface for the Google AppEngine environment.
 * 
 * <p>By default, blobs are stored in the Blobstore service and indexed in the Datastore.
 * However, blobs can also be read/write to Google Cloud Storage by using the prefix 
 * "/gs/" as in "/gs/bucket_name/key"
 * 
 */
public class AppEngineBlobService implements BlobService {

    private static final String GOOGLE_STORAGE_PREFIX = "/gs/";
    
    private DatastoreService datastore;
    private FileService fileService;

    public AppEngineBlobService() {
        datastore = DatastoreServiceFactory.getDatastoreService();
        fileService = FileServiceFactory.getFileService();
    }

    @Override
    public void put(String key, InputSupplier<? extends InputStream> blob) throws IOException {
        if(key.startsWith(GOOGLE_STORAGE_PREFIX)) {
            putToGCS(key, blob);
        } else {
            putToBlobstore(key, blob);
        }
    }

    private void putToGCS(String key, InputSupplier<? extends InputStream> blob) throws IOException {
        String path = key.substring(GOOGLE_STORAGE_PREFIX.length());
        int bucketEnd = path.indexOf('/');
        String bucketName = path.substring(0, bucketEnd);
        String keyName = path.substring(bucketEnd+1);
        
        GSFileOptions options = new GSFileOptionsBuilder()
        .setBucket(bucketName)
        .setKey(keyName)
        .build();
        
        boolean lockForWrite = false; // Do you want to exclusively lock this object?
        AppEngineFile writableFile = fileService.createNewGSFile(options);
        FileWriteChannel writeChannel = fileService.openWriteChannel(writableFile, lockForWrite);
        OutputStream out = Channels.newOutputStream(writeChannel);
        ByteStreams.copy(blob, out);
        out.flush();
        writeChannel.closeFinally();
    }

    private void putToBlobstore(String key,
        InputSupplier<? extends InputStream> blob) throws IOException,
        FileNotFoundException, FinalizationException, LockException {
        AppEngineFile file = fileService.createNewBlobFile("application/octet-stream");
        boolean lock = true;
        FileWriteChannel writeChannel = fileService.openWriteChannel(
            file, lock);
        OutputStream os = Channels.newOutputStream(writeChannel);
        ByteStreams.copy(blob, os);
        os.flush();
        writeChannel.closeFinally();
        
        Entity entity = new Entity("Blob", key);
        entity.setUnindexedProperty("blobKey", fileService.getBlobKey(file));
        datastore.put(entity);
    }

    @Override
    public InputSupplier<InputStream> get(String key) throws BlobNotFoundException {
        if(key.startsWith(GOOGLE_STORAGE_PREFIX)) {
            return getFromGCS(key);
        } else {
            return getFromBlobstore(key);
        }
    }

    private InputSupplier<InputStream> getFromGCS(final String key) {
        return new InputSupplier<InputStream>() {
            
            @Override
            public InputStream getInput() throws IOException {
                boolean lockForRead = false;
                AppEngineFile readableFile = new AppEngineFile(key);
                FileReadChannel readChannel = fileService.openReadChannel(readableFile, lockForRead);
                
                return Channels.newInputStream(readChannel);
            }
        };
    }

    private InputSupplier<InputStream> getFromBlobstore(String key)
        throws BlobNotFoundException {
        Entity entity;
        try {
            entity = datastore.get(KeyFactory.createKey("Blob", key));
        } catch (EntityNotFoundException e) {
            throw new BlobNotFoundException();
        }
        
        final BlobKey blobKey = (BlobKey) entity.getProperty("blobKey");

        return new InputSupplier<InputStream>() {
            @Override
            public InputStream getInput() throws IOException {
                return new BlobstoreInputStream(blobKey);
            }
        };
    }
}
