package org.activityinfo.server.database.hibernate;

import java.io.IOException;

import javax.persistence.EntityManagerFactory;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.log4j.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public final class HibernateSessionFilter implements Filter {

	private static final Logger LOGGER = Logger.getLogger(HibernateSessionFilter.class);
	
	private HibernateSessionScope sessionScope;
	private EntityManagerFactory entityManagerFactory;

	@Inject
	public HibernateSessionFilter(EntityManagerFactory emf, HibernateSessionScope sessionScope) {
		this.entityManagerFactory = emf;
		this.sessionScope = sessionScope;
	}
	
	@Override
	public void init(FilterConfig config) throws ServletException {
	}
	
	@Override
	public void doFilter(ServletRequest servletRequest,
			ServletResponse servletResponse, FilterChain filterChain)
					throws IOException, ServletException {

		try {
			sessionScope.enter();
			filterChain.doFilter(servletRequest, servletResponse);
		} finally {
			sessionScope.exit();
		}
	}

	@Override
	public void destroy() {
		LOGGER.info("Shutting down Hibernate EntityManagerFactory...");
		entityManagerFactory.close();
	}
}
