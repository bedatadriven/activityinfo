/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.server.command.handler;

import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.activityinfo.server.database.hibernate.dao.PartnerDAO;
import org.activityinfo.server.database.hibernate.dao.UserDAO;
import org.activityinfo.server.database.hibernate.dao.UserDAOImpl;
import org.activityinfo.server.database.hibernate.dao.UserDatabaseDAO;
import org.activityinfo.server.database.hibernate.dao.UserPermissionDAO;
import org.activityinfo.server.database.hibernate.entity.User;
import org.activityinfo.server.database.hibernate.entity.UserDatabase;
import org.activityinfo.server.database.hibernate.entity.UserPermission;
import org.activityinfo.server.mail.InvitationMessage;
import org.activityinfo.server.mail.MailSender;
import org.activityinfo.shared.command.UpdateUserPermissions;
import org.activityinfo.shared.command.result.CommandResult;
import org.activityinfo.shared.dto.UserPermissionDTO;
import org.activityinfo.shared.exception.CommandException;
import org.activityinfo.shared.exception.IllegalAccessCommandException;

import com.google.inject.Inject;

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

    private final MailSender mailSender;
    
    private static final Logger logger = Logger.getLogger(UpdateUserPermissionsHandler.class.getName());

    @Inject
    public UpdateUserPermissionsHandler(UserDatabaseDAO databaseDAO, PartnerDAO partnerDAO, UserDAO userDAO,
                                        UserPermissionDAO permDAO, MailSender mailSender) {
        this.userDAO = userDAO;
        this.partnerDAO = partnerDAO;
        this.permDAO = permDAO;
        this.mailSender = mailSender;
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

    private User createNewUser(User executingUser, UserPermissionDTO dto) throws CommandException {
        User user = UserDAOImpl.createNewUser(dto.getEmail(), dto.getName(), executingUser.getLocale());
        userDAO.persist(user);
        
        if(executingUser.getId() == 0) {
        	throw new AssertionError("executingUser.id == 0!");
        }
        if(executingUser.getName() == null) {
        	throw new AssertionError("executingUser.name == null!");
        }
        
        try {
        	mailSender.send(new InvitationMessage(user, executingUser));
        } catch (Exception e) {
        	logger.log(Level.SEVERE, "Could not send invitation mail", e);
        	throw new CommandException("Failed to send invitation email");
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
