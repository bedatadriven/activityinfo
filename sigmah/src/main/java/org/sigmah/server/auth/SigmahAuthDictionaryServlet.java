/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.auth;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.sigmah.client.inject.SigmahAuthProvider;
import org.sigmah.server.Cookies;
import org.sigmah.server.dao.AuthenticationDAO;
import org.sigmah.server.domain.Authentication;
import org.sigmah.server.endpoint.gwtrpc.handler.GetUserInfoHandler;
import org.sigmah.shared.domain.User;
import org.sigmah.shared.dto.profile.ProfileDTO;
import org.sigmah.shared.dto.profile.ProfileUtils;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;

/**
 * Creates and returns the current user informations.
 * 
 * @author Raphaël Calabro (rcalabro@ideia.fr)
 */
@Singleton
public class SigmahAuthDictionaryServlet extends HttpServlet {

    private static final Log log = LogFactory.getLog(SigmahAuthDictionaryServlet.class);

    private static final long serialVersionUID = -1298849337754771926L;

    @Inject
    private Injector injector;

    private String getAuthToken(Cookie[] cookies) {
        for (final Cookie cookie : cookies) {
            if (Cookies.AUTH_TOKEN_COOKIE.equals(cookie.getName())) {
                return cookie.getValue();
            }
        }

        return null;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getParameter("remove") != null) {
            final Cookie cookie = new Cookie("authToken", "");
            cookie.setPath("/");
            cookie.setMaxAge(0);
            resp.addCookie(cookie);

        } else {
            final HashMap<String, String> parameters = new HashMap<String, String>();
            parameters.put(SigmahAuthProvider.SHOW_MENUS, String.valueOf(true));

            final String authToken = getAuthToken(req.getCookies());
            if (authToken != null) {
                final AuthenticationDAO authDAO = injector.getInstance(AuthenticationDAO.class);
                final Authentication auth = authDAO.findById(authToken);

                final User user = auth.getUser();

                parameters.put(SigmahAuthProvider.USER_ID, Integer.toString(user.getId()));
                parameters.put(SigmahAuthProvider.USER_TOKEN, '"' + authToken + '"');
                parameters.put(SigmahAuthProvider.USER_EMAIL, '"' + user.getEmail() + '"');
                parameters.put(SigmahAuthProvider.USER_NAME, '"' + user.getName() + '"');
                parameters.put(SigmahAuthProvider.USER_FIRST_NAME, '"' + user.getFirstName() + '"');
                parameters.put(SigmahAuthProvider.USER_ORG_ID, Integer.toString(user.getOrganization().getId()));
                parameters.put(SigmahAuthProvider.USER_ORG_UNIT_ID,
                        Integer.toString(user.getOrgUnitWithProfiles().getOrgUnit().getId()));

                // Custom serialization of the profile.
                final GetUserInfoHandler infoHandler = injector.getInstance(GetUserInfoHandler.class);
                final ProfileDTO aggregatedProfile = infoHandler.aggregateProfiles(user, null);
                final String aggregatedProfileAsString = ProfileUtils.writeProfile(aggregatedProfile);
                parameters.put(SigmahAuthProvider.USER_AG_PROFILE, '"' + aggregatedProfileAsString + '"');
                if (log.isDebugEnabled()) {
                    log.debug("[doGet] Writes aggregated profile: " + aggregatedProfile);
                    log.debug("[doGet] String representation of the profile: " + aggregatedProfileAsString);
                }
            }

            final Charset utf8 = Charset.forName("UTF-8");
            resp.setCharacterEncoding("UTF-8");

            final ServletOutputStream output = resp.getOutputStream();
            output.println("var " + SigmahAuthProvider.DICTIONARY_NAME + " = {");

            boolean first = true;
            for (Map.Entry<String, String> entry : parameters.entrySet()) {
                if (first)
                    first = false;
                else
                    output.println(",");

                output.print(entry.getKey() + ": ");
                output.write(entry.getValue().getBytes(utf8));
            }

            output.println("};");
        }
    }
}
