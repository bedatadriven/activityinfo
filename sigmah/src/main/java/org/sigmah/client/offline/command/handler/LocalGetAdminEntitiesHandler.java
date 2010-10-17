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

        SqlQueryBuilder query =
            select("AdminEntityId, Name, AdminLevelId, AdminEntityParentId, X1, Y1, X2, Y2 ")
                .from("AdminEntity")
                .where("AdminLevelId").equalTo(cmd.getLevelId())
                .orderBy("Name");

        if(cmd.getParentId() != null) {
            query.where("AdminEntityParentId").equalTo(cmd.getParentId());
        }

        final List<AdminEntityDTO> entities = new ArrayList<AdminEntityDTO>();

        query.forEachResult(connection, new SqlQueryBuilder.ResultHandler() {
            @Override
            public void handle(ResultSet rs) throws SQLException {
                AdminEntityDTO entity = new AdminEntityDTO();
                entity.setId(rs.getInt(1));
                entity.setName(rs.getString(2));
                entity.setLevelId(rs.getInt(3));

                int parentId = rs.getInt(4);
                if(!rs.wasNull()) {
                    entity.setParentId(parentId);
                }

                BoundingBoxDTO bounds = new BoundingBoxDTO();
                bounds.setX1(rs.getDouble(5));
                bounds.setY1(rs.getDouble(6));
                bounds.setX2(rs.getDouble(7));
                bounds.setY2(rs.getDouble(8));
                if(rs.wasNull()) {
                    entity.setBounds(bounds);
                }

                entities.add(entity);
            }
        });

        return new AdminEntityResult(entities);
    }
}
