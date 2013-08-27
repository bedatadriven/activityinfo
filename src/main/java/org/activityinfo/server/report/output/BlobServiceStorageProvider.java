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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.security.SecureRandom;

import javax.ws.rs.core.UriBuilder;

import org.activityinfo.server.util.blob.BlobService;
import org.activityinfo.server.util.config.DeploymentConfiguration;

import com.google.common.io.ByteStreams;
import com.google.inject.Inject;

public class BlobServiceStorageProvider implements StorageProvider {

    private final SecureRandom random = new SecureRandom();
    private final BlobService blobService;
    private final String baseUri;

    @Inject
    public BlobServiceStorageProvider(DeploymentConfiguration config, BlobService blobService) {
        this.blobService = blobService;
        this.baseUri = config.getProperty("base.uri", "https://www.activityinfo.org");
    }

    @Override
    public TempStorage allocateTemporaryFile(String mimeType, String filename)
        throws IOException {

        String id = Long.toString(Math.abs(random.nextLong()), 16);
        
        URI uri = UriBuilder.fromUri(baseUri)
        .path("generated")
        .path(id)
        .path(filename)
        .build();

        return new TempStorage(uri.toString(),
            new TempOutputStream(id));
    }

    private class TempOutputStream extends OutputStream {

        private ByteArrayOutputStream out = new ByteArrayOutputStream();
        private String id;
        
        public TempOutputStream(String id) throws IOException {
            this.id = id;
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
            blobService.put("/temp/" + id, ByteStreams.newInputStreamSupplier(out.toByteArray()));
        }
    }
}
