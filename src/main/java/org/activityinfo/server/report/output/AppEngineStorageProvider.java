package org.activityinfo.server.report.output;

/*
 * #%L
 * ActivityInfo Server
 * %%
 * Copyright (C) 2009 - 2013 UNICEF
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import java.io.IOException;
import java.io.OutputStream;
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

    private DatastoreService datastore = DatastoreServiceFactory
        .getDatastoreService();
    private SecureRandom random = new SecureRandom();
    private Provider<HttpServletRequest> request;

    public AppEngineStorageProvider(Provider<HttpServletRequest> request) {
        this.request = request;
    }

    @Override
    public TempStorage allocateTemporaryFile(String mimeType, String filename)
        throws IOException {

        Key tempFileKey = KeyFactory.createKey("TempFile",
            Long.toString(Math.abs(random.nextLong()), 16));

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

        public TempOutputStream(String mimeType, String filename,
            Key tempFileKey) throws IOException {
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
            BlobKey blobKey = FileServiceFactory.getFileService().getBlobKey(
                writableFile);

            Entity entity = new Entity(tempFileKey);
            entity.setUnindexedProperty("mimeType", mimeType);
            entity.setUnindexedProperty("created", new Date());
            entity.setUnindexedProperty("blobKey", blobKey);
            entity.setUnindexedProperty("filename", filename);
            datastore.put(entity);
        }
    }

}
