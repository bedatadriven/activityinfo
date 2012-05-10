package org.activityinfo.login.server;

import javax.persistence.NoResultException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.activityinfo.login.client.AuthenticationException;
import org.activityinfo.login.client.PasswordExpiredException;
import org.activityinfo.login.shared.LoginException;
import org.activityinfo.login.shared.LoginService;
import org.activityinfo.shared.auth.AuthenticatedUser;
import org.activityinfo.shared.exception.InvalidLoginException;
import org.sigmah.server.authentication.AuthCookieUtil;
import org.sigmah.server.authentication.Authenticator;
import org.sigmah.server.database.hibernate.dao.AuthenticationDAO;
import org.sigmah.server.database.hibernate.dao.Transactional;
import org.sigmah.server.database.hibernate.dao.UserDAO;
import org.sigmah.server.database.hibernate.entity.Authentication;
import org.sigmah.server.database.hibernate.entity.User;

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

	private User findUserByEmail(String email) throws InvalidLoginException {
		try {
			return userDAO.get().findUserByEmail(email);

		} catch (NoResultException e) {
			throw new InvalidLoginException();
		}
	}

	private void checkPassword(String password, User user) throws AuthenticationException {
		if (!authenticator.get().check(user, password)) {
			throw new AuthenticationException();
		}
	}

	@Override
	protected SerializationPolicy doGetSerializationPolicy(
			HttpServletRequest request, String moduleBaseURL, String strongName) {
		// TODO Auto-generated method stub
		return super.doGetSerializationPolicy(request, moduleBaseURL, strongName);
	}
	
	
}
