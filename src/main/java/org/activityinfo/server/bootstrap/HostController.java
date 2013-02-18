/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.server.bootstrap;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import org.activityinfo.server.authentication.AuthCookieUtil;
import org.activityinfo.server.bootstrap.exception.NoValidAuthentication;
import org.activityinfo.server.bootstrap.model.HostPageModel;
import org.activityinfo.server.bootstrap.model.LoginPageModel;
import org.activityinfo.server.bootstrap.model.Redirect;
import org.activityinfo.server.database.hibernate.dao.AuthenticationDAO;
import org.activityinfo.server.database.hibernate.entity.Authentication;
import org.activityinfo.server.util.config.DeploymentConfiguration;
import org.activityinfo.server.util.logging.LogException;
import org.activityinfo.shared.auth.AuthenticatedUser;

import com.bedatadriven.rebar.appcache.server.UserAgentProvider;
import com.google.inject.Inject;

@Path(HostController.ENDPOINT)
public class HostController extends AbstractController {
    public static final String ENDPOINT = "/";

    @Inject
    private DeploymentConfiguration deployConfig;
    
    @GET
    @LogException(emailAlert = true)
    public Response onGet(@Context HttpServletRequest req) throws Exception {
        try {
            Authentication auth = getAuthentication(req);
            if("true".equals(req.getParameter("redirect"))) {
                ResponseBuilder responseBuilder = Response.ok(new Redirect(HostController.ENDPOINT));
                
                AuthCookieUtil.addAuthCookie(responseBuilder, auth, false);
                
				return responseBuilder.build();
            } else {
                HostPageModel model = new HostPageModel(auth, computeAppUrl(req));
                model.setAppCacheEnabled(checkAppCacheEnabled(req));
                model.setMapsApiKey(deployConfig.getProperty("mapsApiKey"));
				return writeView(req, model);
            }
        } catch (NoValidAuthentication noValidAuthentication) {
			return writeView(req, new LoginPageModel());
        }
    }

	private boolean checkAppCacheEnabled(HttpServletRequest req) {
    	// for browsers that only support database synchronisation via gears at this point,
    	// we would rather use gears managed resources stores than HTML5 appcache 
    	// so that we only have to display one permission
    	// (this really only applies to FF <= 3.6 right now)
    	UserAgentProvider userAgentProvider = new UserAgentProvider();
    	return !userAgentProvider.canSupportGears(req);
	}

    protected Authentication getAuthentication(HttpServletRequest request) throws NoValidAuthentication {
        String authToken = request.getParameter("auth");
        if(isEmpty(authToken)) {
            authToken = getCookie(request, AuthenticatedUser.AUTH_TOKEN_COOKIE);
        }
        if (isEmpty(authToken)) {
            throw new NoValidAuthentication();
        }

        AuthenticationDAO authDAO = getInjector().getInstance(AuthenticationDAO.class);
        Authentication auth = authDAO.findById(authToken);

        if (auth == null) {
            throw new NoValidAuthentication();
        }

        return auth;
    }

    private boolean isEmpty(String authToken) {
		return authToken == null || authToken.length() == 0;
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
