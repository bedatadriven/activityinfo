package org.activityinfo.server.database.dao;

import org.activityinfo.server.database.hibernate.entity.Authentication;
import org.activityinfo.server.database.hibernate.entity.User;
import org.activityinfo.shared.util.Collector;

import com.bedatadriven.rebar.sql.client.SqlDatabase;
import com.bedatadriven.rebar.sql.client.SqlResultSetRow;
import com.bedatadriven.rebar.sql.client.query.SqlQuery;
import com.bedatadriven.rebar.sql.client.util.SingleRowHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;

public class UserDAO {

	private final SqlDatabase database;

	@Inject
	public UserDAO(SqlDatabase database) {
		super();
		this.database = database;
	}
	
	public User findById(int id) {
		Collector<User> collector = new Collector<User>();
		findById(id, collector);
		return collector.getResult();
	}
	
	public void findById(final int id, final AsyncCallback<User> callback) {
		SqlQuery.select(
		"name",
		"firstName",
		"email",
		"password",
		"locale")
		.from("userlogin")
		.where("userId").equalTo(id)
		.delegateErrorsTo(callback)
		.execute(database, new SingleRowHandler() {
			
			@Override
			public void handleRow(SqlResultSetRow row) {
				User user = new User();
				user.setId(id);
				user.setName(row.getString("name"));
				user.setEmail(row.getString("email"));
				user.setFirstName(row.getString("firstName"));
				user.setLocale(row.getString("locale"));
				
				callback.onSuccess(user);
			}
		});
	}
	
	public void findAuthenticationByToken(final String authToken, final AsyncCallback<Authentication> callback) {
		SqlQuery.select(
				"a.authToken",
				"u.userId",
				"u.firstName",
				"u.email",
				"u.password",
				"u.locale")
				.from("authentication", "a")
				.leftJoin("user", "u").on("a.userId = u.userId")
				.where("a.authToken").equalTo(authToken)
				.delegateErrorsTo(callback)
				.execute(database, new SingleRowHandler() {
					
					@Override
					public void handleRow(SqlResultSetRow row) {
						User user = new User();
						user.setId(row.getInt("userId"));
						user.setName(row.getString("name"));
						user.setEmail(row.getString("email"));
						user.setFirstName(row.getString("firstName"));
						user.setLocale(row.getString("locale"));
						
						Authentication auth = new Authentication();
						auth.setId(authToken);
						auth.setUser(user);
						
						callback.onSuccess(auth);
					}
				});
	}
	
	public Authentication findAuthenticationByToken(String authToken) {
		Collector<Authentication> collector = Collector.newCollector();
		findAuthenticationByToken(authToken, collector);
		return collector.getResult();
	}
	
}
