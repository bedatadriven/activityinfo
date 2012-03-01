package org.sigmah.server.endpoint.healthcheck;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bedatadriven.rebar.sql.client.SqlDatabase;
import com.bedatadriven.rebar.sql.client.SqlException;
import com.bedatadriven.rebar.sql.client.SqlTransaction;
import com.bedatadriven.rebar.sql.client.SqlTransactionCallback;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

@Singleton
public class HealthCheckServlet extends HttpServlet {

	private final Provider<SqlDatabase> database;
	
	@Inject
	public HealthCheckServlet(Provider<SqlDatabase> database) {
		super();
		this.database = database;
	}


	@Override
	protected void doGet(HttpServletRequest req, final HttpServletResponse resp)
			throws ServletException, IOException {
				
		database.get().transaction(new SqlTransactionCallback() {
			
			@Override
			public void begin(SqlTransaction tx) {
				tx.executeSql("select count(*) from UserDatabase");
			}

			@Override
			public void onSuccess() {
				try {
					resp.getWriter().println("OK");
				} catch (IOException e) {

				}
			}

			@Override
			public void onError(SqlException e) {
				try {
					resp.sendError(500);
				} catch (IOException e1) {
				}
			}
		});
	}
}
