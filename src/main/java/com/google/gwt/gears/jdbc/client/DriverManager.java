package com.google.gwt.gears.jdbc.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.gears.client.Factory;
import com.google.gwt.gears.client.database.Database;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author Alex Bertram
 */
public class DriverManager {

  public static Connection getConnection(String databaseName) throws SQLException {

    Factory factory = Factory.getInstance();
    if (factory == null)
      throw new SQLException("Factory instance is null");

    GWT.log("DriverManager: Creating connection for database '" + databaseName + "'", null);

    Database db = factory.createDatabase();
    db.open(databaseName);

    return new ConnectionImpl(db);

  }

  public static Connection getConnection() throws SQLException {

    Factory factory = Factory.getInstance();
    if (factory == null)
      throw new SQLException("Factory instance is null");

    Database db = factory.createDatabase();
    db.open();

    return new ConnectionImpl(db);

  }
}
