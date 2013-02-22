

package org.activityinfo.server.database;

/*
 * #%L
 * ActivityInfo Server
 * %%
 * Copyright (C) 2009 - 2013 UNICEF
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.dbunit.DatabaseUnitException;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.LowerCaseDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.ext.mssql.InsertIdentityOperation;
import org.dbunit.ext.mysql.MySqlConnection;
import org.dbunit.operation.DatabaseOperation;
import org.junit.internal.runners.model.MultipleFailureException;
import org.junit.runners.model.Statement;

import com.bedatadriven.rebar.sql.server.jdbc.JdbcScheduler;
import com.google.inject.Provider;

public class LoadDataSet extends Statement {
    private final Statement next;
    private final Object target;
    private final Provider<Connection> connectionProvider;
    private final String name;

    public LoadDataSet(Provider<Connection> connectionProvider, Statement next, String name, Object target) {
        this.next = next;
        this.target = target;
        this.connectionProvider = connectionProvider;
        this.name = name;
    }

    @Override
    public void evaluate() throws Throwable {
    	
    	JdbcScheduler.get().forceCleanup();
    	
        System.err.println("DBUnit: removing all rows");
        removeAllRows();

        System.err.println("DBUnit: loading " + name + " into the database.");
        IDataSet data = loadDataSet();

        List<Throwable> errors = new ArrayList<Throwable>();
        errors.clear();
        try {
            populate(data);
            next.evaluate();
        } catch (Throwable e) {
            errors.add(e);
        }
        MultipleFailureException.assertEmpty(errors);
    }

    private IDataSet loadDataSet() throws IOException, DataSetException {
        InputStream in = target.getClass().getResourceAsStream(name);
        if (in == null) {
            throw new Error("Could not find resource '" + name + "'");
        }
        
        return new LowerCaseDataSet(new FlatXmlDataSetBuilder()
        	.setDtdMetadata(true)
        	.setColumnSensing(true)
        	.build(new InputStreamReader(in)));
    }

    private void populate(final IDataSet dataSet) throws DatabaseUnitException, SQLException {
        executeOperation(InsertIdentityOperation.INSERT, dataSet);
    }

    private void removeAllRows() {
        DatabaseCleaner cleaner = new DatabaseCleaner(connectionProvider);
        cleaner.clean();
    }

    private void executeOperation(final DatabaseOperation op, final IDataSet dataSet) throws DatabaseUnitException, SQLException {
    	Connection connection = connectionProvider.get();
    	try {
    		  IDatabaseConnection dbUnitConnection = new MySqlConnection(connection, null);
              op.execute(dbUnitConnection, dataSet);
    	} finally {
    		connection.close();
    	}
    }

}
