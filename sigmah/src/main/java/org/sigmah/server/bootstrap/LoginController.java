/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.bootstrap;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import freemarker.template.Configuration;

import org.sigmah.server.auth.Authenticator;
import org.sigmah.server.bootstrap.model.LoginPageModel;
import org.sigmah.server.util.logging.LogException;
import org.sigmah.shared.domain.User;
import org.sigmah.shared.exception.InvalidLoginException;

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
    @LogException(emailAlert = true)
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        writeView(resp, new LoginPageModel(parseUrlSuffix(req)));
    }

    @Override
    @LogException(emailAlert = true)
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
