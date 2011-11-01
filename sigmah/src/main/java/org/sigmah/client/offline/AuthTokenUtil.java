/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.offline;

import java.util.Date;

import org.sigmah.shared.auth.AuthenticatedUser;

import com.google.gwt.user.client.Cookies;

public class AuthTokenUtil {

    private static final long ONE_YEAR = 365l * 24l * 60l * 60l * 1000l;


    public static void ensurePersistentCookie(AuthenticatedUser auth) {
        // unless the user requests to stay logged in, the authToken is
        // set to expire at the end of the user's session, which
        // means that it won't be available if the user opens the app via
        // the appcache later on.
        // Since BootstrapScriptServlet relies on the token to select the
        // appropriate locale, without the cookie set, trying to retrieve
        // the latest manifest will fail
        Cookies.setCookie(org.sigmah.shared.Cookies.AUTH_TOKEN_COOKIE, auth.getAuthToken(), oneYearLater()  );
    }

    private static Date oneYearLater() {
        long time = new Date().getTime();
        return new Date(time + ONE_YEAR);
    }

}
