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
import java.io.InputStream;

import javax.inject.Singleton;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.activityinfo.server.util.blob.BlobNotFoundException;
import org.activityinfo.server.util.blob.BlobService;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.io.ByteStreams;
import com.google.common.io.InputSupplier;
import com.google.inject.Inject;

/**
 * Serves up temporary files created during the rendering process. The actual
 * blob is identified by a secure hexadecimal id, but a friendly filename can be
 * appended to the uri, which is the best cross-browser way to indicate the file
 * name to be saved as. For example:
 * 
 * <blockquote>
 * http://www.activityinfo.org/generated/1b391a99c2b49a2c/Untitled%20
 * Report%2020130426_0446.rtf </blockquote>
 * 
 * In this URL, the text following the 1b391a99c2b49a2c/ is ignored, but used by
 * all browsers to suggest the file name to save.
 * 
 * 
 */
@Singleton
public class TempStorageServlet extends HttpServlet {

    private final BlobService blobService;

    @Inject
    public TempStorageServlet(BlobService blobService) {
        super();
        this.blobService = blobService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException {

        String keyName = parseBlobKey(req.getRequestURI());
        
        InputSupplier<? extends InputStream> inputSupplier;
        try {
            inputSupplier = blobService.get("/temp/" + keyName);
        } catch(BlobNotFoundException e) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        resp.setHeader("Content-Type", mimeTypeFromUri(req.getRequestURI().toLowerCase()));
        resp.setHeader("Content-Disposition", "attachment");   
        ByteStreams.copy(inputSupplier, resp.getOutputStream());
    }

    private String mimeTypeFromUri(String uri) {
        if (uri.endsWith(".rtf")) {
            return "application/rtf";
        } else if (uri.endsWith(".pdf")) {
            return "application/pdf";
        } else if (uri.endsWith(".png")) {
            return "image/png";
        } else if (uri.endsWith(".html")) {
            return "text/html";
        } else if (uri.endsWith(".ppt")) {
            return "application/vnd.ms-powerpoint";
        } else if (uri.endsWith(".xls")) {
            return "application/vnd.ms-excel";
        } else {
            return "application/octet-stream";
        }
    }

    @VisibleForTesting
    static String parseBlobKey(String uri) {
        String prefix = "/generated/";
        int start = uri.indexOf(prefix);
        if (start == -1) {
            throw new IllegalArgumentException(uri);
        }
        start += prefix.length();
        int keyEnd = uri.indexOf('/', start + 1);
        if (keyEnd == -1) {
            throw new IllegalArgumentException(uri);
        }
        return uri.substring(start, keyEnd);
    }

}
