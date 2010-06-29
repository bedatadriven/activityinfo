package org.sigmah.server.auth;

import org.sigmah.server.domain.User;

/**
 * Service interface which provides validation of user passwords.
 * The only current implementation checks the password against the
 * database but this is intended to be an extensibility point for other
 * methods.
 */
public interface Authenticator {
    boolean check(User user, String plaintextPassword);
}
