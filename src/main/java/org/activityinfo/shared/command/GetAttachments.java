package org.activityinfo.shared.command;

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

import org.activityinfo.shared.command.GetAttachments.GetAttachmentsResult;
import org.activityinfo.shared.command.result.CommandResult;
import org.activityinfo.shared.dto.AttachmentDTO;

import com.google.common.collect.Lists;

public class GetAttachments implements Command<GetAttachmentsResult> {
    private int siteId;

    public int getSiteId() {
        return siteId;
    }

    public GetAttachments(int siteId) {
        this.siteId = siteId;
    }

    public GetAttachments() {
    }

    public static class GetAttachmentsResult implements CommandResult {
        private List<AttachmentDTO> attachments = Lists.newArrayList();

        public GetAttachmentsResult() {
        }

        public GetAttachmentsResult(List<AttachmentDTO> attachments) {
            this.attachments = attachments;
        }

        public List<AttachmentDTO> getAttachments() {
            return attachments;
        }
    }
}
