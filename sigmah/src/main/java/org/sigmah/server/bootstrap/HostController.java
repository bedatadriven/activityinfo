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
import org.sigmah.server.bootstrap.exception.NoValidAuthentication;
import org.sigmah.server.bootstrap.model.HostPageModel;
import org.sigmah.server.dao.AuthenticationDAO;
import org.sigmah.server.dao.Transactional;
import org.sigmah.server.domain.Authentication;
import org.sigmah.server.util.logging.LogException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.sigmah.server.util.StringUtil.isEmpty;

@Singleton
public class HostController extends AbstractController {
    public static final String ENDPOINT = "";

    @Inject
    public HostController(Injector injector, Configuration templateCfg) {
        super(injector, templateCfg);
    }

    @Override
    @LogException(emailAlert = true)
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Authentication auth = getAuthentication(req);
            if("true".equals(req.getParameter("redirect"))) {
                Cookies.addAuthCookie(resp, auth, false);
                resp.sendRedirect(HostController.ENDPOINT);
            } else {
                writeView(resp, new HostPageModel(auth, computeAppUrl(req)));
            }
        } catch (NoValidAuthentication noValidAuthentication) {
            resp.sendRedirect(LoginController.ENDPOINT + parseUrlSuffix(req));
        }
    }

    @Transactional
    protected Authentication getAuthentication(HttpServletRequest request) throws NoValidAuthentication {
        String authToken = request.getParameter("auth");
        if(isEmpty(authToken)) {
            authToken = getCookie(request, Cookies.AUTH_TOKEN_COOKIE);
        }
        if (isEmpty(authToken)) {
            throw new NoValidAuthentication();
        }

        AuthenticationDAO authDAO = injector.getInstance(AuthenticationDAO.class);
        Authentication auth = authDAO.findById(authToken);

        if (auth == null) {
            throw new NoValidAuthentication();
        }

        return auth;
    }

    /**
     * @return  The url used for the desktop shortcut
     */
    private String computeAppUrl(HttpServletRequest request) {
        StringBuilder url = new StringBuilder();
        url.append("http://");
        url.append(request.getServerName());
        if(request.getServerPort() != 80) {
           url.append(":").append(request.getServerPort());
        }
        url.append(request.getRequestURI());
        return url.toString();
    }
}
