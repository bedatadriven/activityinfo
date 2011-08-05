package org.sigmah.shared.dto.allowed;

/*
 * A command unable to determine if the user can perform the command clientside
 */
public class ServerAllowedDTO implements AllowedDTO {
	private String reason;

	public ServerAllowedDTO() {
		this.reason = "The client cannot determine if you are allowed to do this";
	}

	@Override
	public boolean isAllowed() {
		return true;
	}

	@Override
	public String getReason() {
		return reason;
	}
}
