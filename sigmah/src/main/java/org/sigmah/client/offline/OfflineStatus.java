/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.offline;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.user.client.Cookies;
import com.google.inject.Inject;
import org.sigmah.client.dispatch.remote.Authentication;
import org.sigmah.client.inject.DummyConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * A helper class for testing the off-line status of the application.
 */
public class OfflineStatus {
    private final Connection conn;
    private final String offlineCookieName;

	@Inject
	public OfflineStatus(Connection conn, Authentication auth) {
		this.conn = conn;
        offlineCookieName = auth.getUserId() + "_offline";
    }

	/**
	 *  Remove any cached state.
	 */
	public void flushCache() {
		// jest remove our cookies
		Cookies.removeCookie(offlineCookieName);
	}

	/**
	 * Check whether we are running in off-line mode.
	 * @return offline status
	 */
	public boolean isOfflineEnabled() {
		/*
		 * Check for offline status as follows:
		 * 	1.) look for a cookie
		 *  3.) check that gears is enabled
		 *  4.) check the database
		 */

		String isOffline = Cookies.getCookie(offlineCookieName);
        Log.debug("OfflineStatus: Cookie " + offlineCookieName + " =  " + isOffline);
		if ( "true".equals(isOffline)) {
			return true;

		} else if ("false".equals(isOffline)){
			return false;

		} else if (conn instanceof DummyConnection) {
			// No gears connection was found when linking
            Log.debug("OfflineStatus: couldn't get gears, we have dummy connection");
			return false;

		} else {
			// check that we have a sync_regions table available locally
			try {
	            PreparedStatement stmt = conn.prepareStatement("select localVersion from sync_regions where id = ?");
	            stmt.setString(1, "schema");
	            ResultSet rs = stmt.executeQuery();
	            if(rs.next()) {
	            	 // we can run off-line
	            	Cookies.setCookie(offlineCookieName, "true");
	            	return true;
	            } else {
                    Log.error("Offline status: could query database but no schema version found, enable=false");
                    return false;
                }

	        } catch (SQLException e) {
	        	Log.debug("Failed to read sync_regions from local db.");
	        }

	        // set the off-line cookie to false
	        Cookies.setCookie(offlineCookieName, "false");
			return false;
		}
	}
}
