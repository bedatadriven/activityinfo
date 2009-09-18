package org.activityinfo.shared.command;

import org.activityinfo.shared.command.result.VoidResult;
import org.activityinfo.shared.dto.UserDatabaseDTO;
import org.activityinfo.shared.dto.UserModel;

import com.extjs.gxt.ui.client.store.Record;

public class UpdateUser implements Command<VoidResult> {

	private int databaseId;
	private UserModel model;

	
	protected UpdateUser() {
		
	}

    public UpdateUser(UserDatabaseDTO db, UserModel model) {
        this(db.getId(), model);
    }
	
	public UpdateUser(int databaseId, UserModel model) {
		this.databaseId = databaseId;
        this.model = model;              

	}

	public int getDatabaseId() {
		return databaseId;
	}

	public void setDatabaseId(int databaseId) {
		this.databaseId = databaseId;
	}

    public UserModel getModel() {
        return model;
    }

    public void setModel(UserModel model) {
        this.model = model;
    }
}
