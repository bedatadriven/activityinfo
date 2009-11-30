package org.activityinfo.server.command;

import org.activityinfo.shared.command.GetUsers;
import org.activityinfo.shared.command.result.UserResult;
import org.activityinfo.shared.exception.CommandException;
import org.junit.Assert;
import org.junit.Test;

public class GetUsersTest extends CommandTestCase {


    @Test
    public void testUnsorted() throws CommandException {

        populate("sites-simple1");

        setUser(1);

        GetUsers cmd = new GetUsers(1);
        UserResult result = (UserResult)execute(cmd);

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
        populate("sites-simple1");
        setUser(3); // Lisa from Solidarites

        // execute
        UserResult result = (UserResult)execute(new GetUsers(1));

        // VERIFY that we have 1 result:
        // - the one other solidarites user

        Assert.assertEquals("number of results", 1, result.getTotalLength());
        Assert.assertEquals("user name", "Marlene", result.getData().get(0).getName());
    }

    @Test
    public void testManageAllUsersPermission() throws CommandException {

        populate("sites-simple1");
        setUser(2); // Bavon from NRC(with manageAllUsers) permission

        // execute
        UserResult result = (UserResult)execute(new GetUsers(1));

        // VERIFY that we can get can see the two other users from NRC
        Assert.assertEquals("number of results", 2, result.getTotalLength());

    }

}
