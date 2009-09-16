package org.activityinfo.server.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import org.activityinfo.server.DbUnitTestCase;
import org.activityinfo.server.dao.AuthDAO;
import org.activityinfo.server.dao.jpa.AuthDAOJPA;
import org.activityinfo.server.domain.Authentication;
import org.activityinfo.server.domain.User;
import org.activityinfo.server.service.Authenticator;
import org.activityinfo.server.service.impl.DbAuthenticator;
import org.activityinfo.shared.exception.InvalidAuthTokenException;
import org.activityinfo.shared.exception.InvalidLoginException;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;


public class AuthTest extends DbUnitTestCase {


	@BeforeClass
	public static void runBefore() throws Exception {

		populate("authTest");

		/* Now we have to hash the passwords */

		EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        tx.begin();

		List<User> users = em.createQuery("select user from User user").getResultList();

        for(User user : users) {
        	user.changePassword(user.getName() + "12");
        }
        tx.commit();
        em.close();
	}

	@Test
	public void testValidAuth() throws InvalidLoginException {

		EntityManager em = emf.createEntityManager();
        AuthDAO dao = new AuthDAOJPA(em);
        Authenticator auth = new DbAuthenticator(em, dao);

		auth.authenticate("bavon@nrcdrc.org", "Bavon12");
		auth.authenticate("stefan@irc.org", "Stefan12");

		em.close();
	}
	

	@Test(expected=InvalidLoginException.class)
	public void testInvalidAuth() throws InvalidLoginException {

		EntityManager em = emf.createEntityManager();
        AuthDAO dao = new AuthDAOJPA(em);
        Authenticator auth = new DbAuthenticator(em, dao);
        
		auth.authenticate("bavon@nrcdrc.org", "Bavon14");

	}
	
	@Test(expected=InvalidLoginException.class)
	public void testInvalidAuth2() throws InvalidLoginException {

		EntityManager em = emf.createEntityManager();
        AuthDAO dao = new AuthDAOJPA(em);
        Authenticator auth = new DbAuthenticator(em, dao);
        
		auth.authenticate("bavon@nrcdrc.org",  null);

	}


	@Test
	public void testNewSession() throws InvalidAuthTokenException, InvalidLoginException {


        EntityManager em = emf.createEntityManager();
        AuthDAO dao = new AuthDAOJPA(em);
        Authenticator auth = new DbAuthenticator(em, dao);

		Authentication session = auth.authenticate("stefan@irc.org", "Stefan12");
		User stefan = session.getUser();
		
		String sessionId = session.getId();

		Assert.assertEquals("session key length", 32, session.getId().length());

		em.close();


		em = emf.createEntityManager();
		dao = new AuthDAOJPA(em);
        auth = new DbAuthenticator(em, dao);

		session = auth.getSession(sessionId);

		Assert.assertEquals(session.getUser().getId(), stefan.getId());

		em.close();
	}

//	@Test
//	public void testExpiredSession() throws InvalidSessionIdException {
//
//		EntityManager em = emf.createEntityManager();
//		AuthenticationDAO authDAO = new AuthenticationDAO(em);
//
//		Session expiredSession = null;
//		try {
//			expiredSession = authDAO.openSession("12345678123456781234567812345678");
//		} catch(SessionTimeoutException e) {
//
//		}
//		Assert.assertNull(expiredSession);
//
//		em.close();
//
//	}

	@Test(expected= InvalidAuthTokenException.class)
	public void testBadSession() throws InvalidAuthTokenException {

        EntityManager em = emf.createEntityManager();
        AuthDAO dao = new AuthDAOJPA(em);
        Authenticator auth = new DbAuthenticator(em, dao);


		auth.getSession("foobar");

	}
}
