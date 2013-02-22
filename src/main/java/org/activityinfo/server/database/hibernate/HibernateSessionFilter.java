package org.activityinfo.server.database.hibernate;

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
import java.util.logging.Logger;

import javax.persistence.EntityManagerFactory;
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

    private static final Logger LOGGER = Logger
        .getLogger(HibernateSessionFilter.class.getName());

    private HibernateSessionScope sessionScope;
    private EntityManagerFactory entityManagerFactory;

    @Inject
    public HibernateSessionFilter(EntityManagerFactory emf,
        HibernateSessionScope sessionScope) {
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
