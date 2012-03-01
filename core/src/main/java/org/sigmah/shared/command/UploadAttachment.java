package org.sigmah.shared.command;

import org.sigmah.shared.command.UploadAttachment.UploadAttachmentResult;
import org.sigmah.shared.command.result.CommandResult;
import org.sigmah.shared.dto.AttachmentDTO;

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
