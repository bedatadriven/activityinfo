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

import com.google.inject.Inject;
import org.activityinfo.server.dao.PartnerDAO;
import org.activityinfo.server.dao.UserDAO;
import org.activityinfo.server.dao.UserDatabaseDAO;
import org.activityinfo.server.dao.UserPermissionDAO;
import org.activityinfo.server.domain.User;
import org.activityinfo.server.domain.UserDatabase;
import org.activityinfo.server.domain.UserPermission;
import org.activityinfo.server.mail.Invitation;
import org.activityinfo.server.mail.Mailer;
import org.activityinfo.shared.command.UpdateUserPermissions;
import org.activityinfo.shared.command.result.CommandResult;
import org.activityinfo.shared.dto.UserPermissionDTO;
import org.activityinfo.shared.exception.CommandException;
import org.activityinfo.shared.exception.IllegalAccessCommandException;

import java.util.Date;

/**
 * @author Alex Bertram
 * @see org.activityinfo.shared.command.UpdateUserPermissions
 */
public class UpdateUserPermissionsHandler implements CommandHandler<UpdateUserPermissions> {

    // TODO: this needs to be pushed down into the domain layer, it doesn't belong here at the endpoint layer

    private final UserDAO userDAO;
    private final UserDatabaseDAO databaseDAO;
    private final PartnerDAO partnerDAO;
    private final UserPermissionDAO permDAO;

    private final Mailer<Invitation> inviteMailer;

    @Inject
    public UpdateUserPermissionsHandler(UserDatabaseDAO databaseDAO, PartnerDAO partnerDAO, UserDAO userDAO,
                                        UserPermissionDAO permDAO, Mailer<Invitation> inviteMailer) {
        this.userDAO = userDAO;
        this.partnerDAO = partnerDAO;
        this.permDAO = permDAO;
        this.inviteMailer = inviteMailer;
        this.databaseDAO = databaseDAO;
    }

    @Override
    public CommandResult execute(UpdateUserPermissions cmd, User executingUser) throws CommandException {

        UserDatabase database = databaseDAO.findById(cmd.getDatabaseId());
        UserPermissionDTO dto = cmd.getModel();

        /*
           * First check that the current user has permission to
           * add users to to the queries
           */
        boolean isOwner = executingUser.getId() == database.getOwner().getId();
        if (!isOwner) {
            verifyAuthority(cmd, database.getPermissionByUser(executingUser));
        }

        User user;
        if (userDAO.doesUserExist(dto.getEmail())) {
            user = userDAO.findUserByEmail(dto.getEmail());
        } else {
            user = createNewUser(executingUser, dto);
        }

        /*
           * Does the permission record exist ?
           */
        UserPermission perm = database.getPermissionByUser(user);
        if (perm == null) {
            perm = new UserPermission(database, user);
            doUpdate(perm, dto, isOwner, database.getPermissionByUser(executingUser));
            permDAO.persist(perm);
        } else {
            doUpdate(perm, dto, isOwner, database.getPermissionByUser(executingUser));
        }

        return null;
    }

    private User createNewUser(User executingUser, UserPermissionDTO dto) {
        User user;
        user = User.createNewUser(dto.getEmail(), dto.getName(), executingUser.getLocale());
        userDAO.persist(user);
        try {
            inviteMailer.send(new Invitation(user, executingUser), executingUser.getLocaleObject());
        } catch (Exception e) {
            // ignore, don't abort because mail didn't work
        }
        return user;
    }


    /**
     * Verifies that the user executing the command has the permission
     * to do assign these permissions.
     * <p/>
     * Static and visible for testing
     *
     * @param cmd
     * @param executingUserPermissions
     * @throws IllegalAccessCommandException
     */
    public static void verifyAuthority(UpdateUserPermissions cmd, UserPermission executingUserPermissions) throws IllegalAccessCommandException {
        if (!executingUserPermissions.isAllowManageUsers()) {
            throw new IllegalAccessCommandException(
                    "Current user does not have the right to manage other users");
        }

        if (!executingUserPermissions.isAllowManageAllUsers() &&
                executingUserPermissions.getPartner().getId() != cmd.getModel().getPartner().getId()) {
            throw new IllegalAccessCommandException(
                    "Current user does not have the right to manage users from other partners");
        }

        if (!executingUserPermissions.isAllowDesign() &&
                cmd.getModel().getAllowDesign()) {
            throw new IllegalAccessCommandException(
                    "Current user does not have the right to grant design privileges");
        }

        if (!executingUserPermissions.isAllowManageAllUsers() &&
                (cmd.getModel().getAllowViewAll() || cmd.getModel().getAllowEditAll() ||
                        cmd.getModel().getAllowManageAllUsers())) {
            throw new IllegalAccessCommandException(
                    "Current user does not have the right to grant viewAll, editAll, or manageAllUsers privileges");
        }
    }

    protected void doUpdate(UserPermission perm, UserPermissionDTO dto, boolean isOwner, UserPermission executingUserPermissions) {

        perm.setPartner(partnerDAO.findById(dto.getPartner().getId()));
        perm.setAllowView(dto.getAllowView());
        perm.setAllowEdit(dto.getAllowEdit());
        perm.setAllowManageUsers(dto.getAllowManageUsers());

        // If currentUser does not have the manageAllUsers permission, then
        // careful not to overwrite permissions that may have been granted by
        // other users with greater permissions

        // The exception is when a user with partner-level user management rights
        // (manageUsers but not manageAllUsers) removes view or edit permissions from
        // an existing user who had been previously granted viewAll or editAll rights
        // by a user with greater permissions.
        //
        // In this case, the only logical outcome (I think) is that

        if (isOwner || executingUserPermissions.isAllowManageAllUsers() || !dto.getAllowView()) {
            perm.setAllowViewAll(dto.getAllowViewAll());
        }

        if (isOwner || executingUserPermissions.isAllowManageAllUsers() || !dto.getAllowEdit()) {
            perm.setAllowEditAll(dto.getAllowEditAll());
        }

        if (isOwner || executingUserPermissions.isAllowManageAllUsers()) {
            perm.setAllowManageAllUsers(dto.getAllowManageAllUsers());
        }

        if (isOwner || executingUserPermissions.isAllowDesign()) {
            perm.setAllowDesign(dto.getAllowDesign());
        }

        perm.setLastSchemaUpdate(new Date());
    }
}
