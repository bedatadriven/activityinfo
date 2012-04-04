package org.sigmah.shared.command.result;

import java.util.List;


public class IndicatorLinkResult implements CommandResult {
	private List<IndicatorLink> links;

	public IndicatorLinkResult() {
		super();
	}

	public IndicatorLinkResult(List<IndicatorLink> links) {
		super();
		this.links = links;
	}

	public List<IndicatorLink> getLinks() {
		return links;
	}

	public void setLinks(List<IndicatorLink> links) {
		this.links = links;
	}

	
	
}
