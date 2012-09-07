package org.activityinfo.shared.command;

import org.activityinfo.shared.command.result.VoidResult;

public class DeleteSiteAttachment implements MutatingCommand<VoidResult> {

	
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
