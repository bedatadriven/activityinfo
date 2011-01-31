package org.sigmah.shared.command.result;

import java.util.List;

import org.sigmah.shared.dto.profile.ProfileDTO;
import org.sigmah.shared.dto.profile.ProfileDTOLight;



public class ProfileListResult implements CommandResult {
	private static final long serialVersionUID = -6604376432667887721L;
	private List<ProfileDTOLight> list;
	
	public ProfileListResult(){
		
	}
	
	public ProfileListResult(List<ProfileDTOLight> list) {
        this.list = list;
    }

    public List<ProfileDTOLight> getList() {
        return list;
    }

    public void setList(List<ProfileDTOLight> list) {
        this.list = list;
    }
}
