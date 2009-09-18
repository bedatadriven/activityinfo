package org.activityinfo.server.command.handler;

import org.activityinfo.server.dao.AuthDAO;
import org.activityinfo.server.dao.SchemaDAO;
import org.activityinfo.server.domain.Partner;
import org.activityinfo.server.domain.User;
import org.activityinfo.server.domain.UserDatabase;
import org.activityinfo.server.domain.UserPermission;
import org.activityinfo.server.mail.Mailer;
import org.activityinfo.server.service.PasswordGenerator;
import org.activityinfo.shared.command.UpdateUser;
import org.activityinfo.shared.command.result.CommandResult;
import org.activityinfo.shared.dto.UserModel;
import org.activityinfo.shared.exception.CommandException;
import org.apache.commons.mail.SimpleEmail;
import org.apache.commons.mail.EmailException;

import com.google.inject.Inject;

import java.util.ResourceBundle;
import java.text.MessageFormat;

public class UpdateUserHandler implements CommandHandler<UpdateUser> {

	private final AuthDAO authDAO;
    private final SchemaDAO schemaDAO;
    private final Mailer mailer;
    private final PasswordGenerator passwordGenerator;

    @Inject
    public UpdateUserHandler(AuthDAO authDAO, SchemaDAO schemaDAO, Mailer mailer, PasswordGenerator passwordGenerator) {
        this.authDAO = authDAO;
        this.schemaDAO = schemaDAO;
        this.mailer = mailer;
        this.passwordGenerator = passwordGenerator;
    }

    @Override
	public CommandResult execute(UpdateUser cmd, User currentUser) throws CommandException {

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

		    user = authDAO.createUser(model.getEmail(), model.getName(), password, currentUser.getLocale());

            try {
                ResourceBundle mailMessages =
                      ResourceBundle.getBundle("org.activityinfo.server.mail.MailMessages", user.getLocaleObject());

                SimpleEmail email = new SimpleEmail();
                email.addTo(user.getEmail(), user.getName());
                email.setSubject(mailMessages.getString("passwordSubject"));

                StringBuilder sb = new StringBuilder();
                sb.append(MessageFormat.format(mailMessages.getString("greeting"), user.getName())).append("\n\n");
                sb.append(MessageFormat.format(mailMessages.getString("newUserIntro"), currentUser.getName(), currentUser.getEmail())).append("\n\n");
                sb.append(MessageFormat.format(mailMessages.getString("newUserPassword"), user.getName(), password)).append("\n\n");
                sb.append(mailMessages.getString("newUserSignoff"));
                email.setMsg(sb.toString());

                mailer.send(email);
            } catch (Exception e) {
                // don't let an email failure rollback the creation of the user.
                e.printStackTrace();
            }

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

	}
}
