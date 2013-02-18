/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.client.authentication;

import java.util.Date;

import org.activityinfo.shared.auth.AuthenticatedUser;
import org.activityinfo.shared.exception.InvalidAuthTokenException;

import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.user.client.Cookies;
import com.google.inject.Provider;

/**
 * Supplies user <code>Authentication</code> information from
 * the <code>authToken</code> and <code>email</code> cookies, or
 * failing that, from the <code>UserInfo</code> dictionary.
 *
 * @author Alex Bertram
 */
public class ClientSideAuthProvider implements Provider<AuthenticatedUser> {

    private static final long ONE_YEAR = 365l * 24l * 60l * 60l * 1000l;
	
    public AuthenticatedUser get() {

    	String authToken = Cookies.getCookie(AuthenticatedUser.AUTH_TOKEN_COOKIE);
    	String userId = Cookies.getCookie(AuthenticatedUser.USER_ID_COOKIE) ;
    	String email = Cookies.getCookie(AuthenticatedUser.EMAIL_COOKIE);
    	
        if (authToken != null && userId != null && email != null) {

            return new AuthenticatedUser(
            		authToken,
            		Integer.parseInt(userId),
            		email.replaceAll("\"", ""),
                    currentLocale());

        }

       	throw new InvalidAuthTokenException("Request is not authenticated");
    }
    
    private String currentLocale() {
    	return LocaleInfo.getCurrentLocale().getLocaleName();
    }
    
    public static void ensurePersisted() {
        // unless the user requests to stay logged in, the authToken is
        // set to expire at the end of the user's session, which
        // means that it won't be available if the user opens the app via
        // the appcache later on.
        // Since BootstrapScriptServlet relies on the token to select the
        // appropriate locale, without the cookie set, trying to retrieve
        // the latest manifest will fail
    	
    	AuthenticatedUser user = new ClientSideAuthProvider().get();

    	Cookies.setCookie(AuthenticatedUser.AUTH_TOKEN_COOKIE, user.getAuthToken(), oneYearLater());
    	Cookies.setCookie(AuthenticatedUser.USER_ID_COOKIE, Integer.toString(user.getUserId()), oneYearLater());
    	Cookies.setCookie(AuthenticatedUser.EMAIL_COOKIE, user.getEmail(), oneYearLater());
    }
    
    private static Date oneYearLater() {
        long time = new Date().getTime();
        return new Date(time + ONE_YEAR);
    }
}
