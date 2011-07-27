package org.sigmah.shared.command.result;

import java.util.List;

import org.sigmah.shared.dto.PartnerDTO;

public class OrgUnitListResult extends ListResult<PartnerDTO> {

	public OrgUnitListResult() {
		
		
	}
	
	public OrgUnitListResult(List<PartnerDTO> data) {
		super(data);
	}
	
}
