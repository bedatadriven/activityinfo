package org.activityinfo.login.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("service")
public interface LoginService extends RemoteService {
	void login(String email, String password, boolean rememberLogin) throws LoginException;
	void changePassword(String email);
	
}
