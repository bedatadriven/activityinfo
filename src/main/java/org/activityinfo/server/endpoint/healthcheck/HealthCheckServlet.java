package org.activityinfo.server.endpoint.healthcheck;

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
