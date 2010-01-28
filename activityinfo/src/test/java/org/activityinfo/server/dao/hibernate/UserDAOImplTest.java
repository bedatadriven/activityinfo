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

package org.activityinfo.server.dao.hibernate;

import com.google.inject.Inject;
import org.activityinfo.server.dao.OnDataSet;
import org.activityinfo.server.dao.UserDAO;
import org.activityinfo.server.domain.User;
import org.activityinfo.test.InjectionSupport;
import org.activityinfo.test.Modules;
import org.activityinfo.test.TestingHibernateModule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

@RunWith(InjectionSupport.class)
@Modules({TestingHibernateModule.class})
@OnDataSet("/dbunit/schema1.db.xml")
public class UserDAOImplTest {

    private UserDAO userDAO;

    @Inject
    public UserDAOImplTest(UserDAOImpl userDAO) {
        this.userDAO = userDAO;
    }

    @Test
    public void testDoesUserExist() throws Exception {
        assertTrue(userDAO.doesUserExist("bavon@nrcdrc.org"));
    }

    @Test
    public void testDoesUserExistWhenNoUser() throws Exception {
        assertFalse(userDAO.doesUserExist("nonexistantuser@solidarites.org"));
    }

    @Test
    public void testFindUserByEmail() throws Exception {
        User user = userDAO.findUserByEmail("bavon@nrcdrc.org");

        assertEquals("id", 2, user.getId());
    }

    @Test
    public void testFindUserByChangePasswordKey() throws Exception {
    }
}
