package org.activityinfo.server.endpoint.gwtrpc;

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

import java.sql.SQLException;

import org.junit.Test;

public class RemoteExecutionContextTest {
		
	
	@Test
	public void testErrorHandling() throws SQLException {
	
//		TestConnectionProvider provider = new TestConnectionProvider();
//		final Connection connection = provider.get();
//	
//		ServerDatabase database = new ServerDatabase(new Provider<Connection>() {
//
//			@Override
//			public Connection get() {
//				return connection;
//			}
//		});
//		
//		Injector injector = createMock("injector", Injector.class);
//		expect(injector.getInstance(eq(ThrowingCommandHandler.class))).andReturn(new ThrowingCommandHandler()).anyTimes();
//		expect(injector.getInstance(eq(AuthenticatedUser.class))).andReturn(new AuthenticatedUser("XYZ", 1, "alex@gmail.com"));
//		expect(injector.getInstance(eq(SqlDatabase.class))).andReturn(database);
//
//		replay(injector);
//		
//		Exception exception = null;
//		
//		try {
//			ServerExecutionContext.execute(injector, new ThrowingCommand());
//		} catch(Exception e) {
//			exception = e;
//		}
//		
//		assertThat(exception, notNullValue());
//		assertTrue(connection.isClosed());
//		
	}
	
//	private static class ThrowingSqlDatabase extends JdbcDatabase {
//
//		public final ThrowingConnection connection = new ThrowingConnection();
//		
//		public ThrowingSqlDatabase() {
//			super("db");
//		}
//
//		@Override
//		protected JdbcExecutor newExecutor() {
//			return new JdbcExecutor() {
//				
//				@Override
//				protected Connection openConnection() throws Exception {
//					return connection;
//				}
//			};
//		}
//	}

}
