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

	public boolean isOfflineEnabled() {
		String isOffline = Cookies.getCookie(auth.getUserId() + "_offline");

		if ( isOffline != null && "true".equals(isOffline)) {
			return true;
		} else {
			try {
	            PreparedStatement stmt = conn.prepareStatement("select localVersion from sync_regions where id = ?");
	            stmt.setString(1, "locations");
	            ResultSet rs = stmt.executeQuery();
	            if(rs.next()) {
	            	// set this cookie for one day
	                java.util.Date now = new java.util.Date();
	                Date expires = new Date(now.getTime() * 1000 * 60 * 60 * 24);
	            	Cookies.setCookie(auth.getUserId() + "_offline", "true", expires);
	            }
	        } catch (SQLException e) {
	        	Log.debug("Failed to read sync_regions from local db.");
	        }
		}
		return false;
	}

}
