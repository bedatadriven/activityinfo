package org.activityinfo.client.offline;

import com.bedatadriven.rebar.sql.client.GearsConnectionFactory;
import com.google.inject.Inject;
import com.google.inject.Provider;
import org.activityinfo.client.dispatch.remote.Authentication;

import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionProvider implements Provider<Connection> {

    private final Authentication auth;

    @Inject
    public ConnectionProvider(Authentication auth) {
        this.auth = auth;
    }


    public String getDatabaseName() {
        return "user" + auth.getUserId();
    }


    public Connection get() {
        try {
            return GearsConnectionFactory.getConnection(getDatabaseName());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
