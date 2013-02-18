package org.activityinfo.server.authentication;

import org.activityinfo.server.database.hibernate.entity.User;
import org.activityinfo.server.endpoint.gwtrpc.CommandServlet;
import org.activityinfo.shared.auth.AuthenticatedUser;
import org.activityinfo.shared.exception.InvalidAuthTokenException;

import com.google.inject.Provider;
import com.google.inject.Singleton;

/**
 * Stores and provides the authentication status for
 * requests. 
 * 
 * <p>This value is initially set for each new request by the 
 * {@link AuthenticationFilter}. It can be overridden in specific cases
 * (see {@link CommandServlet})
 */
@Singleton
public class ServerSideAuthProvider implements Provider<AuthenticatedUser> {

	private static ThreadLocal<AuthenticatedUser> currentUser = new ThreadLocal<AuthenticatedUser>();
	
	@Override
	public AuthenticatedUser get() {
		AuthenticatedUser user = currentUser.get();
		if(user == null) {
			throw new InvalidAuthTokenException("Request is not authenticated");
		}
		return user;
	}
	
	public boolean isAuthenticated() {
		return currentUser.get() != null;
	}
	
	public void set(AuthenticatedUser user) {
		currentUser.set(user);
	}
	
	public void set(User user) {
		set(new AuthenticatedUser("", user.getId(), user.getEmail()));
	}
	
	public void clear() {
		currentUser.set(null);
	}
}
