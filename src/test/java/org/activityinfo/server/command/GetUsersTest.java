package org.activityinfo.server.command;

import org.activityinfo.shared.command.GetUsers;
import org.activityinfo.shared.command.result.UserResult;
import org.activityinfo.shared.exception.CommandException;
import org.junit.Test;
import org.junit.Assert;

public class GetUsersTest extends CommandTestCase {




    @Test
    public void testUnsorted() throws CommandException {

        populate("sites-simple1");

        setUser(1);

        GetUsers cmd = new GetUsers(1);
        UserResult result = (UserResult)execute(cmd);

        Assert.assertEquals(3, result.getData().size());

    }


}
