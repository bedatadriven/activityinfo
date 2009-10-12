package org.activityinfo.server.service.impl;

import com.google.inject.Inject;
import org.activityinfo.server.dao.AuthDAO;
import org.activityinfo.server.domain.Authentication;
import org.activityinfo.server.domain.User;
import org.activityinfo.server.service.Authenticator;
import org.activityinfo.shared.exception.InvalidAuthTokenException;
import org.activityinfo.shared.exception.InvalidLoginException;

import javax.persistence.EntityManager;

/*
 * @author Alex Bertram
 */

public class DbAuthenticator implements Authenticator {

    private EntityManager em;
    private AuthDAO authDAO;

    @Inject
    public DbAuthenticator(EntityManager em, AuthDAO authDAO) {
        this.em = em;
        this.authDAO = authDAO;
    }


    public Authentication authenticate(String email, String password) throws InvalidLoginException {

        User user = authDAO.getUserByEmail(email);
        if(user == null)
            throw new InvalidLoginException();

        if(!user.checkPassword(password))
            throw new InvalidLoginException();

        em.getTransaction().begin();

        Authentication auth = authDAO.createSession(user);

        em.getTransaction().commit();

        return auth;
    }
    
    public Authentication getSession(String sessionId) throws InvalidAuthTokenException {
        Authentication session = authDAO.getSession(sessionId);
        
        if(session == null) 
        	throw new InvalidAuthTokenException();
        
        return session;
    }

}
