

package org.activityinfo.server.bootstrap;

/*
 * #%L
 * ActivityInfo Server
 * %%
 * Copyright (C) 2009 - 2013 UNICEF
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

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
