package org.activityinfo.server.util.blob;

import java.io.File;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;

public class BlobServiceModuleStub extends AbstractModule {

    @Override
    protected void configure() {
        
    }

    @Provides
    public BlobService provideBlobService() {
        File target = new File("target");
        File blobRoot = new File(target, "blobs");
        blobRoot.mkdirs();
        
        return new FsBlobService(blobRoot);
    }
}
