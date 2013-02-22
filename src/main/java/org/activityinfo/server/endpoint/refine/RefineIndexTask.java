package org.activityinfo.server.endpoint.refine;

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

import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.persistence.EntityManager;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import org.activityinfo.server.database.hibernate.entity.AdminEntity;
import org.apache.commons.codec.language.DoubleMetaphone;
import org.hibernate.SessionFactory;
import org.hibernate.StatelessSession;
import org.hibernate.Transaction;

@Path("/tasks/refine/index")
public class RefineIndexTask {

	
	private final Provider<SessionFactory> sessionFactory;

	@Inject
	public RefineIndexTask(Provider<SessionFactory> sessionFactory) {
		super();
		this.sessionFactory = sessionFactory;
	}


	@GET
	public Response rebuildIndex() {
		
		StatelessSession session = sessionFactory.get().openStatelessSession();
		Transaction tx = session.beginTransaction();
		Iterator<AdminEntity> it = session.createQuery("select e from AdminEntity e").list().iterator();
		
		DoubleMetaphone encoder = new DoubleMetaphone();
		
		while(it.hasNext()) {
			AdminEntity entity = it.next();
			entity.setSoundex(encoder.doubleMetaphone(entity.getName()));
			session.update(entity);
		}
		tx.commit();
		session.close();
		
		
		return Response.ok().build();
	}
	
}
