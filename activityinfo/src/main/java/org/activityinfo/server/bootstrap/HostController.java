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
import org.activityinfo.server.bootstrap.exception.NoValidAuthentication;
import org.activityinfo.server.bootstrap.model.HostPageModel;
import org.activityinfo.server.dao.AuthenticationDAO;
import org.activityinfo.server.dao.Transactional;
import org.activityinfo.server.domain.Authentication;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.activityinfo.server.util.StringUtil.isEmpty;

@Singleton
public class HostController extends AbstractController {
    public static final String ENDPOINT = "";

    @Inject
    public HostController(Injector injector, Configuration templateCfg) {
        super(injector, templateCfg);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (requestIsFromIE6OnPort80(req)) {
            redirectToPort(req, resp, 8080);
            return;
        }

        try {
            Authentication auth = getAuthentication(req);
            writeView(resp, new HostPageModel(auth));

        } catch (NoValidAuthentication noValidAuthentication) {
            resp.sendRedirect(LoginController.ENDPOINT + parseUrlSuffix(req));
        }
    }

    @Transactional
    protected Authentication getAuthentication(HttpServletRequest request) throws NoValidAuthentication {
        String authToken = getCookie(request, AUTH_TOKEN_COOKIE);
        if (isEmpty(authToken))
            throw new NoValidAuthentication();

        AuthenticationDAO authDAO = injector.getInstance(AuthenticationDAO.class);
        Authentication auth = authDAO.findById(authToken);

        if (auth == null)
            throw new NoValidAuthentication();

        return auth;
    }

    private boolean requestIsFromIE6OnPort80(HttpServletRequest request) {
        return request.getServerPort() == 80 &&
                request.getServerName().endsWith("activityinfo.org") &&
                !"80".equals(request.getParameter("port")) &&
                request.getHeader("User-Agent").indexOf("MSIE 6.0") != -1;
    }

    private void redirectToPort(HttpServletRequest request, HttpServletResponse response, int port) throws IOException {
        // there is some sort of enormous problem with IE6 and something
        // to do with the tomcat isapi redirector.
        // When *some* IE6 users access the page through the tomcat redirect,
        // IE6 hangs when images are dynamically added to the page
        // This does not happen when accessed directly through the tomcat server.
        //
        // Until we can figure out what the heck is going on and "fix" the redirector,
        // we are just going to redirect all IE6 users to port 8080


        StringBuilder directUrl = new StringBuilder();
        directUrl.append("http://");
        directUrl.append(request.getServerName()).append(":8080");
        directUrl.append(request.getRequestURI());
        if (request.getQueryString() != null && request.getQueryString().length() != 0)
            directUrl.append("?").append(request.getQueryString());

        String bookmark = parseUrlSuffix(request);
        if (bookmark != null && bookmark.length() != 0)
            directUrl.append("#").append(bookmark);

        response.sendRedirect(directUrl.toString());
    }
}
