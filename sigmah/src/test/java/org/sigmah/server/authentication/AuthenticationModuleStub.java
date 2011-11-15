package org.sigmah.server.authentication;

import org.sigmah.shared.auth.AuthenticatedUser;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;

public class AuthenticationModuleStub extends AbstractModule {

	public static AuthenticatedUser currentUser;

	public static void setUserId(int userId) {
		switch (userId) {
		case 0:
			setAnonymouseUser();
			break;
		default:
			currentUser = new AuthenticatedUser(userId, "XYZ123",
					"test@test.com");
		}

	}

	public static void setAnonymouseUser() {
		currentUser = new AuthenticatedUser(0,
				"AnonymousUser_Authentication_Token",
				"AnonymousUser@activityinfo.com");
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
