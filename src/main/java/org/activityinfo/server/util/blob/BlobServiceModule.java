package org.activityinfo.server.util.blob;

import java.io.File;

import org.activityinfo.server.DeploymentEnvironment;
import org.activityinfo.server.util.config.DeploymentConfiguration;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;

public class BlobServiceModule extends AbstractModule {

    private static final String BLOB_ROOT_KEY = "blob.root";

    @Override
    protected void configure() {
        
    }

    @Provides
    @Singleton
    public BlobService provideBlobService(DeploymentConfiguration config) {
        if (DeploymentEnvironment.isAppEngine()) {
            return new AppEngineBlobService();
        } else {
            String defaultRoot = System.getProperty("user.home") + File.separator + "activityinfo.blob";
            
            File blobRoot = new File(config.getProperty(BLOB_ROOT_KEY, defaultRoot));
            if (blobRoot.exists() && !blobRoot.isDirectory()) {
                throw new RuntimeException("blob.root must be a directory");
            }
            return new FsBlobService(blobRoot);
        }
    }
}
