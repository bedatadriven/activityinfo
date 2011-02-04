/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.offline;

import com.bedatadriven.rebar.appcache.client.AppCache;
import com.bedatadriven.rebar.appcache.client.AppCacheFactory;
import com.google.gwt.user.client.Cookies;
import org.sigmah.client.dispatch.remote.Authentication;

import java.util.Date;

public class AuthTokenUtil {

    private static final long ONE_YEAR = 365l * 24l * 60l * 60l * 1000l;

    public static void maybeEnsurePersistentCookie(Authentication auth) {
        // this is a defensive move to check that, if the app has been loaded
        // from that the cache, that the authToken is indeed present as a cookie
        // otherwise the browser will not be able to load the manifest

        AppCache appCache = AppCacheFactory.get();
        if(appCache.getStatus() != AppCache.Status.UNCACHED &&
           appCache.getStatus() != AppCache.Status.UNSUPPORTED) {

            ensurePersistentCookie(auth);
        }
    }

    public static void ensurePersistentCookie(Authentication auth) {
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
