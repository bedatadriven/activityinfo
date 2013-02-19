package org.activityinfo.server.bootstrap;

import javax.inject.Provider;
import javax.ws.rs.core.NewCookie;

import org.activityinfo.server.database.hibernate.dao.AuthenticationDAO;
import org.activityinfo.server.database.hibernate.dao.Transactional;
import org.activityinfo.server.database.hibernate.entity.Authentication;
import org.activityinfo.server.database.hibernate.entity.User;
import org.activityinfo.shared.auth.AuthenticatedUser;

import com.google.inject.Inject;

public class AuthTokenProvider {
    
    private static final String ROOT = "/";
    private static final int THIS_SESSION = -1;
    private static final int ONE_YEAR = 365 * 24 * 60 * 60;

    
    private final Provider<AuthenticationDAO> authDAO;
    
    @Inject
    public AuthTokenProvider(Provider<AuthenticationDAO> authDAO) {
        super();
        this.authDAO = authDAO;
    }

    @Transactional
    public Authentication createNewAuthToken(User user) {
        Authentication auth = new Authentication(user);
        authDAO.get().persist(auth);
        return auth;
    }
    
    public NewCookie[] createNewAuthCookies(User user) {
        Authentication token = createNewAuthToken(user);
        
        
        NewCookie cookie = newAuthCookie(AuthenticatedUser.AUTH_TOKEN_COOKIE, token.getId());
        NewCookie userCookie = newAuthCookie(AuthenticatedUser.USER_ID_COOKIE, Integer.toString(token.getUser().getId()));
        NewCookie emailCookie =newAuthCookie(AuthenticatedUser.EMAIL_COOKIE, user.getEmail());
        NewCookie localeCookie = newLocaleCookie(AuthenticatedUser.USER_LOCAL_COOKIE, user.getLocale());
        
        return new NewCookie[] { cookie, userCookie, emailCookie, localeCookie };       
    }

    private NewCookie newAuthCookie(String name, String value) {
        String path = ROOT;
        String domain = null;
        String comment = null;
        int maxAge = THIS_SESSION;
        boolean onlySecure = false;
        return new NewCookie(name, value, path, domain, comment, maxAge, onlySecure);
    }

    private NewCookie newLocaleCookie(String name, String value) {
        String path = ROOT;
        String domain = null;
        String comment = null;
        int maxAge = ONE_YEAR;
        boolean onlySecure = false;
        return new NewCookie(name, value, path, domain, comment, maxAge, onlySecure);
    }
}
