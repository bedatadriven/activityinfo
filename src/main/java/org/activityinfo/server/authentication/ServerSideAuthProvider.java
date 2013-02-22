package org.activityinfo.server.authentication;

/*
 * #%L
 * ActivityInfo Server
 * %%
 * Copyright (C) 2009 - 2013 UNICEF
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

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
			return AuthenticatedUser.getAnonymous();
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
