package org.activityinfo.server.login;

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

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;

import org.activityinfo.server.database.hibernate.entity.Domain;

import com.google.inject.Inject;
import com.google.inject.Provider;

public class HttpDomainProvider implements DomainProvider {

    private final Provider<HttpServletRequest> request;
    private final Provider<EntityManager> entityManager;

    protected HttpDomainProvider() {
        request = null;
        entityManager = null;
    }
  
    @Inject
    public HttpDomainProvider(Provider<HttpServletRequest> request, Provider<EntityManager> entityManager) {
        super();
        this.request = request;
        this.entityManager = entityManager;
    }

    /* (non-Javadoc)
     * @see org.activityinfo.server.login.DomainProvider#findDomain()
     */
    @Override
    public Domain findDomain() {
        Domain result = entityManager.get().find(Domain.class, request.get().getServerName());
        if (result == null) {
            result = Domain.DEFAULT;
        }
        return result;
    }
}
