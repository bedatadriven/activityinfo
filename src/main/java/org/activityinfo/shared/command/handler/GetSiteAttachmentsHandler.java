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

import java.util.ArrayList;
import java.util.List;

import org.activityinfo.shared.command.GetSiteAttachments;
import org.activityinfo.shared.command.result.SiteAttachmentResult;
import org.activityinfo.shared.db.Tables;
import org.activityinfo.shared.dto.SiteAttachmentDTO;

import com.bedatadriven.rebar.sql.client.SqlResultCallback;
import com.bedatadriven.rebar.sql.client.SqlResultSet;
import com.bedatadriven.rebar.sql.client.SqlResultSetRow;
import com.bedatadriven.rebar.sql.client.SqlTransaction;
import com.bedatadriven.rebar.sql.client.query.SqlQuery;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class GetSiteAttachmentsHandler implements
    CommandHandlerAsync<GetSiteAttachments, SiteAttachmentResult> {

    private List<SiteAttachmentDTO> dtos;

    @Override
    public void execute(GetSiteAttachments command, ExecutionContext context,
        final AsyncCallback<SiteAttachmentResult> callback) {

        dtos = new ArrayList<SiteAttachmentDTO>();

        SqlQuery.selectAll().from(Tables.SITE_ATTACHMENT, "s")
            .where("s.siteid").equalTo(command.getSiteId())
            .execute(context.getTransaction(), new SqlResultCallback() {
                @Override
                public void onSuccess(SqlTransaction tx, SqlResultSet results) {
                    for (SqlResultSetRow row : results.getRows()) {

                        SiteAttachmentDTO dto = new SiteAttachmentDTO();

                        dto.setSiteId(row.getInt("siteid"));
                        dto.setBlobId(row.getString("blobid"));
                        dto.setFileName(row.getString("filename"));
                        dto.setUploadedBy(row.getInt("uploadedby"));
                        dto.setBlobSize(row.getInt("blobsize"));
                        dto.setContentType(row.getString("contenttype"));

                        dtos.add(dto);
                    }
                    callback.onSuccess(new SiteAttachmentResult(dtos));
                }
            });
    }
}
