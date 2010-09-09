/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.endpoint.gwtrpc.handler;

import com.extjs.gxt.ui.client.Style;
import com.google.inject.Inject;
import org.dozer.Mapper;
import org.sigmah.shared.command.GetUsers;
import org.sigmah.shared.command.handler.CommandHandler;
import org.sigmah.shared.command.result.CommandResult;
import org.sigmah.shared.command.result.UserResult;
import org.sigmah.shared.domain.User;
import org.sigmah.shared.domain.UserPermission;
import org.sigmah.shared.dto.UserPermissionDTO;
import org.sigmah.shared.exception.CommandException;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Alex Bertram
 * @see org.sigmah.shared.command.GetUsers
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
