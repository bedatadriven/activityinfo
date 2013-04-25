package org.activityinfo.server.database.hibernate.dao;

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

import javax.persistence.EntityManager;

import org.activityinfo.server.authentication.SecureTokenGenerator;
import org.activityinfo.server.database.hibernate.entity.User;

import com.google.inject.Inject;

/**
 * @author Alex Bertram
 */
public class UserDAOImpl extends GenericDAO<User, Integer> implements UserDAO {

    @Inject
    public UserDAOImpl(EntityManager em) {
        super(em);
    }

    @Override
    public boolean doesUserExist(String email) {
        return getEntityManager().createNamedQuery("findUserByEmail")
            .setParameter("email", email)
            .getResultList().size() == 1;
    }

    @Override
    public User findUserByEmail(String email) {

        return (User) getEntityManager().createNamedQuery("findUserByEmail")
            .setParameter("email", email)
            .getSingleResult();
    }

    @Override
    public User findUserByChangePasswordKey(String key) {
        return (User) getEntityManager()
            .createNamedQuery("findUserByChangePasswordKey")
            .setParameter("key", key)
            .getSingleResult();
    }

    /**
     * Initializes this User as a new User with a secure changePasswordKey
     */
    public static User createNewUser(String email, String name, String locale) {
        return createNewUser(email, name, "", "", locale);
    }

    /**
     * Initializes this User as a new User with a secure changePasswordKey
     */
    public static User createNewUser(String email, String name, String organization, String jobtitle, String locale) {
        User user = new User();
        user.setEmail(email);
        user.setName(name);
        user.setOrganization(organization);
        user.setJobtitle(jobtitle);
        user.setEmailNotification(false);
        user.setLocale(locale);
        user.setChangePasswordKey(SecureTokenGenerator.generate());
        return user;
    }
}
