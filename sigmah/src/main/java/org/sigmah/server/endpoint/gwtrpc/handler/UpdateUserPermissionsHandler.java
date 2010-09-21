/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.endpoint.gwtrpc.handler;

import com.google.inject.Inject;
import org.sigmah.server.dao.PartnerDAO;
import org.sigmah.server.dao.hibernate.UserDAOImpl;
import org.sigmah.server.mail.Invitation;
import org.sigmah.server.mail.Mailer;
import org.sigmah.server.util.LocaleHelper;
import org.sigmah.shared.command.UpdateUserPermissions;
import org.sigmah.shared.command.handler.CommandHandler;
import org.sigmah.shared.command.result.CommandResult;
import org.sigmah.shared.dao.UserDAO;
import org.sigmah.shared.dao.UserDatabaseDAO;
import org.sigmah.shared.dao.UserPermissionDAO;
import org.sigmah.shared.domain.User;
import org.sigmah.shared.domain.UserDatabase;
import org.sigmah.shared.domain.UserPermission;
import org.sigmah.shared.dto.UserPermissionDTO;
import org.sigmah.shared.exception.CommandException;
import org.sigmah.shared.exception.IllegalAccessCommandException;

import java.util.Date;

/**
 * @author Alex Bertram
 * @see org.sigmah.shared.command.UpdateUserPermissions
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

        User user = null;
        if (userDAO.doesUserExist(dto.getEmail())) {
            user = userDAO.findUserByEmail(dto.getEmail());
        }
        
        if (user == null) {
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
        User user = UserDAOImpl.createNewUser(dto.getEmail(), dto.getName(), executingUser.getLocale());
        userDAO.persist(user);
        try {
            inviteMailer.send(new Invitation(user, executingUser), LocaleHelper.getLocaleObject(executingUser));
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
