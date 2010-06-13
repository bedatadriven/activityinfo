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

package org.activityinfo.server.endpoint.gwtrpc.handler;

import com.extjs.gxt.ui.client.Style;
import com.google.inject.Inject;
import org.activityinfo.server.domain.User;
import org.activityinfo.server.domain.UserPermission;
import org.activityinfo.shared.command.GetUsers;
import org.activityinfo.shared.command.result.CommandResult;
import org.activityinfo.shared.command.result.UserResult;
import org.activityinfo.shared.dto.UserPermissionDTO;
import org.activityinfo.shared.exception.CommandException;
import org.dozer.Mapper;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Alex Bertram
 * @see org.activityinfo.shared.command.GetUsers
 */
public class GetUsersHandler implements CommandHandler<GetUsers> {

    protected EntityManager em;
    protected Mapper mapper;

    @Inject
    public GetUsersHandler(EntityManager em, Mapper mapper) {
        this.em = em;
        this.mapper = mapper;
    }

    @Override
    public CommandResult execute(GetUsers cmd, User currentUser) throws CommandException {

        String orderByClause = "";

        if (cmd.getSortInfo().getSortDir() != Style.SortDir.NONE) {
            String dir = cmd.getSortInfo().getSortDir() == Style.SortDir.ASC ? "asc" : "desc";
            String property = null;
            String field = cmd.getSortInfo().getSortField();

            if ("name".equals(field)) {
                property = "up.user.name";
            } else if ("email".equals(field)) {
                property = "up.user.email";
            } else if ("partner".equals(field)) {
                property = "up.partner.name";
            } else if (field != null && field.startsWith("allow")) {
                property = "up." + field;
            }

            if (property != null) {
                orderByClause = " order by " + property + " " + dir;
            }
        }

        Query query = em.createQuery("select up from UserPermission up where " +
                "up.database.id = :dbId and " +
                "up.user.id <> :currentUserId " + orderByClause)
                .setParameter("dbId", cmd.getDatabaseId())
                .setParameter("currentUserId", currentUser.getId());

        if (cmd.getOffset() > 0) {
            query.setFirstResult(cmd.getOffset());
        }
        if (cmd.getLimit() > 0) {
            query.setMaxResults(cmd.getLimit());
        }

        List<UserPermission> perms = query.getResultList();
        List<UserPermissionDTO> models = new ArrayList<UserPermissionDTO>();

        for (UserPermission perm : perms) {
            models.add(mapper.map(perm, UserPermissionDTO.class));
        }

        int totalCount = ((Number) em.createQuery("select count(up) from UserPermission up where " +
                "up.database.id = :dbId and " +
                "up.user.id <> :currentUserId ")
                .setParameter("dbId", cmd.getDatabaseId())
                .setParameter("currentUserId", currentUser.getId())
                .getSingleResult())
                .intValue();

        return new UserResult(models, cmd.getOffset(), totalCount);
    }
}
