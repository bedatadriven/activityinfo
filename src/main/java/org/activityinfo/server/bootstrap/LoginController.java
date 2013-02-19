/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.server.bootstrap;

import javax.inject.Provider;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.activityinfo.server.authentication.Authenticator;
import org.activityinfo.server.bootstrap.exception.LoginException;
import org.activityinfo.server.bootstrap.exception.PasswordExpiredException;
import org.activityinfo.server.bootstrap.model.LoginPageModel;
import org.activityinfo.server.database.hibernate.dao.UserDAO;
import org.activityinfo.server.database.hibernate.entity.User;
import org.activityinfo.server.util.logging.LogException;

import com.google.inject.Inject;
import com.sun.jersey.api.view.Viewable;

@Path(LoginController.ENDPOINT)
public class LoginController  {
    
	public static final String ENDPOINT = "/login";

	@Inject
	private Provider<Authenticator> authenticator;
	
	@Inject
	private Provider<AuthTokenProvider> authTokenProvider;
	
	@Inject
	private Provider<UserDAO> userDAO;

    
    @GET
    @LogException(emailAlert = true)
    public Viewable getLoginPage(@Context UriInfo uri) throws Exception {
        return new LoginPageModel().asViewable();
    }

    @POST
    @Path("ajax")
    @LogException(emailAlert = true)
	public Response ajaxLogin(
	    @FormParam("email") String email, 
	    @FormParam("password") String password ) throws Exception {
		
        User user = userDAO.get().findUserByEmail(email);
        checkPassword(password, user);
        
        return Response
        .ok()
        .cookie(authTokenProvider.get().createNewAuthCookies(user))
        .build();
    }
    
    @POST
    @LogException(emailAlert = true)
    public Response login(
        @Context UriInfo uri,
        @FormParam("email") String email, 
        @FormParam("password") String password ) throws Exception {
        
        User user;
        try {
            user = userDAO.get().findUserByEmail(email);
            checkPassword(password, user);
        } catch(LoginException e) {
            return Response.ok(LoginPageModel.unsuccessful().asViewable())
                .build();
        }
        
        return Response
        .seeOther(uri.getAbsolutePathBuilder().replacePath("/").build())
        .cookie(authTokenProvider.get().createNewAuthCookies(user))
        .build();
    }
    
    
	private void checkPassword(String password, User user) throws LoginException {

		if (!authenticator.get().check(user, password)) {
			throw new LoginException();
		}
	}
}
