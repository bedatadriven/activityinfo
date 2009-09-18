package org.activityinfo.shared.command.result;

import org.activityinfo.shared.command.result.CommandResult;

public class CreateResult implements CommandResult {

	private int newId;

	protected CreateResult() {
		
	}
		
	public CreateResult(int newId) {
		this.newId = newId;
	}
	
	public int getNewId() {
		return newId;
	}

	public void setNewId(int newId) {
		this.newId = newId;
	}

}
