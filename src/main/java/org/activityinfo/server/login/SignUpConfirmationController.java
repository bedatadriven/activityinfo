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

import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Provider;
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

import org.activityinfo.server.database.hibernate.dao.PartnerDAO;
import org.activityinfo.server.database.hibernate.dao.UserDAO;
import org.activityinfo.server.database.hibernate.dao.UserDatabaseDAO;
import org.activityinfo.server.database.hibernate.dao.UserPermissionDAO;
import org.activityinfo.server.database.hibernate.entity.Partner;
import org.activityinfo.server.database.hibernate.entity.User;
import org.activityinfo.server.database.hibernate.entity.UserDatabase;
import org.activityinfo.server.database.hibernate.entity.UserPermission;
import org.activityinfo.server.login.model.SignUpConfirmationInvalidPageModel;
import org.activityinfo.server.login.model.SignUpConfirmationPageModel;
import org.activityinfo.server.util.MailingListClient;
import org.activityinfo.server.util.logging.LogException;

import com.google.common.base.Strings;
import com.google.inject.Inject;
import com.sun.jersey.api.view.Viewable;

@Path(SignUpConfirmationController.ENDPOINT)
public class SignUpConfirmationController {
    public static final String ENDPOINT = "/signUpConfirmation";

    private static final Logger LOGGER = Logger.getLogger(SignUpConfirmationController.class.getName());

    private static final int MAX_PARAM_LENGTH = 200;

    private static final int DEFAULT_DATABASE_ID = 507; // training DB
    private static final int DEFAULT_PARTNER_ID = 274; // bedatadriven

    private final MailingListClient mailingList;
    
    private final Provider<UserDAO> userDAO;
    private final Provider<UserDatabaseDAO> databaseDAO;
    private final Provider<PartnerDAO> partnerDAO;
    private final Provider<UserPermissionDAO> permissionDAO;
    private final AuthTokenProvider authTokenProvider;

    @Inject
    public SignUpConfirmationController(Provider<UserDAO> userDAO, Provider<UserDatabaseDAO> databaseDAO,
        Provider<PartnerDAO> partnerDAO, Provider<UserPermissionDAO> permissionDAO,
        MailingListClient mailChimp,
        AuthTokenProvider authTokenProvider) {
        super();
        this.userDAO = userDAO;
        this.databaseDAO = databaseDAO;
        this.partnerDAO = partnerDAO;
        this.permissionDAO = permissionDAO;
        this.authTokenProvider = authTokenProvider;
        this.mailingList = mailChimp;
    }

    @GET
    @Produces(MediaType.TEXT_HTML)
    @LogException(emailAlert = true)
    public Viewable getPage(@Context UriInfo uri) throws Exception {
        try {
            User user = userDAO.get().findUserByChangePasswordKey(uri.getRequestUri().getQuery());
            return new SignUpConfirmationPageModel(user.getChangePasswordKey()).asViewable();
        } catch (NoResultException e) {
            return new SignUpConfirmationInvalidPageModel().asViewable();
        }
    }

    @POST
    @LogException(emailAlert = true)
    public Response confirm(
        @Context UriInfo uri,
        @FormParam("key") String key,
        @FormParam("password") String password,
        @FormParam("newsletter") boolean newsletter) {

        try {
            // check params
            checkParam(key, true);
            checkParam(password, true);

            // confirm user
            User user = userDAO.get().findUserByChangePasswordKey(key);
            user.changePassword(password);
            user.clearChangePasswordKey();

            // add user to default database
            addUserToDefaultDatabase(user);

            if(newsletter) {
                mailingList.subscribe(user);
            }
            
            // go to the homepage
            return Response.seeOther(uri.getAbsolutePathBuilder().replacePath("/").build())
                .cookie(authTokenProvider.createNewAuthCookies(user)).build();

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Exception during signup process", e);
            return Response.ok(SignUpConfirmationPageModel.genericErrorModel(key).asViewable())
                .type(MediaType.TEXT_HTML).build();
        }
    }

    @LogException(emailAlert=true)
    protected void addUserToDefaultDatabase(User user) {
        UserDatabase database = databaseDAO.get().findById(DEFAULT_DATABASE_ID);
        Partner partner = partnerDAO.get().findById(DEFAULT_PARTNER_ID);
        UserPermission permission = new UserPermission(database, user);
        permission.setPartner(partner);
        permission.setAllowView(true);
        permission.setAllowViewAll(true);
        permission.setLastSchemaUpdate(new Date());
        permissionDAO.get().persist(permission);
    }

    private void checkParam(String value, boolean required) {
        boolean illegal = false;
        illegal |= (required && Strings.isNullOrEmpty(value));
        illegal |= (value != null && value.length() > MAX_PARAM_LENGTH); // sanity check

        if (illegal) {
            throw new IllegalArgumentException();
        }
    }
}
