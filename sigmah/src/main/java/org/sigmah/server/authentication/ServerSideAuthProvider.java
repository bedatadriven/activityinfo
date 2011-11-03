package org.sigmah.server.authentication;

import javax.persistence.EntityManager;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.sigmah.shared.auth.AuthenticatedUser;

import com.google.inject.Inject;
import com.google.inject.Provider;

public class ServerSideAuthProvider implements Provider<AuthenticatedUser> {

	private final Provider<HttpServletRequest> request;
	private final Provider<EntityManager> entityManager;
	
	@Inject
	public ServerSideAuthProvider(Provider<HttpServletRequest> request,
								  Provider<EntityManager> entityManager) {
		super();
		this.request = request;
		this.entityManager = entityManager;
	}

	@Override
	public AuthenticatedUser get() {
		// first attempt to get authToken from the cookie
		String authToken = authTokenFromCookie();
		
		if(authToken != null) {
			return authFromToken(authToken);
		}
		
		throw new IllegalStateException("Request is not authenticated");
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
				if(cookie.getName().equals(org.sigmah.shared.Cookies.AUTH_TOKEN_COOKIE)) {
					return cookie.getValue();
				}
			}
		}
		return null;
	}
}
