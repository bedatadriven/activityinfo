package org.activityinfo.server.endpoint.gwtrpc;

import java.sql.SQLException;

import org.junit.Test;

public class ServerExecutionContextTest {
		
	
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
