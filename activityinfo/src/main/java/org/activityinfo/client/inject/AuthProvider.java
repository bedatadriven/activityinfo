package org.activityinfo.client.inject;

import org.activityinfo.client.command.Authentication;

import com.google.inject.Provider;
import com.google.gwt.i18n.client.Dictionary;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.core.client.GWT;
/*
 * @author Alex Bertram
 */

public class AuthProvider implements Provider<Authentication> {

    public Authentication get() {

        if(Cookies.getCookie("authToken") != null &&
           Cookies.getCookie("email") != null) {

            return new Authentication(Cookies.getCookie("authToken"),
                                      Cookies.getCookie("email"));

        }

        Dictionary userInfo;
        try {
            userInfo = Dictionary.getDictionary("UserInfo");
            return new Authentication(
                    userInfo.get("authToken"),
                    userInfo.get("email"));

        } catch (Exception e) {
            GWT.log("DictionaryAuthenticationProvider: exception retrieving dictionary from page", e);
            throw new Error();
        }
    }
}
