/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.bootstrap;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import freemarker.template.Configuration;
import org.sigmah.server.Cookies;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Singleton
public class LogoutController extends AbstractController {
    public static final String ENDPOINT = "logout";

    @Inject
    public LogoutController(Injector injector, Configuration templateCfg) {
        super(injector, templateCfg);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logUserOut(resp);
        delegateGet(LoginController.class, req, resp);
    }

    protected void logUserOut(HttpServletResponse resp) {
        removeCookie(resp, Cookies.AUTH_TOKEN_COOKIE);
    }
}
