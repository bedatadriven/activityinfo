package org.sigmah.shared.command;

import org.sigmah.shared.command.result.CommandResult;
import org.sigmah.shared.dto.UserPermissionDTO;
import org.sigmah.shared.dto.allowed.AllowedDTO;

public abstract class AbstractCommand<T extends CommandResult> implements
		NewCommand<T> {

	/*
	 *  By default, any command is not allowed by any user. This forces downstream implementors
	 *  to explicitly hand permission to the user wishing to perform the command.
	 */
	@Override
	public AllowedDTO isAllowed(UserPermissionDTO userpermission) {
		return denied;
	}
	
}
