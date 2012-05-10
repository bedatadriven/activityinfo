package org.activityinfo.shared.command;

import org.activityinfo.shared.command.UploadAttachment.UploadAttachmentResult;
import org.activityinfo.shared.command.result.CommandResult;
import org.activityinfo.shared.dto.AttachmentDTO;

public class UploadAttachment implements Command<UploadAttachmentResult> {
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
