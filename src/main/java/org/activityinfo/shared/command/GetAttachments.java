package org.activityinfo.shared.command;

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
