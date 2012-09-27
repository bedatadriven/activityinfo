package org.activityinfo.server.endpoint.gwtrpc;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.not;
import static org.easymock.EasyMock.replay;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.sql.Connection;
import java.sql.SQLException;

import org.activityinfo.server.database.ServerDatabase;
import org.activityinfo.server.database.TestConnectionProvider;
import org.activityinfo.shared.auth.AuthenticatedUser;
import org.activityinfo.shared.command.ThrowingCommand;
import org.activityinfo.shared.command.handler.ThrowingCommandHandler;
import org.junit.Test;

import com.bedatadriven.rebar.sql.client.SqlDatabase;
import com.bedatadriven.rebar.sql.server.jdbc.JdbcDatabase;
import com.bedatadriven.rebar.sql.server.jdbc.JdbcExecutor;
import com.google.inject.Injector;
import com.google.inject.Provider;

public class ServerExecutionContextTest {
		
	
	@Test
	public void testErrorHandling() throws SQLException {
	
		TestConnectionProvider provider = new TestConnectionProvider();
		final Connection connection = provider.get();
	
		ServerDatabase database = new ServerDatabase(new Provider<Connection>() {

			@Override
			public Connection get() {
				return connection;
			}
		});
		
		Injector injector = createMock("injector", Injector.class);
		expect(injector.getInstance(eq(ThrowingCommandHandler.class))).andReturn(new ThrowingCommandHandler()).anyTimes();
		expect(injector.getInstance(eq(AuthenticatedUser.class))).andReturn(new AuthenticatedUser("XYZ", 1, "alex@gmail.com"));
		expect(injector.getInstance(eq(SqlDatabase.class))).andReturn(database);

		replay(injector);
		
		Exception exception = null;
		
		try {
			ServerExecutionContext.execute(injector, new ThrowingCommand());
		} catch(Exception e) {
			exception = e;
		}
		
		assertThat(exception, notNullValue());
		assertTrue(connection.isClosed());
		
	}
	
	private static class ThrowingSqlDatabase extends JdbcDatabase {

		public final ThrowingConnection connection = new ThrowingConnection();
		
		public ThrowingSqlDatabase() {
			super("db");
		}

		@Override
		protected JdbcExecutor newExecutor() {
			return new JdbcExecutor() {
				
				@Override
				protected Connection openConnection() throws Exception {
					return connection;
				}
			};
		}
	}

}
