/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.offline;

import com.bedatadriven.rebar.persistence.client.ConnectionProvider;
import com.bedatadriven.rebar.sql.client.GearsConnectionFactory;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import org.sigmah.client.dispatch.remote.Authentication;

import java.sql.Connection;
import java.sql.SQLException;

@Singleton
public class LocalConnectionProvider implements Provider<Connection>, ConnectionProvider {

    private final Authentication auth;
    private Connection conn;

    @Inject
    public LocalConnectionProvider(Authentication auth) {
        this.auth = auth;
    }

    public Connection get() {
        if(conn == null) {
            try {
                conn = GearsConnectionFactory.getConnection(auth.getLocalDbName());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return conn;
    }

	@Override
	public Connection getConnection() throws SQLException {
		return get();
	}

	@Override
	public void closeConnection(Connection conn) throws SQLException {
		conn.close();
	}

	@Override
	public void close() {
		if (this.conn != null) {
			try {
				this.conn.close();	
			} catch (Exception e) {
				// ignore
			}
		}
	}
}