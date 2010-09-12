/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.offline;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.sigmah.client.dispatch.remote.Authentication;
import org.sigmah.client.inject.DummyConnection;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.user.client.Cookies;
import com.google.inject.Inject;

/**
 * A helper class for testing the off-line status of the application.
 */
public class OfflineStatus {
    private final Connection conn;
    private final Authentication auth;

	
	@Inject
	public OfflineStatus(Connection conn, Authentication auth) {
		this.conn = conn;
		this.auth = auth;
	}

	/**
	 *  Remove any cached state.
	 */
	public void flushCache() {
		// jest remove our cookies
		Cookies.removeCookie(auth.getUserId() + "_offline");
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
	
		String isOffline = Cookies.getCookie(auth.getUserId() + "_offline");
		if ( "true".equals(isOffline)) {
			return true;
			
		} else if ("false".equals(isOffline)){ 
			return false;
			
		} else if (conn instanceof DummyConnection) {
			// No gears connection was found when linking
			return false;
		
		} else {
			// check that we have a sync_regions table available locally
			try {
	            PreparedStatement stmt = conn.prepareStatement("select localVersion from sync_regions where id = ?");
	            stmt.setString(1, "locations");
	            ResultSet rs = stmt.executeQuery();
	            if(rs.next()) {
	            	 // we can run off-line 
	            	Cookies.setCookie(auth.getUserId() + "_offline", "true");
	            	return true;
	            }
	            
	        } catch (SQLException e) {
	        	Log.debug("Failed to read sync_regions from local db.");
	        }
	        
	        // set the off-line cookie to false
	        Cookies.setCookie(auth.getUserId() + "_offline", "false");
			return false;
		}
	}
}
