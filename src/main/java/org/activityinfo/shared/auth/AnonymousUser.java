package org.activityinfo.shared.auth;

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


import com.google.gwt.user.client.Cookies;

public final class AnonymousUser {

	public static final String AUTHTOKEN = "AnonymousUser";
	public static final int USER_ID = 0;
	public static final String USER_EMAIL = "AnonymousUser@activityinfo.org";

	private AnonymousUser() {

	}

	public static void createCookiesForAnonymousUser() {
		Cookies.setCookie("authToken", AUTHTOKEN);
		Cookies.setCookie("userId", String.valueOf(USER_ID));
		Cookies.setCookie("email", USER_EMAIL);	
	}
	
}
