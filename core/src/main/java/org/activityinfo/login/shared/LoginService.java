package org.activityinfo.login.shared;

import org.activityinfo.shared.auth.AuthenticatedUser;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("service")
public interface LoginService extends RemoteService {
	AuthenticatedUser login(String email, String password, boolean rememberLogin) throws LoginException;
	void changePassword(String email);
	
}
