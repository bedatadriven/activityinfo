/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.shared.auth;

import org.sigmah.shared.dto.AnonymousUser;

import com.google.gwt.i18n.client.LocaleInfo;


/**
 * Encapsulates user identity and their authorization to access the server.
 * 
 * This is normally injected, see the default
 * {@link org.sigmah.client.authentication.ClientSideAuthProvider}
 * 
 */
public class AuthenticatedUser {
	private String authToken;
	private int userId;
	private String userEmail;
	private String userLocale;
	/**
	 * Authentication token.
	 */
	public static final String AUTH_TOKEN_COOKIE = "authToken";
	
	public AuthenticatedUser(String authToken, int userId, String userEmail,
			String userLocale) {
		super();
		this.authToken = authToken;
		this.userId = userId;
		this.userEmail = userEmail;
		this.userLocale = userLocale;
	}
	
	public AuthenticatedUser(String authToken, int userId, String userEmail) {
		super();
		this.authToken = authToken;
		this.userId = userId;
		this.userEmail = userEmail;
		this.userLocale = "en";
	}
	
	public String getAuthToken() {
		return authToken;
	}
	
	public void setAuthToken(String authToken) {
		this.authToken = authToken;
	}
	
	public int getUserId() {
		return userId;
	}
	
	public String getEmail() {
		return userEmail;
	}
	
	public String getUserLocale() {
		return userLocale;
	}

	public int getId() {
		return userId;
	}
	
	public static AuthenticatedUser getAnonymous() {
		return new AuthenticatedUser(AnonymousUser.AUTHTOKEN, 0, AnonymousUser.USER_EMAIL);
	}

	public static AuthenticatedUser getAnonymous(LocaleInfo currentLocale) {
		return new AuthenticatedUser(AnonymousUser.AUTHTOKEN, 0, AnonymousUser.USER_EMAIL, currentLocale.getLocaleName());
	}
}
