package org.activityinfo.shared.command.result;

import java.util.List;

import org.activityinfo.shared.dto.PartnerDTO;

public class OrgUnitListResult extends ListResult<PartnerDTO> {

	public OrgUnitListResult() {
		
		
	}
	
	public OrgUnitListResult(List<PartnerDTO> data) {
		super(data);
	}
	
}
