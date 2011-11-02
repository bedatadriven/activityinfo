package org.sigmah.server.auth;

import org.sigmah.shared.auth.AuthenticatedUser;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;

public class AuthenticationModuleStub extends AbstractModule {

	public static AuthenticatedUser currentUser;
	
	public static void setUserId(int userId) {
		currentUser = new AuthenticatedUser(userId, "XYZ123", "test@test.com");
	}
	
	static {
		setUserId(1);
	}
	
	
	@Override
	protected void configure() {
	}
	
	@Provides
	public AuthenticatedUser provideAuthenticatedUser() {
		return currentUser;
	}
}
