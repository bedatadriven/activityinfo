package org.activityinfo.server.dao;

import org.activityinfo.server.domain.Authentication;
import org.activityinfo.server.domain.User;

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
	 * @param email The new user's email address
	 * @param name The new user's name
     * @param invitingUser The user who has invited this user
	 * @return The new, persisted <code>User</code> entity
	 */
	User createUser(String email, String name, String locale, User invitingUser);

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