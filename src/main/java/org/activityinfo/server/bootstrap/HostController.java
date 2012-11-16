/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.server.bootstrap;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.activityinfo.login.shared.AuthenticatedUser;
import org.activityinfo.server.authentication.AuthCookieUtil;
import org.activityinfo.server.bootstrap.exception.NoValidAuthentication;
import org.activityinfo.server.bootstrap.model.HostPageModel;
import org.activityinfo.server.database.hibernate.dao.AuthenticationDAO;
import org.activityinfo.server.database.hibernate.dao.Transactional;
import org.activityinfo.server.database.hibernate.entity.Authentication;
import org.activityinfo.server.util.config.DeploymentConfiguration;
import org.activityinfo.server.util.logging.LogException;

import com.bedatadriven.rebar.appcache.server.UserAgentProvider;
import com.google.common.base.Strings;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;

import freemarker.template.Configuration;

@Singleton
public class HostController extends AbstractController {
    public static final String ENDPOINT = "/";

    private DeploymentConfiguration deployConfig;
    
    @Inject
    public HostController(Injector injector, Configuration templateCfg, DeploymentConfiguration deployConfig) {
        super(injector, templateCfg);
        this.deployConfig = deployConfig;
    }

    @Override
    @LogException(emailAlert = true)
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Authentication auth = getAuthentication(req);
            if("true".equals(req.getParameter("redirect"))) {
                AuthCookieUtil.addAuthCookie(resp, auth, false);
                resp.sendRedirect(HostController.ENDPOINT);
            } else {
                HostPageModel model = new HostPageModel(auth, computeAppUrl(req));
                model.setAppCacheEnabled(checkAppCacheEnabled(req));
                model.setMapsApiKey(deployConfig.getProperty("mapsApiKey"));
				writeView(resp, model);
            }
        } catch (NoValidAuthentication noValidAuthentication) {
            resp.sendRedirect("/content/" + parseUrlSuffix(req));
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
