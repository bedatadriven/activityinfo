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
import org.activityinfo.server.mail.Mailer;
import org.activityinfo.server.service.PasswordGenerator;
import org.activityinfo.shared.command.UpdateUserPermissions;
import org.activityinfo.shared.command.result.CommandResult;
import org.activityinfo.shared.dto.UserModel;
import org.activityinfo.shared.exception.CommandException;
import org.apache.commons.mail.SimpleEmail;

import java.text.MessageFormat;
import java.util.ResourceBundle;
import java.util.Date;

/**
 * @see org.activityinfo.shared.command.UpdateUserPermissions
 *
 * @author Alex Bertram
 */
public class UpdateUserPermissionsHandler implements CommandHandler<UpdateUserPermissions> {

	private final AuthDAO authDAO;
    private final SchemaDAO schemaDAO;
    private final Mailer mailer;
    private final PasswordGenerator passwordGenerator;

    @Inject
    public UpdateUserPermissionsHandler(AuthDAO authDAO, SchemaDAO schemaDAO, Mailer mailer, PasswordGenerator passwordGenerator) {
        this.authDAO = authDAO;
        this.schemaDAO = schemaDAO;
        this.mailer = mailer;
        this.passwordGenerator = passwordGenerator;
    }

    @Override
	public CommandResult execute(UpdateUserPermissions cmd, User currentUser) throws CommandException {

		/* 
		 * First check that the current user has permission to 
		 * add users to to the queries
		 */
		
		UserDatabase database = schemaDAO.findById(UserDatabase.class, cmd.getDatabaseId());
		if(!database.isAllowedDesign(currentUser)) {
			throw new CommandException();
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

		    String password = passwordGenerator.generate();

		    user = authDAO.createUser(model.getEmail(), model.getName(), currentUser.getLocale(), currentUser);



        }
		
		/* 
		 * Does the permission record exist ?
		 */
		
		UserPermission perm = database.getPermissionByUser(user);
		
		if(perm == null ) {
			perm = new UserPermission(database, user);
			
			updatePerm(perm, model);
			 
			schemaDAO.save(perm);

		} else {
			
			updatePerm(perm, model);
		}
		
		return null;
	}

	protected void updatePerm(UserPermission perm, UserModel model) {
		perm.setPartner( schemaDAO.findById(Partner.class, model.getPartner().getId()) );
		perm.setAllowView( model.getAllowView() );
		perm.setAllowViewAll( model.getAllowViewAll() );
		perm.setAllowEdit( model.getAllowEdit() );
		perm.setAllowEditAll( model.getAllowEditAll() );
		perm.setAllowDesign( model.getAllowDesign() );
        perm.setLastSchemaUpdate(new Date());
	}
}
