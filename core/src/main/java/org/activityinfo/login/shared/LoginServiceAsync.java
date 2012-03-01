package org.activityinfo.login.shared;

import org.sigmah.shared.auth.AuthenticatedUser;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface LoginServiceAsync {	
	void login(String email, String password, boolean remember, AsyncCallback<AuthenticatedUser> callback);
	void changePassword(String email, AsyncCallback<Void> callback);
}
