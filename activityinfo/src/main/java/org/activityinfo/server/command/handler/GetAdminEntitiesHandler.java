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
import org.activityinfo.server.dao.AdminDAO;
import org.activityinfo.server.domain.AdminEntity;
import org.activityinfo.server.domain.User;
import org.activityinfo.shared.command.GetAdminEntities;
import org.activityinfo.shared.command.result.AdminEntityResult;
import org.activityinfo.shared.command.result.CommandResult;
import org.activityinfo.shared.dto.AdminEntityModel;
import org.activityinfo.shared.exception.CommandException;
import org.dozer.Mapper;

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

        if (cmd.getParentId() != null)
            query.withParentEntityId(cmd.getParentId());
        if (cmd.getActivityId() != null)
            query.withSitesOfActivityId(cmd.getActivityId());

        List<AdminEntity> entities = query.execute();

        List<AdminEntityModel> models = new ArrayList<AdminEntityModel>();

        for (AdminEntity entity : entities) {
            models.add(mapper.map(entity, AdminEntityModel.class));
        }

        return new AdminEntityResult(models);
    }
}
