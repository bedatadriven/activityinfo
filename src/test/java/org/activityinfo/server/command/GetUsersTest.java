package org.activityinfo.server.command;

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

import org.activityinfo.server.database.OnDataSet;
import org.activityinfo.shared.command.GetUsers;
import org.activityinfo.shared.command.result.UserResult;
import org.activityinfo.shared.exception.CommandException;
import org.activityinfo.test.InjectionSupport;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(InjectionSupport.class)
@OnDataSet("/dbunit/sites-simple1.db.xml")
public class GetUsersTest extends CommandTestCase {

    private final static int DATABASE_OWNER = 1;

    @Test
    public void testUnsorted() throws CommandException {
        setUser(DATABASE_OWNER);

        GetUsers cmd = new GetUsers(1);
        UserResult result = execute(cmd);

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
        UserResult result = execute(new GetUsers(1));

        // VERIFY that we have 1 result:
        // - the one other solidarites user

        Assert.assertEquals("number of results", 1, result.getTotalLength());
        Assert.assertEquals("user name", "Marlene", result.getData().get(0)
            .getName());
    }

    @Test
    public void testManageAllUsersPermission() throws CommandException {

        setUser(2); // Bavon from NRC(with manageAllUsers) permission

        // execute
        UserResult result = execute(new GetUsers(1));

        // VERIFY that we can get can see the two other users from NRC
        Assert.assertEquals("number of results", 2, result.getTotalLength());
    }
}
