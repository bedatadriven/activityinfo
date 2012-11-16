package org.activityinfo.login.shared;


import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("service")
public interface LoginService extends RemoteService {
	AuthenticatedUser login(String email, String password, boolean rememberLogin) throws LoginException;
	void changePassword(String email);
	
}
