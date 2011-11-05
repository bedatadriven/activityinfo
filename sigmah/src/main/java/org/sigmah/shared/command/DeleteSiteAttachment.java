package org.sigmah.shared.command;

import org.sigmah.shared.command.result.VoidResult;

public class DeleteSiteAttachment implements Command<VoidResult> {

	
	private String blobId;
	
	public DeleteSiteAttachment(){
		
	}

	public String getBlobId() {
		return blobId;
	}

	public void setBlobId(String blobId) {
		this.blobId = blobId;
	}
	
}
