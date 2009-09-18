package org.activityinfo.server.dao;

import org.activityinfo.server.domain.*;

public interface AuthDAO {

	/**
	 * Lookup a user by their email
	 * 
	 * @param email
	 * @return The user, or null if no user by this email exists
	 */
	User getUserByEmail(String email);

	/**
	 * Creates a new user in the database
	 * 
	 * @param email
	 * @param name
	 * @param password
	 * @return
	 */
	User createUser(String email, String name, String password, String locale);

	/**
	 * Inserts a new session for the given user in the database
	 * 
	 * @param user
	 */
	Authentication createSession(User user);

	/**
	 * Returns the session record corresponding to the sessionId provided
	 *  or NULL if the token is invalid
	 */
	Authentication getSession(String sessionId);
}