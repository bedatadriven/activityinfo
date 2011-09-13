package org.sigmah.server.endpoint.gwtrpc.handler;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletContext;

import org.sigmah.client.i18n.I18N;
import org.sigmah.server.auth.SecureTokenGenerator;
import org.sigmah.shared.command.ExportUsers;
import org.sigmah.shared.command.GetUsers;
import org.sigmah.shared.command.handler.CommandHandler;
import org.sigmah.shared.command.result.CommandResult;
import org.sigmah.shared.command.result.RenderResult;
import org.sigmah.shared.command.result.UserResult;
import org.sigmah.shared.domain.User;
import org.sigmah.shared.dto.UserPermissionDTO;
import org.sigmah.shared.exception.CommandException;
import org.sigmah.shared.exception.UnexpectedCommandException;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.google.inject.Inject;

public class ExportUsersHandler implements CommandHandler<ExportUsers>{

	private GetUsersHandler usersHandler;
	private ServletContext context;

	@Inject
	public ExportUsersHandler(ServletContext context, GetUsersHandler usersHandler) {
		this.usersHandler = usersHandler;
		this.context = context;
	}
	
	@Override
	public CommandResult execute(ExportUsers cmd, User user) throws CommandException {
		UserResult users = (UserResult) usersHandler.execute(new GetUsers(cmd.getDatabaseId()), user);
        String filename = SecureTokenGenerator.generate() + ".csv";
        String path = context.getRealPath("/temp") + "/" + filename;
        try {
            File file = new File(path);
            StringBuilder builder = new StringBuilder();
    		generateUserListInCsvFile(users, builder);
    		Files.write(builder, file, Charsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
            throw new UnexpectedCommandException();
        }
		return new RenderResult(filename);
	}

	/** Writes given users to a Csv file using given writer */ 
	private StringBuilder generateUserListInCsvFile(UserResult users, StringBuilder os) throws IOException {
		createCsvHeaderForUser(os);
		for (UserPermissionDTO user : users.getData()) {
			toCsvRow(os, user);
		}
		return os;
	}
	private StringBuilder createCsvHeaderForUser(StringBuilder builder) {
		builder.append(I18N.CONSTANTS.name());
		appendComma(builder);
		builder.append(I18N.CONSTANTS.email());
		appendComma(builder);
		builder.append(I18N.CONSTANTS.partner());
		appendComma(builder);
		builder.append(I18N.CONSTANTS.allowView());
		appendComma(builder);
		builder.append(I18N.CONSTANTS.allowEdit());
		appendComma(builder);
		builder.append(I18N.CONSTANTS.allowViewAll());
		appendComma(builder);
		builder.append(I18N.CONSTANTS.allowEditAll());
		appendComma(builder);
		builder.append(I18N.CONSTANTS.allowManageUsers());
		appendComma(builder);
		builder.append(I18N.CONSTANTS.manageAllUsers());
		appendComma(builder);
		builder.append(I18N.CONSTANTS.allowDesign());
		appendEol(builder);
		return builder;
	}

	private StringBuilder toCsvRow(StringBuilder builder, UserPermissionDTO user) {
		builder.append(user.getName());
		appendComma(builder);
		builder.append(user.getEmail());
		appendComma(builder);
		builder.append(user.getPartner());
		appendComma(builder);
		appendBooleanIfTrue(builder, user.getAllowView());
		appendComma(builder);
		appendBooleanIfTrue(builder, user.getAllowView());
		appendComma(builder);
		appendBooleanIfTrue(builder, user.getAllowEdit());
		appendComma(builder);
		appendBooleanIfTrue(builder, user.getAllowViewAll());
		appendComma(builder);
		appendBooleanIfTrue(builder, user.getAllowEditAll());
		appendComma(builder);
		appendBooleanIfTrue(builder, user.getAllowManageUsers());
		appendComma(builder);
		appendBooleanIfTrue(builder, user.getAllowManageAllUsers());
		appendComma(builder);
		appendBooleanIfTrue(builder, user.getAllowDesign());
		appendEol(builder);
		return builder;
	}
	
	private void appendEol(StringBuilder builder) {
		builder.append("\n");
	}

	private StringBuilder appendComma(StringBuilder builder) {
		return builder.append(",");
	}
	
	private StringBuilder appendBooleanIfTrue(StringBuilder builder, boolean bool) {
		if (bool) {
			builder.append(I18N.CONSTANTS.yes());
		}
		return builder;
	}
	
}
