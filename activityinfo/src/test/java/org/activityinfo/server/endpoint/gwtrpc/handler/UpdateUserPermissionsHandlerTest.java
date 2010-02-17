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

package org.activityinfo.server.endpoint.gwtrpc.handler;

import org.activityinfo.MockDb;
import org.activityinfo.server.dao.PartnerDAO;
import org.activityinfo.server.dao.UserDAO;
import org.activityinfo.server.dao.UserDatabaseDAO;
import org.activityinfo.server.dao.UserPermissionDAO;
import org.activityinfo.server.domain.Partner;
import org.activityinfo.server.domain.User;
import org.activityinfo.server.domain.UserDatabase;
import org.activityinfo.server.domain.UserPermission;
import org.activityinfo.server.mail.Invitation;
import org.activityinfo.server.mail.Mailer;
import org.activityinfo.shared.command.UpdateUserPermissions;
import org.activityinfo.shared.dto.PartnerModel;
import org.activityinfo.shared.dto.UserModel;
import org.activityinfo.shared.exception.IllegalAccessCommandException;
import org.junit.Before;
import org.junit.Test;

import java.util.Locale;

import static org.easymock.EasyMock.*;

/**
 * @author Alex Bertram
 */
public class UpdateUserPermissionsHandlerTest {

    private Partner NRC;
    private Partner IRC;
    private PartnerModel NRC_DTO;

    private MockDb db = new MockDb();
    protected Mailer<Invitation> mailer;
    protected UpdateUserPermissionsHandler handler;
    protected User owner;

    @Before
    public void setup() {

        NRC = new Partner();
        NRC.setId(1);
        NRC.setName("NRC");
        NRC.setFullName("Norwegian Refugee Council");
        db.persist(NRC);

        IRC = new Partner();
        IRC.setId(2);
        IRC.setName("IRC");
        IRC.setFullName("International Rescue Committee");
        db.persist(IRC);

        NRC_DTO = new PartnerModel(1, "NRC");

        mailer = createMock("InvitationMailer", Mailer.class);

        handler = new UpdateUserPermissionsHandler(
                db.getDAO(UserDatabaseDAO.class), db.getDAO(PartnerDAO.class), db.getDAO(UserDAO.class),
                db.getDAO(UserPermissionDAO.class), mailer);


        owner = new User();
        owner.setId(99);
        owner.setName("Alex");
        db.persist(owner);

        UserDatabase udb = new UserDatabase(1, "PEAR");
        udb.setOwner(owner);
        db.persist(udb);

    }

    @Test
    public void ownerCanAddUser() throws Exception {

        mailer.send(isA(Invitation.class), isA(Locale.class));
        replay(mailer);

        UserModel user = new UserModel();
        user.setEmail("other@foobar");
        user.setPartner(NRC_DTO);
        user.setAllowView(true);

        UpdateUserPermissions cmd = new UpdateUserPermissions(1, user);

        handler.execute(cmd, owner);

        verify(mailer);
    }


    /**
     * Asserts that someone with ManageUsersPermission will
     * be permitted to grant some one edit rights.
     */
    @Test
    public void testVerifyAuthorityForViewPermissions() throws IllegalAccessCommandException {

        UserPermission executingUserPermissions = new UserPermission();
        executingUserPermissions.setPartner(NRC);
        executingUserPermissions.setAllowManageUsers(true);

        UserModel dto = new UserModel();
        dto.setPartner(NRC_DTO);
        dto.setAllowView(true);

        UpdateUserPermissions cmd = new UpdateUserPermissions(1, dto);

        UpdateUserPermissionsHandler.verifyAuthority(cmd, executingUserPermissions);
    }

    /**
     * Asserts that someone with ManageUsersPermission will
     * be permitted to grant some one edit rights.
     */
    @Test
    public void testVerifyAuthorityForEditPermissions() throws IllegalAccessCommandException {

        UserPermission executingUserPermissions = new UserPermission();
        executingUserPermissions.setPartner(NRC);
        executingUserPermissions.setAllowManageUsers(true);

        UserModel dto = new UserModel();
        dto.setPartner(NRC_DTO);
        dto.setAllowView(true);
        dto.setAllowEdit(true);

        UpdateUserPermissions cmd = new UpdateUserPermissions(1, dto);

        UpdateUserPermissionsHandler.verifyAuthority(cmd, executingUserPermissions);
    }

    @Test(expected = IllegalAccessCommandException.class)
    public void testFailingVerifyAuthorityForView() throws IllegalAccessCommandException {

        UserPermission executingUserPermissions = new UserPermission();
        executingUserPermissions.setPartner(IRC);
        executingUserPermissions.setAllowManageUsers(true);

        UserModel dto = new UserModel();
        dto.setPartner(NRC_DTO);
        dto.setAllowView(true);
        dto.setAllowEdit(true);

        UpdateUserPermissions cmd = new UpdateUserPermissions(1, dto);

        UpdateUserPermissionsHandler.verifyAuthority(cmd, executingUserPermissions);
    }

    @Test
    public void testVerifyAuthorityForViewByOtherPartner() throws IllegalAccessCommandException {

        UserPermission executingUserPermissions = new UserPermission();
        executingUserPermissions.setPartner(IRC);
        executingUserPermissions.setAllowManageUsers(true);
        executingUserPermissions.setAllowManageAllUsers(true);

        UserModel dto = new UserModel();
        dto.setPartner(NRC_DTO);
        dto.setAllowView(true);
        dto.setAllowEdit(true);

        UpdateUserPermissions cmd = new UpdateUserPermissions(1, dto);

        UpdateUserPermissionsHandler.verifyAuthority(cmd, executingUserPermissions);
    }


//
//
//    /**
//     * Verifies that a user with the manageUsers permission can
//     * add another user to the UserDatabase
//     *
//     * @throws CommandException
//     */
//    @Test
//    public void testAuthorizedCreate() throws CommandException {
//
//        populate("schema1");
//
//        setUser(2);
//
//        UserModel user = new UserModel();
//        user.setEmail("ralph@lauren.com");
//        user.setName("Ralph");
//        user.setPartner(new PartnerModel(1, "NRC"));
//        user.setAllowView(true);
//        user.setAllowEdit(true);
//
//        UpdateUserPermissions cmd = new UpdateUserPermissions(1, user);
//        execute(cmd);
//
//        UserResult result = execute(new GetUsers(1));
//        Assert.assertEquals(1, result.getTotalLength());
//        Assert.assertEquals("ralph@lauren.com", result.getData().get(0).getEmail());
//        Assert.assertTrue("edit permissions", result.getData().get(0).getAllowEdit());
//    }
//
//    /**
//     * Verifies that the owner of a database can update an existing users
//     * permission
//     *
//     * @throws CommandException
//     */
//    @Test
//    public void testOwnerUpdate() throws CommandException {
//        populate("schema1");
//        setUser(1);
//
//        UserModel user = new UserModel();
//        user.setEmail("bavon@nrcdrc.org");
//        user.setPartner(new PartnerModel(1, "NRC"));
//        user.setAllowView(true);
//        user.setAllowViewAll(false);
//        user.setAllowEdit(true);
//        user.setAllowEdit(false);
//        user.setAllowDesign(true);
//
//        execute(new UpdateUserPermissions(1, user));
//
//        UserResult result = execute(new GetUsers(1));
//        UserModel reUser = result.getData().get(0);
//        Assert.assertEquals("bavon@nrcdrc.org", reUser.getEmail());
//        Assert.assertTrue("design rights", user.getAllowDesign());
//
//    }


}
