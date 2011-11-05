package org.sigmah.shared.command;

import org.sigmah.shared.command.result.SiteAttachmentResult;

public class GetSiteAttachments implements Command<SiteAttachmentResult> {

	private int siteId;

	public GetSiteAttachments() {

	}

	public int getSiteId() {
		return siteId;
	}

	public void setSiteId(int siteId) {
		this.siteId = siteId;
	}

	
	
}
