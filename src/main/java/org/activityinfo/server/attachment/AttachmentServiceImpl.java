package org.activityinfo.server.attachment;

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

import com.google.inject.Inject;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;

public class AttachmentServiceImpl implements AttachmentService {


    @Inject
    public AttachmentServiceImpl() {
    }
    
    @Override
    public void serveAttachment(String blobId, HttpServletResponse response) throws IOException {
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public void upload(String key, FileItem fileItem,
        InputStream uploadingStream) {
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public void delete(String key) {
        throw new UnsupportedOperationException("TOOD");
    }

    @Override
    public FileItemFactory createFileItemFactory() {
        throw new UnsupportedOperationException("TODO");
    }

}
