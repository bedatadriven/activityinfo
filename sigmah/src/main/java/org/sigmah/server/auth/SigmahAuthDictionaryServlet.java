/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.auth;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.sigmah.server.domain.Authentication;
import org.sigmah.server.Cookies;
import org.sigmah.server.dao.AuthenticationDAO;
import org.sigmah.shared.domain.User;

/**
 * Creates and returns the current user informations.
 * @author Raphaël Calabro (rcalabro@ideia.fr)
 */
@Singleton
public class SigmahAuthDictionaryServlet extends HttpServlet {

    @Inject
    private Injector injector;

    private String getAuthToken(Cookie[] cookies) {
        for(final Cookie cookie : cookies) {
            if(Cookies.AUTH_TOKEN_COOKIE.equals(cookie.getName())) {
                return cookie.getValue();
            }
        }

        return null;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if(req.getParameter("remove") != null) {
            final Cookie cookie = new Cookie("authToken", "");
            cookie.setMaxAge(0);
            resp.addCookie(cookie);
            
        } else {
            final HashMap<String, String> parameters = new HashMap<String, String>();
            parameters.put("showActivityInfoMenus", "true");

            final String authToken = getAuthToken(req.getCookies());
            if(authToken != null) {
                AuthenticationDAO authDAO = injector.getInstance(AuthenticationDAO.class);
                Authentication auth = authDAO.findById(authToken);

                final User user = auth.getUser();

                parameters.put("connectedUserId", Integer.toString(user.getId()));
                parameters.put("connectedUserAuthToken", '"'+authToken+'"');
                parameters.put("connectedUserEmail", '"'+user.getEmail()+'"');
                parameters.put("connectedUserOrgId", Integer.toString(user.getOrganization().getId()));
            }

            final ServletOutputStream output = resp.getOutputStream();
            output.println("var SigmahParams = {");

            boolean first = true;
            for(Map.Entry<String, String> entry : parameters.entrySet()) {
                if(first)
                    first = false;
                else
                    output.println(",");

                output.print(entry.getKey()+": "+entry.getValue());
            }

            output.println("};");
        }
    }

}
