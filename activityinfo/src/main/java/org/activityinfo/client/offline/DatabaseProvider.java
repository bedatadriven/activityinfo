package org.activityinfo.client.offline;

import com.google.gwt.gears.client.Factory;
import com.google.gwt.gears.client.database.Database;
import com.google.gwt.gears.client.database.DatabaseException;
import com.google.inject.Inject;
import com.google.inject.Provider;
import org.activityinfo.client.dispatch.remote.Authentication;
import org.activityinfo.client.offline.dao.AuthDAO;

public class DatabaseProvider implements Provider<Database> {

    private final Authentication auth;

    private Database userDb;

    @Inject
    public DatabaseProvider(Authentication auth) {
        this.auth = auth;
    }

    private void openDatabase() {


        userDb = Factory.getInstance().createDatabase();
        userDb.open(getDatabaseName());
    }

    public String getDatabaseName() {
        AuthDAO authDAO = new AuthDAO();
        int userId;
        try {
            userId = authDAO.updateOrInsert(auth);
        } catch (DatabaseException e) {
            throw new RuntimeException(e);
        }
        return "user" + userId;
    }


    public Database get() {
        if (userDb == null)
            openDatabase();

        return userDb;
    }
}
