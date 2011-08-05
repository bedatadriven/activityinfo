package org.sigmah.shared.dto.allowed;

public class IsAllowedDTO implements AllowedDTO {
	private String reason;

	@Override
	public boolean isAllowed() {
		return true;
	}

	@Override
	public String getReason() {
		return "You're just too cool to be denied";
	}
}