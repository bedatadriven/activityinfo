package org.sigmah.shared.command;

import org.sigmah.shared.command.result.CommandResult;
import org.sigmah.shared.dto.UserPermissionDTO;
import org.sigmah.shared.dto.allowed.AllowedDTO;

public abstract class AbstractGetCommand<T extends CommandResult> implements GetCommand<T> {

	@Override
	public AllowedDTO isAllowed(UserPermissionDTO userPermission) {
		if (!userPermission.getAllowView()) {
			return denied;
		}

		return allowed;
	}
}