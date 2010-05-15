/*
 * This file is part of ActivityInfo.
 *
 * ActivityInfo is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * ActivityInfo is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with ActivityInfo.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright 2010 Alex Bertram and contributors.
 */

package org.activityinfo.server.endpoint.gwtrpc;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import org.activityinfo.login.client.LoginException;
import org.activityinfo.login.client.LoginResult;
import org.activityinfo.login.client.LoginService;
import org.activityinfo.server.Cookies;
import org.activityinfo.server.auth.Authenticator;
import org.activityinfo.server.dao.AuthenticationDAO;
import org.activityinfo.server.dao.Transactional;
import org.activityinfo.server.dao.UserDAO;
import org.activityinfo.server.domain.Authentication;
import org.activityinfo.server.domain.User;
import org.activityinfo.server.util.logging.LogException;

import javax.persistence.NoResultException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Singleton
public class LoginServiceServlet extends RemoteServiceServlet implements LoginService {

    private final Injector injector;
    private final Authenticator authenticator;

    @Inject
    public LoginServiceServlet(Injector injector, Authenticator authenticator) {
        this.injector = injector;
        this.authenticator = authenticator;
    }

    @Override
    @LogException
    public LoginResult login(String email, String password) throws LoginException {

        User user = findUser(email);

        if(!authenticator.check(user, password))
            throw new LoginException();

        Authentication auth = createAndPersistAuthToken(user);

        HttpServletResponse response = injector.getInstance(HttpServletResponse.class);
        Cookies.addAuthCookie(response, auth, false);

        return new LoginResult(getAppUrl(auth));
    }

    private String getAppUrl(Authentication auth) {
        HttpServletRequest request = injector.getInstance(HttpServletRequest.class);

        StringBuilder url = new StringBuilder();
        url.append("http://").append(request.getServerName());
        if(request.getServerPort()!=80)
            url.append(":").append(request.getServerPort());
        url.append("/").append(request.getContextPath());
        url.append("?auth=").append(auth.getId());
        url.append("&redirect=true");

        return url.toString();
    }

    private User findUser(String email) throws LoginException {
        UserDAO userDAO = injector.getInstance(UserDAO.class);
        try {
            return userDAO.findUserByEmail(email);
        } catch (NoResultException e) {
            throw new LoginException();
        }
    }

    @Transactional
    protected Authentication createAndPersistAuthToken(User user) {
        AuthenticationDAO authDAO = injector.getInstance(AuthenticationDAO.class);
        Authentication auth = new Authentication(user);
        authDAO.persist(auth);

        return auth;
    }
}
