package org.activityinfo.client.offline.dao;

import org.activityinfo.client.dispatch.remote.Authentication;

import java.sql.*;

/**
 * Data Access Object for the Authentication Database, which
 * is a local database containing a list of all users with offline data
 */
public class AuthDAO {

    public AuthDAO() {
    }

    private Connection openOrCreateDatabase() throws SQLException {

        Connection conn = DriverManager.getConnection("jdbc:sqlite:users");
        Statement ddl = conn.createStatement();
        ddl.execute("create table if not exists users (id integer primary key autoincrement, " +
                " Email varchar(75) unique, AuthToken varchar(32))");
        ddl.close();

        return conn;
    }

    /**
     * Updates a user's authentication token if it exists, or else creates a new
     * record for this user.
     *
     * @param auth The authentication data to store
     */
    public void updateOrInsert(Authentication auth) throws SQLException {

        Connection conn = openOrCreateDatabase();

        try {
            // Try to update an existing entry with this AuthToken
            PreparedStatement update = conn.prepareStatement("update users set authToken = ? where id = ?");
            update.setString(1, auth.getAuthToken());
            update.setString(2, auth.getEmail());
            int rowsUpdated = update.executeUpdate();

            if (rowsUpdated == 0) {
                PreparedStatement insert = conn.prepareStatement("insert into users (id, email, authToken) values (?, ?, ?)");
                insert.setInt(1, auth.getUserId());
                insert.setString(2, auth.getEmail());
                insert.setString(3, auth.getAuthToken());
                insert.executeUpdate();
            }
        } finally {
            conn.close();
        }
    }

}
