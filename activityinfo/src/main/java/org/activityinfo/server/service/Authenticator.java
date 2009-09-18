package org.activityinfo.server.service;

import org.activityinfo.server.domain.Authentication;
import org.activityinfo.shared.exception.InvalidAuthTokenException;
import org.activityinfo.shared.exception.InvalidLoginException;


public interface Authenticator {

    /**
     * Attempts to authenticate a user
     *
     * @param email The user's email address
     * @param password Plaintext password provided by the user
     * @return A new authentication (session) object
     * @throws InvalidLoginException 
     */
    Authentication authenticate(String email, String password) throws InvalidLoginException;

    Authentication getSession(String sessionId) throws InvalidAuthTokenException;
    

}
