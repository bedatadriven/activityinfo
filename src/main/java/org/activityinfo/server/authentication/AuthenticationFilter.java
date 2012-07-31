package org.activityinfo.server.authentication;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

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

import org.activityinfo.server.database.hibernate.entity.Authentication;
import org.activityinfo.server.i18n.LocaleHelper;
import org.activityinfo.shared.auth.AuthenticatedUser;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
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
	private final LoadingCache<String, AuthenticatedUser> authTokenCache = CacheBuilder.newBuilder()
			.maximumSize(10000)	
			.expireAfterAccess(6, TimeUnit.HOURS)
			.build(new CacheLoader<String, AuthenticatedUser>() {

				@Override
				public AuthenticatedUser load(String authToken) throws Exception {
					return queryAuthToken(authToken);
				}
			});
	
	
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
			try {
				AuthenticatedUser currentUser = authTokenCache.get(authToken);
				authProvider.set(currentUser);
		        LocaleProxy.setLocale(LocaleHelper.getLocaleObject(currentUser));
			} catch (Exception e) {
				authProvider.set(null);
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


	private AuthenticatedUser queryAuthToken(String authToken) {
		Authentication entity = entityManager.get().find(Authentication.class, authToken);
		if(entity == null) {
			throw new IllegalArgumentException();
		}
		return new AuthenticatedUser(authToken, entity.getUser().getId(), entity.getUser().getEmail());
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
