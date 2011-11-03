/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.authentication;

import org.sigmah.shared.auth.AuthenticatedUser;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.i18n.client.Dictionary;
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

    public AuthenticatedUser get() {

        if (Cookies.getCookie("authToken") != null &&
                Cookies.getCookie("userId") != null &&
                Cookies.getCookie("email") != null) {

            return new AuthenticatedUser(
                    Cookies.getCookie("authToken"),
            		Integer.parseInt(Cookies.getCookie("userId")),
                    Cookies.getCookie("email"),
                    currentLocale());

        }

        Dictionary userInfo;
        try {
            userInfo = Dictionary.getDictionary("UserInfo");
            return new AuthenticatedUser(
                    userInfo.get("authToken"),
            		Integer.parseInt(userInfo.get("userId")),
                    userInfo.get("email"),
                    currentLocale());
        } catch (Exception e) {
            Log.fatal("DictionaryAuthenticationProvider: exception retrieving dictionary from page", e);
            throw new RuntimeException("Cannot retrieve user dictionary from page", e);
        }
    }
    
    private String currentLocale() {
    	return LocaleInfo.getCurrentLocale().getLocaleName();
    }
}
