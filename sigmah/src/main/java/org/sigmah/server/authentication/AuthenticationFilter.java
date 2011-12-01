package org.sigmah.server.authentication;

import java.io.IOException;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.sigmah.server.database.hibernate.entity.Authentication;
import org.sigmah.server.i18n.LocaleHelper;
import org.sigmah.shared.auth.AuthenticatedUser;

import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.teklabs.gwt.i18n.server.LocaleProxy;

/**
 * This filter tries to establish the identify of the connected user
 * at the start of each request.
 * 
 * <p>If the request is successfully authenticated, it is stored in the 
 * {@link ServerSideAuthProvider}.
 */
@Singleton
public class AuthenticationFilter implements Filter {

	private final Provider<HttpServletRequest> request;
	private final Provider<EntityManager> entityManager;
	private final ServerSideAuthProvider authProvider;
	
	private final Map<String, AuthenticatedUser> authTokenCache = Maps.newHashMap();

	@Inject
	public AuthenticationFilter(Provider<HttpServletRequest> request, 
							    Provider<EntityManager> entityManager,
							    ServerSideAuthProvider authProvider){
		this.entityManager = entityManager;
		this.request = request;
		this.authProvider = authProvider;
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain filterChain) throws IOException, ServletException {

		authProvider.clear();
		
		String authToken = authTokenFromCookie();
		if(authToken != null) {
			AuthenticatedUser currentUser = lookupAuthToken(authToken);
			if(currentUser != null) {
				authProvider.set(currentUser);
		        LocaleProxy.setLocale(LocaleHelper.getLocaleObject(currentUser));
			}
		}
		filterChain.doFilter(request, response);
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void destroy() {
	}


	private synchronized AuthenticatedUser lookupAuthToken(String authToken) {
		if(authTokenCache.containsKey(authToken)) {
			return authTokenCache.get(authToken);
		} else {
			Authentication entity;
			try {
				entity = entityManager.get().find(Authentication.class, authToken);
			} catch (EntityNotFoundException e) {
				return null;
			}
			AuthenticatedUser authenticatedUser = new AuthenticatedUser(authToken, entity.getUser().getId(), entity.getUser().getEmail());
			authTokenCache.put(authToken, authenticatedUser);
			return authenticatedUser;
		}
	}


	private String authTokenFromCookie() {
		Cookie[] cookies = request.get().getCookies();
		if(cookies != null) {
			for(Cookie cookie : cookies) {
				if(cookie.getName().equals(AuthenticatedUser.AUTH_TOKEN_COOKIE)) {
					return cookie.getValue();
				}
			}
		}
		return null;
	}
}
