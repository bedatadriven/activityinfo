package org.activityinfo.client.inject;

import com.google.gwt.gears.client.database.Database;
import com.google.gwt.gears.client.database.ResultSet;
import com.google.gwt.gears.client.database.DatabaseException;
import com.google.gwt.gears.client.Factory;
import com.google.gwt.gears.client.GearsException;
import com.google.gwt.core.client.GWT;
import com.google.inject.Provider;
import com.google.inject.Inject;

import org.activityinfo.client.command.Authentication;
import org.activityinfo.client.offline.OfflineManager;
import org.activityinfo.client.offline.dao.AuthDAO;
import org.activityinfo.shared.command.Authenticate;

public class OfflineDbProvider implements Provider<Database> {

    private final Authentication auth;

    private Database userDb;

    @Inject
    public OfflineDbProvider(Authentication auth) {
        this.auth = auth;
    }

    private void openDatabase() {

        AuthDAO authDAO = new AuthDAO();
        int userId = 0;
        try {
            userId = authDAO.updateOrInsert(auth);
        } catch (DatabaseException e) {
            throw new RuntimeException(e);
        }

        userDb = Factory.getInstance().createDatabase();
        userDb.open("user" + userId);
    }


    public Database get() {
        if(userDb == null)
            openDatabase();

        return userDb;
    }
}
