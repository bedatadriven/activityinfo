package org.sigmah.server.authentication;

import java.io.IOException;

import javax.persistence.EntityManager;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.sigmah.shared.auth.AuthenticatedUser;

import com.google.inject.Inject;
import com.google.inject.Provider;

public class AuthenticationFilter implements Filter {

	private final Provider<HttpServletRequest> request;
	private final Provider<EntityManager> entityManager;

	@Inject
	public AuthenticationFilter(Provider<HttpServletRequest> request, Provider<EntityManager> entityManager){
		this.entityManager = entityManager;
		this.request = request;
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain filterChain) throws IOException, ServletException {

		String authToken = authTokenFromCookie();

		if(authToken != null) {

			if(authFromToken(authToken) != null){
				return;
			}			
		}

		throw new IllegalStateException("Request is not authenticated");

	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void destroy() {
	}


	private AuthenticatedUser authFromToken(String authToken) {
		org.sigmah.server.database.hibernate.entity.Authentication entity =
			entityManager.get().find(org.sigmah.server.database.hibernate.entity.Authentication.class, authToken);

		return new AuthenticatedUser(entity.getUser().getId(), authToken, entity.getUser().getEmail());
	}


	private String authTokenFromCookie() {
		Cookie[] cookies = request.get().getCookies();
		if(cookies != null) {
			for(Cookie cookie : cookies) {
				if(cookie.getName().equals(org.sigmah.shared.auth.AuthenticatedUser.AUTH_TOKEN_COOKIE)) {
					return cookie.getValue();
				}
			}
		}
		return null;
	}
}
