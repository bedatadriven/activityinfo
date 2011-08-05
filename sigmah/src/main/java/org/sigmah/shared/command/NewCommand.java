package org.sigmah.shared.command;

import org.sigmah.shared.command.result.CommandResult;
import org.sigmah.shared.dto.UserPermissionDTO;
import org.sigmah.shared.dto.allowed.AllowedDTO;
import org.sigmah.shared.dto.allowed.DeniedDTO;
import org.sigmah.shared.dto.allowed.IsAllowedDTO;

public interface NewCommand<T extends CommandResult> {
	public AllowedDTO isAllowed(UserPermissionDTO userpermission);
	
	public static IsAllowedDTO allowed = new IsAllowedDTO();
	public static DeniedDTO denied = new DeniedDTO();
}
