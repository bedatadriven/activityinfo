/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.server.bootstrap;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.activityinfo.server.bootstrap.model.Redirect;
import org.activityinfo.shared.auth.AuthenticatedUser;

@Path(LogoutController.ENDPOINT)
public class LogoutController extends AbstractController {
    public static final String ENDPOINT = "/logout";

    @GET
    public Response onGet(@Context HttpServletRequest req) throws ServletException, IOException {
    	ResponseBuilder resp = Response.ok(new Redirect(LoginController.ENDPOINT));
    	
        logUserOut(resp);
        
        return resp.build();
    }
    
    static final Collection<String> COOKIES_TO_DELETE = Arrays.asList(AuthenticatedUser.AUTH_TOKEN_COOKIE, AuthenticatedUser.EMAIL_COOKIE, AuthenticatedUser.USER_ID_COOKIE, AuthenticatedUser.USER_LOCAL_COOKIE);

    protected void logUserOut(ResponseBuilder resp) {
		for (String ctd : COOKIES_TO_DELETE) {
			resp.cookie(new NewCookie(ctd, null));
		}
    }
}
