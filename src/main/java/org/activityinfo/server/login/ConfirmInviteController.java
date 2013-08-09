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

import javax.inject.Provider;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.activityinfo.server.database.hibernate.dao.UserDAO;
import org.activityinfo.server.database.hibernate.entity.User;
import org.activityinfo.server.login.model.ConfirmInvitePageModel;
import org.activityinfo.server.login.model.InvalidInvitePageModel;
import org.activityinfo.server.util.MailingListClient;
import org.activityinfo.server.util.logging.LogException;

import com.google.common.base.Strings;
import com.google.inject.Inject;
import com.sun.jersey.api.view.Viewable;

@Path(ConfirmInviteController.ENDPOINT)
public class ConfirmInviteController {
    public static final String ENDPOINT = "/confirm";

    private final Provider<UserDAO> userDAO;
    private final AuthTokenProvider authTokenProvider;
    private final MailingListClient mailingList;

    @Inject
    public ConfirmInviteController(Provider<UserDAO> userDAO,
        AuthTokenProvider authTokenProvider,
        MailingListClient mailingList) {
        super();
        this.userDAO = userDAO;
        this.mailingList = mailingList;
        this.authTokenProvider = authTokenProvider;
    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    @LogException(emailAlert = true)
    public Viewable getPage(@Context UriInfo uri) throws Exception {
        try {
            User user = userDAO.get().findUserByChangePasswordKey(
                uri.getRequestUri().getQuery());
            return new ConfirmInvitePageModel(user).asViewable();

        } catch (NoResultException e) {
            return new InvalidInvitePageModel().asViewable();
        }
    }

    @POST
    @LogException(emailAlert = true)
    public Response confirm(
        @Context UriInfo uri,
        @FormParam("key") String key,
        @FormParam("locale") String locale,
        @FormParam("password") String password,
        @FormParam("name") String name,
        @FormParam("newsletter") boolean newsletter) throws Exception {

        User user = null;
        try {
            user = userDAO.get().findUserByChangePasswordKey(key);
            user.setName(checkNonEmpty(name));
            user.setLocale(checkNonEmpty(locale));
            user.changePassword(checkNonEmpty(password));
            user.clearChangePasswordKey();
            
            if(newsletter) {
                mailingList.subscribe(user);
            }
            
            return Response
                .seeOther(uri.getAbsolutePathBuilder().replacePath("/").build())
                .cookie(authTokenProvider.createNewAuthCookies(user))
                .build();

        } catch (EntityNotFoundException e) {
            return Response.ok(new InvalidInvitePageModel().asViewable())
                .type(MediaType.TEXT_HTML)
                .build();
        } catch (IllegalArgumentException e) {
            return Response.ok(
                ConfirmInvitePageModel.incompleteForm(user).asViewable())
                .type(MediaType.TEXT_HTML)
                .build();
        }
    }

    private String checkNonEmpty(String value) {
        if (Strings.isNullOrEmpty(value)) {
            throw new IllegalArgumentException();
        }
        return value;
    }
}
