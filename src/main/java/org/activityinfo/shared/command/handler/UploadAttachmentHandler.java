package org.activityinfo.shared.command.handler;

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

import org.activityinfo.shared.command.UploadAttachment;
import org.activityinfo.shared.command.UploadAttachment.UploadAttachmentResult;
import org.activityinfo.shared.dto.AttachmentDTO;

import com.bedatadriven.rebar.sql.client.query.SqlInsert;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * 1. Client uploads image to elasticbeanstalk s3 bucket 2. Client adds
 * attachment to the database
 */
public class UploadAttachmentHandler implements
    CommandHandlerAsync<UploadAttachment, UploadAttachmentResult> {
    @Override
    public void execute(UploadAttachment command, ExecutionContext context,
        AsyncCallback<UploadAttachmentResult> callback) {
        AttachmentDTO attachment = command.getAttachment();
        SqlInsert.insertInto("Attachment")
            .value("attachmentId", attachment.getId())
            .value("createdDate", attachment.getCreatedDate())
            .value("sizeInKb", attachment.getSizeInKb())
            .value("extension", attachment.getExtension())
            .value("siteId", attachment.getSiteId())

            .execute(context.getTransaction());
    }

}
