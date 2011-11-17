package org.sigmah.shared.command;

import org.sigmah.shared.command.result.CreateResult;
import org.sigmah.shared.dto.TargetDTO;

/*
 * The user wants to add a Target to a UserDatabase
 */

public class AddTarget implements Command<CreateResult> {
	private int databaseId;
	private TargetDTO targetDTO;

	public AddTarget() {
		super();
	}

	public AddTarget(int databaseId, TargetDTO targetDTO) {
		super();
		this.databaseId = databaseId;
		this.targetDTO = targetDTO;
	}

	public int getDatabaseId() {
		return databaseId;
	}

	public void setDatabaseId(int databaseId) {
		this.databaseId = databaseId;
	}

	public TargetDTO getTargetDTO() {
		return targetDTO;
	}

	public void setTargetDTO(TargetDTO targetDTO) {
		this.targetDTO = targetDTO;
	}
}
