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
 * Copyright 2009 Alex Bertram and contributors.
 */

package org.activityinfo.server.command.handler;

import org.activityinfo.server.command.CommandTestCase;
import org.activityinfo.server.domain.User;
import org.activityinfo.server.domain.Partner;
import org.activityinfo.shared.command.UpdateUserPermissions;
import org.activityinfo.shared.command.GetUsers;
import org.activityinfo.shared.command.result.UserResult;
import org.activityinfo.shared.dto.UserModel;
import org.activityinfo.shared.dto.PartnerModel;
import org.activityinfo.shared.exception.CommandException;
import org.junit.Test;
import org.junit.Assert;

import java.util.List;

/**
 * @author Alex Bertram
 */
public class UpdateUserPermissionsHandlerTest extends CommandTestCase {

    /**
     * Verifies that a user with the manageUsers permission can
     * add another user to the UserDatabase
     *
     * @throws CommandException
     */
    @Test
    public void testAuthorizedCreate() throws CommandException {

        populate("schema1");

        setUser(2);

        UserModel user = new UserModel();
        user.setEmail("ralph@lauren.com");
        user.setName("Ralph");
        user.setPartner(new PartnerModel(1, "NRC"));
        user.setAllowView(true);
        user.setAllowEdit(true);

        UpdateUserPermissions cmd = new UpdateUserPermissions(1, user);
        execute(cmd);

        UserResult result = execute(new GetUsers(1));
        Assert.assertEquals(1, result.getTotalLength());
        Assert.assertEquals("ralph@lauren.com", result.getData().get(0).getEmail());
        Assert.assertTrue("edit permissions", result.getData().get(0).getAllowEdit());
    }

    /**
     * Verifies that the owner of a database can update an existing users
     * permission
     *
     * @throws CommandException
     */
    @Test
    public void testOwnerUpdate() throws CommandException {
        populate("schema1");
        setUser(1);

        UserModel user = new UserModel();
        user.setEmail("bavon@nrcdrc.org");
        user.setPartner(new PartnerModel(1, "NRC"));
        user.setAllowView(true);
        user.setAllowViewAll(false);
        user.setAllowEdit(true);
        user.setAllowEdit(false);
        user.setAllowDesign(true);                                                      

        execute(new UpdateUserPermissions(1, user));

        UserResult result = execute(new GetUsers(1));
        UserModel reUser = result.getData().get(0);
        Assert.assertEquals("bavon@nrcdrc.org", reUser.getEmail());
        Assert.assertTrue("design rights", user.getAllowDesign());

    }


}
