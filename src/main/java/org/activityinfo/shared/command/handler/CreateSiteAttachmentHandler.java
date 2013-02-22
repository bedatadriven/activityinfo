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

import org.activityinfo.shared.command.CreateSiteAttachment;
import org.activityinfo.shared.command.result.VoidResult;

import com.bedatadriven.rebar.sql.client.query.SqlInsert;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class CreateSiteAttachmentHandler implements
    CommandHandlerAsync<CreateSiteAttachment, VoidResult> {

    @Override
    public void execute(CreateSiteAttachment command, ExecutionContext context,
        AsyncCallback<VoidResult> callback) {

        SqlInsert.insertInto("siteattachment")
            .value("siteid", command.getSiteId())
            .value("blobid", command.getBlobId())
            .value("filename", command.getFileName())
            .value("uploadedby", context.getUser().getUserId())
            .value("blobsize", command.getBlobSize())
            .value("contenttype", command.getContentType())
            .execute(context.getTransaction());

        callback.onSuccess(new VoidResult());
    }

}
