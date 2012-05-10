package org.sigmah.server.database.hibernate;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public final class HibernateSessionFilter implements Filter {

	private HibernateSessionScope sessionScope;

	@Inject
	public HibernateSessionFilter(HibernateSessionScope sessionScope) {
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
	}
}
