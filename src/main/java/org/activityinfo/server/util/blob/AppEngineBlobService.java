package org.activityinfo.server.util.blob;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

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

   
    private BlobstoreBlobService blobstore = new BlobstoreBlobService();
    private GcsBlobService gcs = new GcsBlobService();
   
    @Override
    public void put(String key, InputSupplier<? extends InputStream> blob) throws IOException {
        OutputStream out = put(key);
        ByteStreams.copy(blob, out);
        out.close();
    }
    

    @Override
    public OutputStream put(String key) throws IOException {
        if(key.startsWith(GcsBlobService.GOOGLE_STORAGE_PREFIX)) {
            return gcs.put(key);
        } else {
            return blobstore.put(key);
        }
    }


    @Override
    public InputSupplier<InputStream> get(String key) throws BlobNotFoundException {
        if(key.startsWith(GcsBlobService.GOOGLE_STORAGE_PREFIX)) {
            return gcs.get(key);
        } else {
            return blobstore.get(key);
        }
    }
}
