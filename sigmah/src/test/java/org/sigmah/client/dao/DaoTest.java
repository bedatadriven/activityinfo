/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */
package org.sigmah.client.dao;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;

import org.dbunit.DatabaseUnitException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.ext.h2.H2Connection;
import org.dbunit.operation.DatabaseOperation;
import org.junit.Before;

import com.bedatadriven.rebar.persistence.client.ConnectionProvider;
import com.google.inject.Inject;

public abstract class DaoTest {
	
	public abstract String getResourcePath();
	
	@Inject
    protected ConnectionProvider connectionProvider; 
	
	public Connection getConnection() throws SQLException  {
		return connectionProvider.getConnection();
	}
	
	@Before
  	public void setup() throws Throwable {
		load();
	}
	
	public void load() throws IOException, DatabaseUnitException, SQLException {
		InputStream in = this.getClass().getResourceAsStream(getResourcePath());
		if (in == null) {
			throw new Error("Could not open sql resource:" + getResourcePath());
		}
        IDataSet data = new FlatXmlDataSet(new InputStreamReader(in), false, true, false);
        DatabaseOperation.CLEAN_INSERT.execute(new H2Connection(getConnection(), ""), data);
	}
	
}
