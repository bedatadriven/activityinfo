package org.activityinfo.login.server;

import javax.persistence.NoResultException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.activityinfo.login.shared.LoginException;
import org.activityinfo.login.shared.LoginService;
import org.activityinfo.login.shared.PasswordExpiredException;
import org.activityinfo.server.authentication.AuthCookieUtil;
import org.activityinfo.server.authentication.Authenticator;
import org.activityinfo.server.database.hibernate.dao.AuthenticationDAO;
import org.activityinfo.server.database.hibernate.dao.Transactional;
import org.activityinfo.server.database.hibernate.dao.UserDAO;
import org.activityinfo.server.database.hibernate.entity.Authentication;
import org.activityinfo.server.database.hibernate.entity.User;
import org.activityinfo.shared.auth.AuthenticatedUser;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.google.gwt.user.server.rpc.SerializationPolicy;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

@Singleton
public class LoginServiceServlet extends RemoteServiceServlet implements LoginService {

	private final Provider<Authenticator> authenticator;
	private final Provider<UserDAO> userDAO;
	private final Provider<HttpServletResponse> response;
	private final Provider<AuthenticationDAO> authDAO;

	@Inject
	public LoginServiceServlet(Provider<Authenticator> authenticator,
			Provider<UserDAO> userDAO,
			Provider<HttpServletResponse> response,
			Provider<AuthenticationDAO> authDAO) {
		super();
		this.authenticator = authenticator;
		this.userDAO = userDAO;
		this.response = response;
		this.authDAO = authDAO;
	}

	@Override
	public AuthenticatedUser login(String email, String password, boolean rememberLogin) throws LoginException {
		User user = findUserByEmail(email);
		if(user.getHashedPassword() == null || user.getHashedPassword().length() == 0) {
			throw new PasswordExpiredException();
		} else {
			checkPassword(password, user);
			return createAuthCookie(user, rememberLogin); 
		}
	}

	private AuthenticatedUser createAuthCookie(User user, boolean rememberLogin) {
        Authentication auth = createNewAuthToken(user);
        AuthCookieUtil.addAuthCookie(response.get(), auth, rememberLogin);
		return new AuthenticatedUser(auth.getId(), auth.getUser().getId(), auth.getUser().getEmail());
	}

    @Transactional
    protected Authentication createNewAuthToken(User user) {
        Authentication auth = new Authentication(user);
        authDAO.get().persist(auth);
        return auth;
    }

	
	@Override
	public void changePassword(String email) {
		// TODO Auto-generated method stub

	}

	private User findUserByEmail(String email) throws LoginException {
		try {
			return userDAO.get().findUserByEmail(email);

		} catch (NoResultException e) {
			throw new LoginException();
		}
	}

	private void checkPassword(String password, User user) throws LoginException {
		if (!authenticator.get().check(user, password)) {
			throw new LoginException();
		}
	}

	@Override
	protected SerializationPolicy doGetSerializationPolicy(
			HttpServletRequest request, String moduleBaseURL, String strongName) {
		// TODO Auto-generated method stub
		return super.doGetSerializationPolicy(request, moduleBaseURL, strongName);
	}
	
	
}
