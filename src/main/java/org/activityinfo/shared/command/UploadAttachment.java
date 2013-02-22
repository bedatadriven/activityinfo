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

import org.activityinfo.shared.command.UploadAttachment.UploadAttachmentResult;
import org.activityinfo.shared.command.result.CommandResult;
import org.activityinfo.shared.dto.AttachmentDTO;

public class UploadAttachment implements
    MutatingCommand<UploadAttachmentResult> {
    private AttachmentDTO attachment;

    public UploadAttachment(AttachmentDTO attachment) {
        this.attachment = attachment;
    }

    public UploadAttachment() {
    }

    public AttachmentDTO getAttachment() {
        return attachment;
    }

    public static class UploadAttachmentResult implements CommandResult {
        private int id;

        public UploadAttachmentResult() {
        }

        public UploadAttachmentResult(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }
    }
}
