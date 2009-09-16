package org.activityinfo.client.offline.dao;

import com.google.gwt.gears.client.database.Database;
import com.google.gwt.gears.client.database.DatabaseException;
import com.google.gwt.gears.client.database.ResultSet;
import com.google.gwt.gears.client.Factory;
import com.google.gwt.gears.client.GearsException;
import com.google.gwt.core.client.GWT;

import org.activityinfo.client.command.Authentication;

public class AuthDAO {

    private Database authDb;

    public AuthDAO() {


    }

    private void openOrCreateDatabase() throws DatabaseException {

        Database db;

        db = Factory.getInstance().createDatabase();
        db.open("users");
        // The 'int' type will store up to 8 byte ints depending on the magnitude of the
        // value added.
        db.execute("create table if not exists users (id integer primary key autoincrement, " +
                " Email varchar(75) unique, AuthToken varchar(32))");

        authDb = db;
    }


    /**
     * Updates a user's authentication token if it exists, or else creates a new
     * record for this user.
     *
     * @param auth
     * @return The user's local id
     */
    public int updateOrInsert(Authentication auth) throws DatabaseException {

        if(authDb == null)
            openOrCreateDatabase();

        // Try to update an existing entry with this AuthToken
        authDb.execute("update users set authToken = ? where email = ?", auth.getAuthToken(), auth.getEmail());
        if(authDb.getRowsAffected() == 0) {
            // New User: insert record
            authDb.execute("insert into users (email, authToken) values (?, ?)", auth.getAuthToken(), auth.getEmail());
            return authDb.getLastInsertRowId();
        } else {
            // Existing record: update authToken
            ResultSet rs = authDb.execute("select id from users where email = ?", auth.getEmail());
            return rs.getFieldAsInt(0);
        }
    }

    public void close() {
        if(authDb != null) {
            try {
                authDb.close();
            } catch (DatabaseException e) {
                GWT.log("Exception thrown while closing auth db", e);
            }
            authDb = null;
        }
    }



}
