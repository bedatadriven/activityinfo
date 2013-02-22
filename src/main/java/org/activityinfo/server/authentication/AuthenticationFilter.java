package org.activityinfo.server.authentication;

/*
 * #%L
 * ActivityInfo Server
 * %%
 * Copyright (C) 2009 - 2013 UNICEF
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
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

import com.google.common.base.Strings;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.teklabs.gwt.i18n.server.LocaleProxy;

/**
 * This filter tries to establish the identify of the connected user at the
 * start of each request.
 * 
 * <p>
 * If the request is successfully authenticated, it is stored in the
 * {@link ServerSideAuthProvider}.
 */
@Singleton
public class AuthenticationFilter implements Filter {

    private static final Logger LOGGER = Logger
        .getLogger(AuthenticationFilter.class.getName());

    private final Provider<HttpServletRequest> request;
    private final Provider<EntityManager> entityManager;
    private final ServerSideAuthProvider authProvider;
    private final BasicAuthentication basicAuthenticator;

    private final LoadingCache<String, AuthenticatedUser> authTokenCache = CacheBuilder
        .newBuilder()
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
        ServerSideAuthProvider authProvider,
        BasicAuthentication basicAuthenticator) {
        this.entityManager = entityManager;
        this.request = request;
        this.authProvider = authProvider;
        this.basicAuthenticator = basicAuthenticator;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
        FilterChain filterChain) throws IOException, ServletException {

        authProvider.clear();

        String authToken = ((HttpServletRequest) request)
            .getHeader("Authorization");
        if (Strings.isNullOrEmpty(authToken)) {
            authToken = authTokenFromCookie();
        }
        if (authToken != null) {
            try {
                AuthenticatedUser currentUser = authTokenCache.get(authToken);
                authProvider.set(currentUser);
                LocaleProxy
                    .setLocale(LocaleHelper.getLocaleObject(currentUser));

                LOGGER.info("Setting locale to " + currentUser.getUserLocale());

            } catch (Exception e) {
                authProvider.clear();
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
        Authentication entity = entityManager.get().find(Authentication.class,
            authToken);
        if (entity == null) {
            // try as basic authentication
            entity = basicAuthenticator.tryAuthenticate(authToken);
        }
        if (entity == null) {
            throw new IllegalArgumentException();
        }
        AuthenticatedUser authenticatedUser = new AuthenticatedUser(authToken,
            entity.getUser().getId(), entity.getUser().getEmail());
        authenticatedUser.setUserLocale(entity.getUser().getLocale());
        return authenticatedUser;
    }

    private String authTokenFromCookie() {
        Cookie[] cookies = request.get().getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName()
                    .equals(AuthenticatedUser.AUTH_TOKEN_COOKIE)) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

}
