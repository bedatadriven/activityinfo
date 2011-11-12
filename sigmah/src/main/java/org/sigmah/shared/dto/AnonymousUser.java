package org.sigmah.shared.dto;

import com.google.gwt.user.client.Cookies;

public class AnonymousUser {

	public static String AUTHTOKEN = "AnonymousUser_Authentication_Token";
	public static int USER_ID = 0;
	public static String USER_EMAIL = "AnonymousUser@activityinfo.com";

	private AnonymousUser() {

	}

	public static void createCookiesForAnonymousUser() {
		Cookies.setCookie("authToken", AUTHTOKEN);
		Cookies.setCookie("userId", String.valueOf(USER_ID));
		Cookies.setCookie("email", USER_EMAIL);	
	}
	
}
