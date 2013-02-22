package org.activityinfo.server.util.tracking;

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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.activityinfo.server.database.hibernate.entity.User;
import org.activityinfo.server.database.hibernate.entity.UserPermission;
import org.json.JSONObject;

import com.mixpanel.mixpanelapi.MessageBuilder;
import com.mixpanel.mixpanelapi.MixpanelAPI;

@Singleton
public class UpdateProfilesTask extends HttpServlet {

    public static final String END_POINT = "/tasks/mixpanel/updateProfiles";

    private static final Logger LOGGER = Logger
        .getLogger(UpdateProfilesTask.class.getName());

    private final Provider<EntityManager> entityManager;

    @Inject
    public UpdateProfilesTask(Provider<EntityManager> entityManager) {
        super();
        this.entityManager = entityManager;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException {

        MessageBuilder messageBuilder = new MessageBuilder(
            "31eced8bff93159dfee3b10f8e3a0804");
        MixpanelAPI mixpanelAPI = new MixpanelAPI();
        List<User> users = entityManager.get()
            .createQuery("select u from User u").getResultList();
        for (User user : users) {
            try {
                JSONObject msg = messageBuilder.set("u" + user.getId(),
                    propertiesOf(user));
                mixpanelAPI.sendMessage(msg);
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE,
                    "Could not update profile for " + user.getEmail(), e);
            }
        }
    }

    private JSONObject propertiesOf(User user) {
        try {
            Date dateCreated = findCreated(user);
            JSONObject properties = new JSONObject();
            properties.put("$first_name", user.getName());
            properties.put("$email", user.getEmail());
            if (dateCreated != null) {
                properties.put("$last_seen", toString(findLastSeen(user)));
                properties.put("$created", toString(dateCreated));
            }
            properties.put("ownsDbCount", countCreatedDatabases(user));
            properties.put("adminDbCount", countAdminDatabases(user));
            properties.put("lang", user.getLocale());
            properties.put("organization", getPartner(user));
            return properties;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String toString(Date date) {
        return new SimpleDateFormat("yyyy-MM-dd").format(date);
    }

    private Date findLastSeen(User user) {
        return (Date) entityManager
            .get()
            .createQuery(
                "select max(a.dateCreated) from Authentication a where a.user = :user")
            .setParameter("user", user)
            .getSingleResult();
    }

    private Date findCreated(User user) {
        return (Date) entityManager
            .get()
            .createQuery(
                "select min(a.dateCreated) from Authentication a where a.user = :user")
            .setParameter("user", user)
            .getSingleResult();
    }

    private long countCreatedDatabases(User user) {
        return (Long) entityManager
            .get()
            .createQuery(
                "select count(*) from UserDatabase db where db.owner = :user")
            .setParameter("user", user)
            .getSingleResult();
    }

    private String getPartner(User user) {
        List<UserPermission> perms = entityManager.get()
            .createQuery("select p from UserPermission p where p.user = :user")
            .setParameter("user", user)
            .getResultList();

        StringBuilder partners = new StringBuilder();
        for (UserPermission perm : perms) {
            if (partners.length() > 0) {
                partners.append(", ");
            }
            partners.append(perm.getPartner().getName());
        }
        return partners.toString();
    }

    private long countAdminDatabases(User user) {
        return (Long) entityManager
            .get()
            .createQuery(
                "select count(*) from UserPermission p where p.user = :user and p.allowDesign=1")
            .setParameter("user", user)
            .getSingleResult();
    }
}
