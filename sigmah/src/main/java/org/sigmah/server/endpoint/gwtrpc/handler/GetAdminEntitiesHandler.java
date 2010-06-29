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

package org.sigmah.server.endpoint.gwtrpc.handler;

import com.google.inject.Inject;
import org.dozer.Mapper;
import org.sigmah.server.dao.AdminDAO;
import org.sigmah.server.domain.AdminEntity;
import org.sigmah.server.domain.User;
import org.sigmah.shared.command.GetAdminEntities;
import org.sigmah.shared.command.result.AdminEntityResult;
import org.sigmah.shared.command.result.CommandResult;
import org.sigmah.shared.dto.AdminEntityDTO;
import org.sigmah.shared.exception.CommandException;

import java.util.ArrayList;
import java.util.List;

public class GetAdminEntitiesHandler implements CommandHandler<GetAdminEntities> {

    protected AdminDAO adminDAO;
    protected Mapper mapper;

    @Inject
    public GetAdminEntitiesHandler(AdminDAO adminDAO, Mapper mapper) {
        this.adminDAO = adminDAO;
        this.mapper = mapper;
    }

    @Override
    public CommandResult execute(GetAdminEntities cmd, User user) throws CommandException {

        AdminDAO.Query query = adminDAO.query().level(cmd.getLevelId());

        if (cmd.getParentId() != null) {
            query.withParentEntityId(cmd.getParentId());
        }
        if (cmd.getActivityId() != null) {
            query.withSitesOfActivityId(cmd.getActivityId());
        }

        List<AdminEntity> entities = query.execute();

        List<AdminEntityDTO> models = new ArrayList<AdminEntityDTO>();

        for (AdminEntity entity : entities) {
            models.add(mapper.map(entity, AdminEntityDTO.class));
        }

        return new AdminEntityResult(models);
    }
}
