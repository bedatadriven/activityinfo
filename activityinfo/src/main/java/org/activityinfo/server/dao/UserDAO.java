/*
 * This file is part of ActivityInfo.
 *
 * ActivityInfo is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * ActivityInfo is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with ActivityInfo.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright 2010 Alex Bertram and contributors.
 */

package org.activityinfo.server.dao;

import com.google.inject.ImplementedBy;
import org.activityinfo.server.dao.hibernate.UserDAOImpl;
import org.activityinfo.server.domain.User;

import javax.persistence.EntityNotFoundException;

@ImplementedBy(UserDAOImpl.class)
public interface UserDAO extends DAO<User, Integer> {

    boolean doesUserExist(String email);

    User findUserByEmail(String email)
            throws EntityNotFoundException;

    User findUserByChangePasswordKey(String key);
}
