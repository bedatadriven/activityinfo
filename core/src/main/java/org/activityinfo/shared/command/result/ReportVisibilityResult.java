package org.activityinfo.shared.command.result;

import java.util.List;

import org.activityinfo.shared.dto.ReportVisibilityDTO;

public class ReportVisibilityResult implements CommandResult {

	private List<ReportVisibilityDTO> list;
	
	public ReportVisibilityResult() {
		
	}

	public ReportVisibilityResult(List<ReportVisibilityDTO> list) {
		super();
		this.list = list;
	}

	public List<ReportVisibilityDTO> getList() {
		return list;
	}

	public void setList(List<ReportVisibilityDTO> list) {
		this.list = list;
	}
	
	
}
