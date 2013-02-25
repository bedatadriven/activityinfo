package org.activityinfo.server.login;

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

import java.io.IOException;
import java.net.URI;

import javax.inject.Provider;
import javax.persistence.NoResultException;
import javax.servlet.ServletException;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.activityinfo.server.database.hibernate.dao.Transactional;
import org.activityinfo.server.database.hibernate.dao.UserDAO;
import org.activityinfo.server.database.hibernate.entity.User;
import org.activityinfo.server.login.exception.IncompleteFormException;
import org.activityinfo.server.login.model.ChangePasswordPageModel;
import org.activityinfo.server.login.model.InvalidInvitePageModel;
import org.activityinfo.server.util.logging.LogException;

import com.google.inject.Inject;
import com.sun.jersey.api.view.Viewable;

@Path(ChangePasswordController.ENDPOINT)
public class ChangePasswordController {
    public static final String ENDPOINT = "/changePassword";

    private final Provider<UserDAO> userDAO;

    @Inject
    public ChangePasswordController(Provider<UserDAO> userDAO) {
        super();
        this.userDAO = userDAO;
    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    public Viewable getPage(@Context UriInfo uri) throws Exception {
        try {
            User user = userDAO.get().findUserByChangePasswordKey(
                uri.getRequestUri().getQuery());
            return new ChangePasswordPageModel(user).asViewable();
        } catch (NoResultException e) {
            return new InvalidInvitePageModel().asViewable();
        }
    }

    @POST
    @LogException(emailAlert = true)
    public Response changePassword(@Context UriInfo uri,
        @FormParam("key") String key, @FormParam("password") String password)
        throws IOException, ServletException {
        User user = null;
        try {
            user = userDAO.get().findUserByChangePasswordKey(key);
        } catch (NoResultException e) {
            return Response.ok()
                .entity(new InvalidInvitePageModel().asViewable())
                .type(MediaType.TEXT_HTML)
                .build();
        }

        changePassword(user, password);

        URI appUri = uri.getAbsolutePathBuilder().replacePath("/").build();

        return Response.seeOther(appUri)
            .build();
    }

    @Transactional
    protected void changePassword(User user, String newPassword)
        throws IncompleteFormException {
        user.changePassword(newPassword);
        user.clearChangePasswordKey();
        user.setNewUser(false);
    }
}
