package org.sigmah.shared.command;

import org.sigmah.shared.command.result.VoidResult;

public class DeleteSiteAttachment implements Command<VoidResult> {

	
	private int attachmentId;
	
	public DeleteSiteAttachment(){
		
	}

	public int getAttachmentId() {
		return attachmentId;
	}

	public void setAttachmentId(int attachmentId) {
		this.attachmentId = attachmentId;
	}
	
	
	
	
}
