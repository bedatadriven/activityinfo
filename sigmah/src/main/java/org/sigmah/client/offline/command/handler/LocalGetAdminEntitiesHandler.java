/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.offline.command.handler;

import java.sql.Connection;

import org.sigmah.shared.command.GetAdminEntities;
import org.sigmah.shared.command.handler.CommandHandler;
import org.sigmah.shared.command.result.AdminEntityResult;
import org.sigmah.shared.command.result.CommandResult;
import org.sigmah.shared.dao.SqlAdminDAO;
import org.sigmah.shared.domain.User;
import org.sigmah.shared.exception.CommandException;

import com.google.inject.Inject;

public class LocalGetAdminEntitiesHandler implements CommandHandler<GetAdminEntities> {

    private Connection connection;

    @Inject
    public LocalGetAdminEntitiesHandler(Connection connection) {
        this.connection = connection;
    }

    @Override
    public CommandResult execute(GetAdminEntities cmd, User user) throws CommandException {

        return new AdminEntityResult(SqlAdminDAO.query(connection, cmd));
    }
}
