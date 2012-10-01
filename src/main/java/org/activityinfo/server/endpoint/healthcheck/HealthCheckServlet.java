package org.activityinfo.server.endpoint.healthcheck;

import java.io.IOException;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

@Singleton
public class HealthCheckServlet extends HttpServlet {

	private final Provider<EntityManager> entityManager;
	
	@Inject
	public HealthCheckServlet(Provider<EntityManager> entityManager) {
		super();
		this.entityManager = entityManager;
	}
	
	@Override
	protected void doGet(HttpServletRequest req, final HttpServletResponse resp)
			throws ServletException, IOException {
	
		// verify connection with database
		Query query = entityManager.get().createNativeQuery("select count(*) from UserDatabase");
		Object count = query.getSingleResult();
	}
}
