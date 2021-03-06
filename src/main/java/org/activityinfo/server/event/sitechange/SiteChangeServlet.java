package org.activityinfo.server.event.sitechange;

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
import java.util.List;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.activityinfo.server.authentication.ServerSideAuthProvider;
import org.activityinfo.server.command.DispatcherSync;
import org.activityinfo.server.database.hibernate.entity.User;
import org.activityinfo.server.mail.MailSender;
import org.activityinfo.shared.command.GetSchema;
import org.activityinfo.shared.command.GetSites;
import org.activityinfo.shared.command.result.SiteResult;
import org.activityinfo.shared.dto.ActivityDTO;
import org.activityinfo.shared.dto.SchemaDTO;
import org.activityinfo.shared.dto.SiteDTO;
import org.activityinfo.shared.dto.UserDatabaseDTO;

import com.google.common.annotations.VisibleForTesting;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

@Singleton
public class SiteChangeServlet extends HttpServlet {
    private static final long serialVersionUID = -7693455083421531780L;

    private static final Logger LOGGER = Logger
        .getLogger(SiteChangeServlet.class.getName());

    public static final String ENDPOINT = "/tasks/notifysitechange";
    public static final String PARAM_USER = "u";
    public static final String PARAM_SITE = "s";
    public static final String PARAM_TYPE = "t";

    private final Provider<EntityManager> entityManager;
    private final Provider<MailSender> mailSender;
    private final ServerSideAuthProvider authProvider;
    private final DispatcherSync dispatcher;

    @Inject
    public SiteChangeServlet(Provider<EntityManager> entityManager,
        Provider<MailSender> mailSender,
        ServerSideAuthProvider authProvider,
        DispatcherSync dispatcher) {
        this.entityManager = entityManager;
        this.mailSender = mailSender;
        this.authProvider = authProvider;
        this.dispatcher = dispatcher;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException {
        try {
            int userId = Integer.parseInt(req.getParameter(PARAM_USER));
            int siteId = Integer.parseInt(req.getParameter(PARAM_SITE));
            ChangeType type = ChangeType.valueOf(req.getParameter(PARAM_TYPE));
            if (type.isKnown()) {
                sendNotifications(userId, siteId, type);
            }

        } catch (Throwable t) {
            LOGGER.warning("can't complete notify task: " + t.getMessage());
            LOGGER.throwing(this.getClass().getSimpleName(), "doGet", t);
        }
    }

    @VisibleForTesting
    void sendNotifications(int editorUserId, int siteId, ChangeType type) {
        User user = entityManager.get().find(User.class, editorUserId);

        /*
         * For our purposes, the user who initiated the change will be
         * considered the authenticated user for this thread
         */

        authProvider.set(user);

        SiteResult siteResult = dispatcher.execute(GetSites.byId(siteId));
        SiteDTO siteDTO = siteResult.getData().get(0);

        SchemaDTO schemaDTO = dispatcher.execute(new GetSchema());
        ActivityDTO activityDTO = schemaDTO.getActivityById(siteDTO
            .getActivityId());
        UserDatabaseDTO userDatabaseDTO = activityDTO.getDatabase();

        Date date = new Date();

        List<User> recipients = findRecipients(userDatabaseDTO.getId());
        for (User recipient : recipients) {
            try {
                // do not send users who modified the report an email to
                // themselves!
                if (recipient.getId() != editorUserId) {
                    LOGGER.info("sending sitechange notification email to "
                        + recipient.getEmail());

                    UpdateMessageBuilder message = new UpdateMessageBuilder();
                    message.setDate(date);
                    message.setEditor(user);
                    message.setRecipient(recipient);
                    message.setUserDatabaseDTO(userDatabaseDTO);
                    message.setSiteDTO(siteDTO);
                    message.setActivityDTO(activityDTO);
                    message.setChangeType(type);

                    mailSender.get().send(message.build());
                }

            } catch (Throwable t) {
                LOGGER.warning("failed sending notification email to "
                    + recipient.getName() + " <" + recipient.getEmail() + ">: "
                    + t.getMessage());
                t.printStackTrace();
            }
        }
    }

    // select owners/designers with the emailNotification flag set to true
    @VisibleForTesting
    @SuppressWarnings("unchecked")
    List<User> findRecipients(int userDatabaseId) {
        Query query = entityManager
            .get()
            .createNativeQuery(
                "select u.* from userlogin u "
                    +
                    "where u.userid in ("
                    +
                    "select p.userid uid from userpermission p where p.databaseid = ?1 and p.allowdesign = b'1' "
                    +
                    "union "
                    +
                    "select d.owneruserid uid from userdatabase d where d.DatabaseId = ?2 "
                    +
                    ") and u.emailnotification = b'1'",
                User.class)
            .setParameter(1, userDatabaseId)
            .setParameter(2, userDatabaseId);

        return query.getResultList();
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException {
        doGet(request, response);
    }
}
