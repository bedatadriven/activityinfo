package org.sigmah.shared.dto.allowed;

public class DeniedDTO implements AllowedDTO {
	private String reason;
	
	public DeniedDTO() {
		this.reason = "No access denied reason given";
	}

	public DeniedDTO(String reason) {
		this.reason = reason;
	}

	@Override
	public boolean isAllowed() {
		return false;
	}

	@Override
	public String getReason() {
		return reason;
	}
}