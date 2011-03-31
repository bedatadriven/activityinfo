/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.offline.command.handler;

import com.google.inject.Inject;
import org.sigmah.shared.command.GetAdminEntities;
import org.sigmah.shared.command.handler.CommandHandler;
import org.sigmah.shared.command.result.AdminEntityResult;
import org.sigmah.shared.command.result.CommandResult;
import org.sigmah.shared.dao.SqlAdminDAO;
import org.sigmah.shared.dao.SqlQueryBuilder;
import org.sigmah.shared.domain.User;
import org.sigmah.shared.dto.AdminEntityDTO;
import org.sigmah.shared.dto.BoundingBoxDTO;
import org.sigmah.shared.exception.CommandException;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.sigmah.shared.dao.SqlQueryBuilder.select;

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
