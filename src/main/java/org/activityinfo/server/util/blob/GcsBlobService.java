package org.activityinfo.server.util.blob;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.Channels;

import com.google.appengine.api.files.AppEngineFile;
import com.google.appengine.api.files.FileReadChannel;
import com.google.appengine.api.files.FileService;
import com.google.appengine.api.files.FileServiceFactory;
import com.google.appengine.api.files.FileWriteChannel;
import com.google.appengine.api.files.GSFileOptions;
import com.google.appengine.api.files.GSFileOptions.GSFileOptionsBuilder;
import com.google.common.io.InputSupplier;

public class GcsBlobService  {

    public static final String GOOGLE_STORAGE_PREFIX = "/gs/";

    private FileService fileService = FileServiceFactory.getFileService();

    public OutputStream put(String key) throws IOException {
        GSFileOptions options = parseOptions(key);

        boolean lockForWrite = true;
        AppEngineFile writableFile = fileService.createNewGSFile(options);
        final FileWriteChannel writeChannel = fileService.openWriteChannel(writableFile, lockForWrite);
        OutputStream out = Channels.newOutputStream(writeChannel);

        return new DelegatingOutputStream(out) {

            @Override
            protected void afterClosed() throws IOException {
                writeChannel.closeFinally();
            }
        };
    }

    private GSFileOptions parseOptions(String key) {
        String path = key.substring(GOOGLE_STORAGE_PREFIX.length());
        int bucketEnd = path.indexOf('/');
        String bucketName = path.substring(0, bucketEnd);
        String keyName = path.substring(bucketEnd+1);

        GSFileOptions options = new GSFileOptionsBuilder()
        .setBucket(bucketName)
        .setKey(keyName)
        .build();
        return options;
    }

    public InputSupplier<InputStream> get(final String key)
        throws BlobNotFoundException {
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
}
