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
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Provider;
import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.activityinfo.server.database.hibernate.dao.Transactional;
import org.activityinfo.server.database.hibernate.dao.UserDAO;
import org.activityinfo.server.database.hibernate.dao.UserDAOImpl;
import org.activityinfo.server.database.hibernate.entity.User;
import org.activityinfo.server.login.model.SignUpPageModel;
import org.activityinfo.server.mail.MailSender;
import org.activityinfo.server.mail.SignUpConfirmationMessage;
import org.activityinfo.server.util.logging.LogException;

import com.google.common.base.Strings;
import com.google.inject.Inject;
import com.sun.jersey.api.view.Viewable;

@Path(SignUpController.ENDPOINT)
public class SignUpController {
    public static final String ENDPOINT = "/signUp*";
    
    private static final Logger LOGGER = Logger.getLogger(SignUpController.class.getName());

    private static final int MAX_PARAM_LENGTH = 200;

    @Inject
    private MailSender mailer;

    @Inject
    private Provider<UserDAO> userDAO;

    @Inject
    private EntityManager entityManager;

    @GET
    @Produces(MediaType.TEXT_HTML)
    @LogException(emailAlert = true)
    public Viewable getPage(@Context HttpServletRequest req)
        throws ServletException, IOException {
        return new SignUpPageModel().asViewable();
    }

    @POST
    @Produces(MediaType.TEXT_HTML)
    @LogException(emailAlert = true)
    @Transactional
    public Viewable signUp(
        @FormParam("name") String name,
        @FormParam("organization") String organization,
        @FormParam("jobtitle") String jobtitle,
        @FormParam("email") String email,
        @FormParam("locale") String locale) {

        LOGGER.info("New user signing up! [name: " + name + ", email: " + email
            + ", locale: " + locale + ", organization: " + organization + ", job title: " + jobtitle + "]");

        // checking parameter values
        try {
            checkParam(name, true);
            checkParam(organization, false);
            checkParam(jobtitle, false);
            checkParam(email, true);
            checkParam(locale, true);
        } catch (IllegalArgumentException e) {
            LOGGER.log(Level.INFO, "User " + name + " (" + email + ") failed to sign up", e);
            return SignUpPageModel.formErrorModel()
                .set(email, name, organization, jobtitle, locale)
                .asViewable();
        }

        try {
            // check duplicate email
            if (userDAO.get().doesUserExist(email)) {
                return SignUpPageModel.emailExistsErrorModel()
                    .set(email, name, organization, jobtitle, locale)
                    .asViewable();
            }
            
            // persist new user
            User user = UserDAOImpl.createNewUser(email, name, organization, jobtitle, locale);
            userDAO.get().persist(user);
    
            // send confirmation email
            mailer.send(new SignUpConfirmationMessage(user));

            // return to page with positive result
            return SignUpPageModel.confirmationEmailSentModel().asViewable();

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "User " + name + " (" + email + ") failed to sign up", e);
            entityManager.getTransaction().rollback();
            return SignUpPageModel.genericErrorModel()
                .set(email, name, organization, jobtitle, locale)
                .asViewable();
        }
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
