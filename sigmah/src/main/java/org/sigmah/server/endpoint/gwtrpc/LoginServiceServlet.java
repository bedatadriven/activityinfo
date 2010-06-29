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

package org.sigmah.server.endpoint.gwtrpc;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import org.sigmah.login.client.LoginException;
import org.sigmah.server.Cookies;
import org.sigmah.server.auth.Authenticator;
import org.sigmah.server.dao.AuthenticationDAO;
import org.sigmah.server.dao.Transactional;
import org.sigmah.server.dao.UserDAO;
import org.sigmah.server.domain.Authentication;
import org.sigmah.server.domain.User;

import javax.persistence.NoResultException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Singleton
public class LoginServiceServlet extends HttpServlet {

    private final Injector injector;
    private final Authenticator authenticator;

    @Inject
    public LoginServiceServlet(Injector injector, Authenticator authenticator) {
        this.injector = injector;
        this.authenticator = authenticator;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try {
            tryLogin(req, resp);
        } catch (LoginException e) {
            writeResponse(resp, "BAD LOGIN");
        }
    }

    private void tryLogin(HttpServletRequest req, HttpServletResponse resp) throws LoginException, IOException {
        String email = req.getParameter("email");
        String password = req.getParameter("password");

        User user = findUser(email);

        if(!authenticator.check(user, password)) {
            throw new LoginException();
        }

        Authentication auth = createAndPersistAuthToken(user);

        HttpServletResponse response = injector.getInstance(HttpServletResponse.class);
        Cookies.addAuthCookie(response, auth, false);

        writeResponse(resp, "OK");
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

    private void writeResponse(HttpServletResponse resp, String body) throws IOException {
        resp.setContentType("text/html");
        PrintWriter writer = resp.getWriter();
        writer.print("<html>\n");
        writer.print("<script type=\"text/javascript\">\n");
        writer.print("window.name = \"" + body + "\";\n");
        writer.print("</script>\n");
        writer.print("</html>");
        writer.flush();
    }
}

