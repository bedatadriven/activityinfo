package org.sigmah.shared.command.result;

import java.util.List;

import org.sigmah.shared.dto.UserDTO;

/**
 * Organization users result list.
 * 
 * @author nrebiai
 * 
 */
public class UserListResult implements CommandResult {


	private static final long serialVersionUID = -6604376432667887721L;
	private List<UserDTO> list;
	
	public UserListResult(){
		
	}
	
	public UserListResult(List<UserDTO> list) {
        this.list = list;
    }

    public List<UserDTO> getList() {
        return list;
    }

    public void setList(List<UserDTO> list) {
        this.list = list;
    }
}
