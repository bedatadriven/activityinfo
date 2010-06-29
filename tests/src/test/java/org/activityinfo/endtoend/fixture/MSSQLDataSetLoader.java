/*
 * This file is part of ActivityInfo.
 *
 * ActivityInfo is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * ActivityInfo is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with ActivityInfo.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright 2010 Alex Bertram and contributors.
 */

package org.activityinfo.endtoend.fixture;

import org.dbunit.DatabaseUnitException;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.ext.mssql.InsertIdentityOperation;
import org.dbunit.ext.mssql.MsSqlConnection;
import org.dbunit.operation.DatabaseOperation;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class MSSQLDataSetLoader {

    private Connection connection;
    private IDataSet dataSet;
    private String name;


    public void load(String name) {
        this.name = name;
        try {
            load();
        } catch (Exception e) {
            throw new AssertionError(e);
        }
    }

    public void load() throws IOException, DatabaseUnitException, SQLException, ClassNotFoundException {
        openConnection();
        try {
            loadDataSet();
            removeAllRows();
            populate();
        } finally {
            closeConnection();
        }
    }

    private void loadDataSet() throws IOException, DataSetException {
        InputStream in = getClass().getResourceAsStream(name);
        if (in == null)
            throw new Error("Could not find resource '" + name + "'");
        dataSet = new FlatXmlDataSet(new InputStreamReader(in), false, true, false);
    }

    private void openConnection() throws SQLException, ClassNotFoundException {
        Class.forName("net.sourceforge.jtds.jdbc.Driver");
        connection = DriverManager.getConnection(
            "jdbc:jtds:sqlserver://localhost:1433/actinfo;user=sa;password=adminpwd");
    }

    private void removeAllRows() throws SQLException {
        String tables[] = {
            "AttributeGroupInActivity",
            "PartnerInDatabase",
            "LocationAdminLink",
            "IndicatorValue",
            "AttributeValue",
            "ReportingPeriod",
            "Site",
            "Location",
            "Indicator",
            "Attribute",
            "AttributeGroup",
            "Activity",
            "UserPermission",
            "Partner",
            "ReportSubscription",
            "ReportTemplate",
            "UserDatabase",
            "Authentication",
            "UserLogin",
            "LocationType",
            "AdminEntity",
            "AdminLevel",
            "Country" };

        Statement stmt = connection.createStatement();
        for(String tableName : tables) {
            stmt.execute("delete from " + tableName);
        }
        stmt.close();
    }


    private void populate() throws SQLException, DatabaseUnitException {
        executeOperation(InsertIdentityOperation.INSERT, dataSet);
    }

    private void executeOperation(final DatabaseOperation op, final IDataSet dataSet) throws SQLException, DatabaseUnitException {
        IDatabaseConnection dbUnitConnection = createDbUnitConnection(connection);
        op.execute(dbUnitConnection, dataSet);
    }

    private IDatabaseConnection createDbUnitConnection(Connection connection) throws DatabaseUnitException {
        IDatabaseConnection con = new MsSqlConnection(connection);
        con.getConfig().setProperty("http://www.dbunit.org/properties/escapePattern", "[?]");
        return con;
    }

    private void closeConnection() throws SQLException {
        connection.close();
    }
}
