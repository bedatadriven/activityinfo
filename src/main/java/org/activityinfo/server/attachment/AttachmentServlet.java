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

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.activityinfo.server.command.DispatcherSync;
import org.activityinfo.server.database.hibernate.entity.SiteAttachment;
import org.activityinfo.shared.command.CreateSiteAttachment;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

@Singleton
public class AttachmentServlet extends HttpServlet {

    private AttachmentService service;
    private DispatcherSync dispatcher;
    private Provider<EntityManager> entityManager;

    private static final Logger LOGGER = Logger
        .getLogger(AttachmentServlet.class.getName());

    @Inject
    public AttachmentServlet(AttachmentService service,
        Provider<EntityManager> entityManager,
        DispatcherSync dispatcher) {
        this.service = service;
        this.entityManager = entityManager;
        this.dispatcher = dispatcher;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException {

        String key = req.getParameter("blobId");
        SiteAttachment attachment = entityManager.get().find(
            SiteAttachment.class, key);

        resp.setHeader("Content-Disposition", "attachment; filename=\""
            + attachment.getFileName() + "\"");
        resp.setContentType(attachment.getContentType());

        service.serveAttachment(key, resp);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

        try {
            String key = request.getParameter("blobId");
            Integer siteId = Integer.valueOf(request.getParameter("siteId"));

            FileItem fileItem = getFirstUploadFile(request);

            String fileName = fileItem.getName();
            InputStream uploadingStream = fileItem.getInputStream();

            service.upload(key, fileItem, uploadingStream);

            CreateSiteAttachment siteAttachment = new CreateSiteAttachment();
            siteAttachment.setSiteId(siteId);
            siteAttachment.setBlobId(key);
            siteAttachment.setFileName(fileName);
            siteAttachment.setBlobSize(fileItem.getSize());
            siteAttachment.setContentType(fileItem.getContentType());

            dispatcher.execute(siteAttachment);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error handling upload", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private FileItem getFirstUploadFile(HttpServletRequest request)
        throws FileUploadException {
        boolean isMultipart = ServletFileUpload.isMultipartContent(request);
        if (isMultipart) {

            FileItemFactory factory = service.createFileItemFactory();
            ServletFileUpload upload = new ServletFileUpload(factory);

            List<FileItem> items = upload.parseRequest(request);

            for (FileItem item : items) {
                return item;
            }
        }
        throw new RuntimeException("No upload provided");
    }
}