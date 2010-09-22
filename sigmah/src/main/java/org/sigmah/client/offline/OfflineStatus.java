/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.offline;

import com.google.gwt.user.client.Cookies;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.sigmah.client.dispatch.remote.Authentication;

import java.sql.Connection;
import java.util.Date;

/**
 * A helper class for testing the off-line status of the application.
 */
@Singleton
public class OfflineStatus {
    private final Connection conn;
    private final String offlineCookieName;

    @Inject
    public OfflineStatus(Connection conn, Authentication auth) {
        this.conn = conn;
        offlineCookieName = auth.getUserId() + "_offline";
    }


    /**
     * Check whether we are running in off-line mode.
     * @return offline status
     */
    public boolean isOfflineEnabled() {
        return "enabled".equals(Cookies.getCookie(offlineCookieName));
    }

    public boolean isOfflineInstalled() {
        return "installed".equals(Cookies.getCookie(offlineCookieName));
    }

    public void setOfflineEnabled(boolean enabled) {
        Date expires = new Date();
        expires.setTime(expires.getTime() + 31536000l);
        Cookies.setCookie(offlineCookieName, enabled ? "enabled" : "installed", expires);
    }

    /**
     *  Remove any cached state.
     */
    public void flushCache() {
        // jest remove our cookies
        Cookies.removeCookie(offlineCookieName);
    }
                           
}
