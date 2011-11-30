package org.sigmah.shared.dto;

import com.google.gwt.user.client.Cookies;

public class AnonymousUser {

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
