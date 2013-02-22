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

import java.sql.Connection;

import com.bedatadriven.rebar.sql.client.SqlDatabase;
import com.bedatadriven.rebar.sql.client.query.MySqlDialect;
import com.bedatadriven.rebar.sql.client.query.SqlDialect;
import com.google.inject.AbstractModule;

public class TestDatabaseModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(Connection.class).toProvider(TestConnectionProvider.class);
		bind(SqlDialect.class).to(MySqlDialect.class);
	}

}
