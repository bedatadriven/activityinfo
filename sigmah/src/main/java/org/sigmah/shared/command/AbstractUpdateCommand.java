package org.sigmah.shared.command;

import org.sigmah.shared.command.result.CommandResult;
import org.sigmah.shared.dto.UserPermissionDTO;
import org.sigmah.shared.dto.allowed.AllowedDTO;

public abstract class AbstractUpdateCommand<T extends CommandResult> implements NewCommand<T> {

	@Override
	public AllowedDTO isAllowed(UserPermissionDTO userpermission) {
		if (userpermission.getAllowEdit()) {
			return denied;
		}
		
		return allowed;
	}

}
