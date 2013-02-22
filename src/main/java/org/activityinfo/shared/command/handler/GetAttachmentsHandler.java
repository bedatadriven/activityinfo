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

import java.util.List;

import org.activityinfo.shared.command.GetAttachments;
import org.activityinfo.shared.command.GetAttachments.GetAttachmentsResult;
import org.activityinfo.shared.dto.AttachmentDTO;

import com.bedatadriven.rebar.sql.client.SqlResultCallback;
import com.bedatadriven.rebar.sql.client.SqlResultSet;
import com.bedatadriven.rebar.sql.client.SqlResultSetRow;
import com.bedatadriven.rebar.sql.client.SqlTransaction;
import com.bedatadriven.rebar.sql.client.query.SqlQuery;
import com.google.common.collect.Lists;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class GetAttachmentsHandler implements
    CommandHandlerAsync<GetAttachments, GetAttachmentsResult> {
    @Override
    public void execute(final GetAttachments command, ExecutionContext context,
        final AsyncCallback<GetAttachmentsResult> callback) {
        SqlQuery.selectAll()
            .from("attachment")
            .where("SiteId")
            .equalTo(command.getSiteId())

            .execute(context.getTransaction(), new SqlResultCallback() {
                @Override
                public void onSuccess(SqlTransaction tx, SqlResultSet results) {
                    List<AttachmentDTO> attachments = Lists.newArrayList();
                    for (SqlResultSetRow row : results.getRows()) {
                        AttachmentDTO attachment = new AttachmentDTO();
                        attachment.setId(row.getInt("AttachmentId"));
                        attachment.setExtension(row.getString("extension"));
                        attachment.setCreatedDate(row.getDate("createdDate"));
                        attachment.setName(row.getString("name"));
                        attachment.setSizeInKb(row.getInt("sizeInKb"));
                        attachment.setSiteId(command.getSiteId());
                        attachments.add(attachment);
                    }
                    callback.onSuccess(new GetAttachmentsResult(attachments));
                }
            });
    }
}
