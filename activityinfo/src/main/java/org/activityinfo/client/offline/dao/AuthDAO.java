package org.activityinfo.client.offline.dao;

import com.google.gwt.gears.client.Factory;
import com.google.gwt.gears.client.database.Database;
import com.google.gwt.gears.client.database.DatabaseException;
import com.google.gwt.gears.client.database.ResultSet;
import org.activityinfo.client.dispatch.remote.Authentication;

/**
 * Data Access Object
 */
public class AuthDAO {

    public AuthDAO() {


    }

    private Database openOrCreateDatabase() throws DatabaseException {

        Database db;

        db = Factory.getInstance().createDatabase();
        db.open("users");
        // The 'int' type will store up to 8 byte ints depending on the magnitude of the
        // value added.
        db.execute("create table if not exists users (id integer primary key autoincrement, " +
                " Email varchar(75) unique, AuthToken varchar(32))");

        return db;
    }


    /**
     * Updates a user's authentication token if it exists, or else creates a new
     * record for this user.
     *
     * @param auth The authentication data to store
     * @return The user's local id
     * @throws com.google.gwt.gears.client.database.DatabaseException
     *          if an underlying database operation fails.
     */
    public int updateOrInsert(Authentication auth) throws DatabaseException {

        Database authDb = openOrCreateDatabase();

        try {
            // Try to update an existing entry with this AuthToken
            authDb.execute("update users set authToken = ? where email = ?", auth.getAuthToken(), auth.getEmail());
            if (authDb.getRowsAffected() == 0) {
                // New User: insert record
                authDb.execute("insert into users (email, authToken) values (?, ?)", auth.getEmail(), auth.getAuthToken());
                return authDb.getLastInsertRowId();
            } else {
                // Existing record: just get local user id that we use to identify the database
                ResultSet rs = authDb.execute("select id from users where email = ?", auth.getEmail());
                return rs.getFieldAsInt(0);
            }
        } finally {
            authDb.close();
        }
    }

}
