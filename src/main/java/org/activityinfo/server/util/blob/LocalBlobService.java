package org.activityinfo.server.util.blob;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;

import com.google.common.io.Files;
import com.google.common.io.InputSupplier;

public class LocalBlobService implements BlobService {

    private static final Logger LOGGER = Logger.getLogger(LocalBlobService.class.getName());
    
    private final String rootDir;

    public LocalBlobService(File blobRoot) {
        rootDir = blobRoot.getAbsolutePath();
    }

    private File getFile(String key) {
        return new File(rootDir + File.separator + key.replace('/', File.separatorChar));
    }

    @Override
    public void put(String key, InputSupplier<? extends InputStream> blob)
        throws IOException {
        File file = getFile(key);
        file.getParentFile().mkdirs();
        Files.copy(blob, file);
        LOGGER.info("Wrote blob '" +  key + "' to " + file.getAbsolutePath());
    }

    @Override
    public InputSupplier<FileInputStream> get(String key)
        throws BlobNotFoundException {
        File file = getFile(key);
        if (!file.exists()) {
            LOGGER.severe("Couldn't find '" + key + "' at " + file.getAbsolutePath());
            throw new BlobNotFoundException();
        }
        return Files.newInputStreamSupplier(file);
    }
}
