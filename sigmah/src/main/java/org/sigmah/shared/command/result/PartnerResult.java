package org.sigmah.shared.command.result;

import java.util.List;

import org.sigmah.shared.dto.PartnerDTO;

public class PartnerResult extends ListResult<PartnerDTO> {

	public PartnerResult() {
		super();
	}

	public PartnerResult(List<PartnerDTO> data) {
		super(data);
	}
	
	

}
