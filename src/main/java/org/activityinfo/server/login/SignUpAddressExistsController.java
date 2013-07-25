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
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.activityinfo.server.authentication.SecureTokenGenerator;
import org.activityinfo.server.database.hibernate.dao.Transactional;
import org.activityinfo.server.database.hibernate.dao.UserDAO;
import org.activityinfo.server.database.hibernate.entity.User;
import org.activityinfo.server.login.model.SignUpAddressExistsPageModel;
import org.activityinfo.server.mail.MailSender;
import org.activityinfo.server.mail.ResetPasswordMessage;
import org.activityinfo.server.util.logging.LogException;

import com.google.inject.Inject;
import com.sun.jersey.api.view.Viewable;

@Path(SignUpAddressExistsController.ENDPOINT)
public class SignUpAddressExistsController {
    public static final String ENDPOINT = "/signUpAddressExists";
    
    private static final Logger LOGGER = Logger.getLogger(SignUpAddressExistsController.class.getName());

    @Inject
    private MailSender mailer;

    @Inject
    private Provider<UserDAO> userDAO;

    @POST
    @Produces(MediaType.TEXT_HTML)
    @LogException(emailAlert = true)
    @Transactional
    public Viewable resetPassword(@FormParam("email") String email) {
        try {
            User user = userDAO.get().findUserByEmail(email);
            user.setChangePasswordKey(SecureTokenGenerator.generate());
            user.setDateChangePasswordKeyIssued(new Date());

            mailer.send(new ResetPasswordMessage(user));

            return new SignUpAddressExistsPageModel(email).asEmailSent();

        } catch (NoResultException e) {
            return new SignUpAddressExistsPageModel().asLoginError();

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to send password reset email", e);
            return new SignUpAddressExistsPageModel().asEmailError();
        }
    }
}
