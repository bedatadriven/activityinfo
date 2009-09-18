package org.activityinfo.server.dao.jpa;

import java.util.List;
import javax.persistence.EntityManager;

import org.activityinfo.server.dao.AuthDAO;
import org.activityinfo.server.domain.*;

import com.google.inject.Inject;


public class AuthDAOJPA implements AuthDAO {

	private final EntityManager em;
	
	
	@Inject
	public AuthDAOJPA(EntityManager em) {
		this.em = em;
	}

    @Override
	public User getUserByEmail(String email) {
		List<User> list = em.createQuery("select u from User u where u.email = ?1")
							.setParameter(1, email).getResultList();
		
		if(list.size() == 0)
			return null;
		else 
			return list.get(0);
	}

    @Override
	public User createUser(String email, String name, String password, String locale) {
	
		User newUser = new User();
		newUser.setEmail(email);
		newUser.setName(name);
		newUser.setNewUser(true);
		newUser.setLocale("fr");
		newUser.changePassword(password);

		em.persist(newUser);
		
		return newUser;
				
	}

    @Override
	public Authentication createSession(User user) {
	
		Authentication session = new Authentication(user);
		em.persist(session);
		
		return session;
	}

	@Override
	public Authentication getSession(String sessionId) {

		return em.find(Authentication.class, sessionId);
	}
    
}


