package org.activityinfo.server.schedule;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * Handles startup and shutdown of the Quartz scheduler
 *
 */
@Singleton
public class QuartzFilter implements Filter {

	private final Quartz quartz;
	
	@Inject
	public QuartzFilter(Quartz quartz) {
		super();
		this.quartz = quartz;
	}

	@Override
	public void init(FilterConfig config) throws ServletException {
		quartz.start();
	}
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain filterChain) throws IOException, ServletException {
		filterChain.doFilter(request, response);
	}

	
	@Override
	public void destroy() {
		quartz.shutdown();
	}
}
