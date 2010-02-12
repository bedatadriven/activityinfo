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

package org.activityinfo.server.bootstrap;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import freemarker.template.Configuration;
import org.activityinfo.server.auth.Authenticator;
import org.activityinfo.server.bootstrap.model.LoginPageModel;
import org.activityinfo.server.domain.User;
import org.activityinfo.server.util.logging.Trace;
import org.activityinfo.shared.exception.InvalidLoginException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Singleton
public class LoginController extends AbstractController {

    public static final String ENDPOINT = "login";

    @Inject
    public LoginController(Injector injector, Configuration templateCfg) {
        super(injector, templateCfg);
    }

    @Override
    @Trace
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        writeView(resp, new LoginPageModel(parseUrlSuffix(req)));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            User user = findUserByEmail(req.getParameter("email"));
            checkPassword(req.getParameter("password"), user);

            boolean rememberLogin = "true".equals(req.getParameter("remember"));
            createAuthCookie(req, resp, user, rememberLogin);

            resp.sendRedirect(HostController.ENDPOINT + urlSuffix(req));

        } catch (InvalidLoginException e) {
            writeView(resp, LoginPageModel.unsuccessful(urlSuffix(req)));
        }
    }

    protected void checkPassword(String password, User user) throws InvalidLoginException {
        Authenticator authenticator = injector.getInstance(Authenticator.class);
        if (!authenticator.check(user, password)) {
            throw new InvalidLoginException();
        }
    }

    protected String urlSuffix(HttpServletRequest req) {
        String suffix = req.getParameter("urlSuffix");
        return suffix == null ? "" : suffix;
    }

}
