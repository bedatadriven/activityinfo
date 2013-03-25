package org.activityinfo.server.util.blob;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.google.common.io.InputSupplier;

/**
 * Generic interface to a blob storage mechanism.
 * Can be backed by AppEngine or just a local directory
 * if AI is running on a single server
 */
public interface BlobService {
    
    void put(String key, InputSupplier<? extends InputStream> blob) throws IOException;
    
    OutputStream put(String key) throws IOException;

    InputSupplier<? extends InputStream> get(String key) throws BlobNotFoundException;
}
