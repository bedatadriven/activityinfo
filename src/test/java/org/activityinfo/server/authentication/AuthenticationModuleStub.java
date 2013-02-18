package org.activityinfo.server.authentication;

import org.activityinfo.server.authentication.ServerSideAuthProvider;
import org.activityinfo.shared.auth.AuthenticatedUser;

import com.google.inject.AbstractModule;

public class AuthenticationModuleStub extends AbstractModule {

	public static ServerSideAuthProvider authProvider = new ServerSideAuthProvider();

	public static void setUserId(int userId) {
		switch (userId) {
		case 0:
			authProvider.set(AuthenticatedUser.getAnonymous());
			break;
		default:
			authProvider.set(new AuthenticatedUser("XYZ123", userId,
					"test@test.com"));
		}
	}

	static {
		setUserId(1);
	}

	public static AuthenticatedUser getCurrentUser() {
		return authProvider.get();
	}
	
	@Override
	protected void configure() {
		bind(AuthenticatedUser.class).toProvider(authProvider);
	}
}
