package org.activityinfo.shared.command.result;

import java.util.List;

import org.activityinfo.shared.dto.AdminLevelDTO;

public class AdminLevelResult extends ListResult<AdminLevelDTO> {
	
	public AdminLevelResult() {
	}
	
	public AdminLevelResult(List<AdminLevelDTO> levels) {
		super(levels);
	}
}
