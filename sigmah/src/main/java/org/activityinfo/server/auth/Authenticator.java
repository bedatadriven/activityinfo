package org.activityinfo.server.auth;

import org.activityinfo.server.domain.User;

public interface Authenticator {
    boolean check(User user, String plaintextPassword);
}
