/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.database.hibernate.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.sigmah.server.database.OnDataSet;
import org.sigmah.server.database.hibernate.dao.UserDAO;
import org.sigmah.server.database.hibernate.dao.UserDAOImpl;
import org.sigmah.server.database.hibernate.entity.User;
import org.sigmah.test.InjectionSupport;
import org.sigmah.test.MockHibernateModule;
import org.sigmah.test.Modules;

import com.google.inject.Inject;

@RunWith(InjectionSupport.class)
@Modules({MockHibernateModule.class})
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
