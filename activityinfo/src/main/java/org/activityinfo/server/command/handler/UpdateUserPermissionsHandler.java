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

import com.google.inject.Inject;
import org.activityinfo.server.dao.AuthDAO;
import org.activityinfo.server.dao.SchemaDAO;
import org.activityinfo.server.domain.Partner;
import org.activityinfo.server.domain.User;
import org.activityinfo.server.domain.UserDatabase;
import org.activityinfo.server.domain.UserPermission;
import org.activityinfo.shared.command.UpdateUserPermissions;
import org.activityinfo.shared.command.result.CommandResult;
import org.activityinfo.shared.dto.UserModel;
import org.activityinfo.shared.exception.CommandException;
import org.activityinfo.shared.exception.IllegalAccessCommandException;

import java.util.Date;

/**
 * @see org.activityinfo.shared.command.UpdateUserPermissions
 *
 * @author Alex Bertram
 */
public class UpdateUserPermissionsHandler implements CommandHandler<UpdateUserPermissions> {

	private final AuthDAO authDAO;
    private final SchemaDAO schemaDAO;

    @Inject
    public UpdateUserPermissionsHandler(AuthDAO authDAO, SchemaDAO schemaDAO) {
        this.authDAO = authDAO;
        this.schemaDAO = schemaDAO;
    }

    @Override
	public CommandResult execute(UpdateUserPermissions cmd, User currentUser) throws CommandException {

		/* 
		 * First check that the current user has permission to 
		 * add users to to the queries
		 */
		
		UserDatabase database = schemaDAO.findById(UserDatabase.class, cmd.getDatabaseId());
        Partner partner = schemaDAO.findById(Partner.class, cmd.getModel().getPartner().getId());

        UserPermission currentUserPermission;
        if(database.getOwner().getId() != currentUser.getId()) {
            currentUserPermission = database.getPermissionByUser(currentUser);

            // confirm that the user has the right to manage other users
            if(!currentUserPermission.isAllowManageUsers())
                throw new IllegalAccessCommandException("Current user does not have the right to manage other users");
            // confirm that the user has the right to manage this user
            if(!currentUserPermission.isAllowManageAllUsers() &&
                       currentUserPermission.getPartner().getId() != partner.getId())
                throw new IllegalAccessCommandException("Current user does not have the right to manage users from other partners");

            // confirm that the user is not try to assign rights above their paygrade
            if(!currentUserPermission.isAllowDesign() &&
                    cmd.getModel().getAllowDesign())
                throw new IllegalAccessCommandException("Current user does not have the right to grant design priviledges");

            if(!currentUserPermission.isAllowManageAllUsers() &&
                 (cmd.getModel().getAllowViewAll() || cmd.getModel().getAllowEditAll() ||
                  cmd.getModel().getAllowManageAllUsers()))
                throw new IllegalAccessCommandException("Current user does not have the right to grant viewAll, editAll, or manageAllUsers priviledges");
        } else {
            // the owner's rights are implict, define relevant rights here for convienance
            currentUserPermission = new UserPermission();
            currentUserPermission.setAllowManageUsers(true);
            currentUserPermission.setAllowManageAllUsers(true);
            currentUserPermission.setAllowDesign(true);
        }

		/*
		 * Does the user (with this email) already exist ?
		 * 
		 * If not, create a new login record
		 * 
		 */
        UserModel model = cmd.getModel();
		User user = authDAO.getUserByEmail(model.getEmail());
		if(user == null) {
		    user = authDAO.createUser(model.getEmail(), model.getName(), currentUser.getLocale(), currentUser);
        }
		
		/* 
		 * Does the permission record exist ?
		 */
		UserPermission perm = database.getPermissionByUser(user);
		if(perm == null ) {
			perm = new UserPermission(database, user);
			updatePerm(currentUserPermission, perm, model);
			schemaDAO.save(perm);
		} else {
			updatePerm(currentUserPermission, perm, model);
		}
		
		return null;
	}

	protected void updatePerm(UserPermission currentUserPermission, UserPermission perm, UserModel model) {

		perm.setPartner( schemaDAO.findById(Partner.class, model.getPartner().getId()) );
		perm.setAllowView( model.getAllowView() );
		perm.setAllowEdit( model.getAllowEdit() );
        perm.setAllowManageUsers( model.getAllowManageUsers() );

        // If currentUser does not have the manageAllUsers permission, then
        // careful not to overwrite permissions that may have been granted by
        // other users with greater permissions

        // The exception is when a user with partner-level user management rights
        // (manageUsers but not manageAllUsers) removes view or edit permissions from
        // an existing user who had been previously granted viewAll or editAll rights
        // by a user with greater permissions.
        //
        // In this case, the only logical outcome (I think) is that

        if( currentUserPermission.isAllowManageAllUsers() || !model.getAllowView())
            perm.setAllowViewAll( model.getAllowViewAll() );

        if( currentUserPermission.isAllowManageAllUsers() || !model.getAllowEdit())
            perm.setAllowEditAll( model.getAllowEditAll() );

        if( currentUserPermission.isAllowManageAllUsers() )
            perm.setAllowManageAllUsers( model.getAllowManageAllUsers() );

        if( currentUserPermission.isAllowDesign())
            perm.setAllowDesign( model.getAllowDesign() );

        perm.setLastSchemaUpdate(new Date());
	}
}
