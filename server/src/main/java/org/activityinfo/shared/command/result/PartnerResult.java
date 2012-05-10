package org.activityinfo.shared.command.result;

import java.util.List;

import org.activityinfo.shared.dto.PartnerDTO;

public class PartnerResult extends ListResult<PartnerDTO> {

	public PartnerResult() {
		super();
	}

	public PartnerResult(List<PartnerDTO> data) {
		super(data);
	}
	
	

}
