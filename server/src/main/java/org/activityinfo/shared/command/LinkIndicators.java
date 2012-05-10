package org.activityinfo.shared.command;

import org.activityinfo.shared.command.result.VoidResult;

public class LinkIndicators implements Command<VoidResult> {
	private boolean link;
	private int sourceIndicatorId;
	private int destIndicatorId;
	
	public LinkIndicators() {
		super();
	}

	public boolean isLink() {
		return link;
	}

	public void setLink(boolean link) {
		this.link = link;
	}

	public int getSourceIndicatorId() {
		return sourceIndicatorId;
	}

	public void setSourceIndicatorId(int sourceIndicatorId) {
		this.sourceIndicatorId = sourceIndicatorId;
	}

	public int getDestIndicatorId() {
		return destIndicatorId;
	}

	public void setDestIndicatorId(int destIndicatorId) {
		this.destIndicatorId = destIndicatorId;
	}
	
	
}
