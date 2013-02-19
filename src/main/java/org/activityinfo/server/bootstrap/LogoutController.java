/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.server.bootstrap;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.activityinfo.shared.auth.AuthenticatedUser;

@Path(LogoutController.ENDPOINT)
public class LogoutController {
    public static final String ENDPOINT = "/logout";

    @GET
    public Response logout(@Context UriInfo uri) throws ServletException, IOException {
        return Response.seeOther(uri.getAbsolutePathBuilder().replacePath(LoginController.ENDPOINT).build())
        .cookie(emptyCookies())
        .build();
    }
   
    private NewCookie[] emptyCookies() {
        return new NewCookie[] {
            new NewCookie(AuthenticatedUser.AUTH_TOKEN_COOKIE, null),
            new NewCookie(AuthenticatedUser.EMAIL_COOKIE, null),
            new NewCookie(AuthenticatedUser.USER_ID_COOKIE, null),
            new NewCookie(AuthenticatedUser.USER_LOCAL_COOKIE, null)   
        };
    }
}
