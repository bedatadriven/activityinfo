package org.activityinfo.shared.command;

import org.activityinfo.shared.command.result.SiteAttachmentResult;

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
