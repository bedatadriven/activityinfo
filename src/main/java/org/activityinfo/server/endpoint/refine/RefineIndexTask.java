package org.activityinfo.server.endpoint.refine;

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
