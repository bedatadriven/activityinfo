/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.endpoint.gwtrpc;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.sigmah.server.dao.OnDataSet;
import org.sigmah.shared.command.GetUsers;
import org.sigmah.shared.command.result.UserResult;
import org.sigmah.shared.exception.CommandException;
import org.sigmah.test.InjectionSupport;

@RunWith(InjectionSupport.class)
@OnDataSet("/dbunit/sites-simple1.db.xml")
public class GetUsersTest extends CommandTestCase {

    private final static int DATABASE_OWNER = 1;

    @Test
    public void testUnsorted() throws CommandException {
        setUser(DATABASE_OWNER);

        GetUsers cmd = new GetUsers(1);
        UserResult result = (UserResult) execute(cmd);

        Assert.assertEquals(3, result.getData().size());
    }

    /**
     * Verify that users with ManageUsers permission can get a list of users
     * within their organisation
     */
    @Test
    public void testManageUsersPermission() throws CommandException {
        // populate with a known state and authenticate as user 3, who
        // has ManageUser permissions for Solidarites
        setUser(3); // Lisa from Solidarites

        // execute
        UserResult result = (UserResult) execute(new GetUsers(1));

        // VERIFY that we have 1 result:
        // - the one other solidarites user

        Assert.assertEquals("number of results", 1, result.getTotalLength());
        Assert.assertEquals("user name", "Marlene", result.getData().get(0).getName());
    }

    @Test
    public void testManageAllUsersPermission() throws CommandException {

        setUser(2); // Bavon from NRC(with manageAllUsers) permission

        // execute
        UserResult result = (UserResult) execute(new GetUsers(1));

        // VERIFY that we can get can see the two other users from NRC
        Assert.assertEquals("number of results", 2, result.getTotalLength());
    }
}
