package org.activityinfo.login.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface LoginServiceAsync {	
	void login(String email, String password, boolean remember, AsyncCallback<Void> callback);
	void changePassword(String email, AsyncCallback<Void> callback);
}
