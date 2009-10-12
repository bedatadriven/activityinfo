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

import com.extjs.gxt.ui.client.Style;
import com.google.inject.Inject;
import org.activityinfo.server.domain.*;
import org.activityinfo.shared.command.GetInvitationList;
import org.activityinfo.shared.command.result.CommandResult;
import org.activityinfo.shared.command.result.InvitationList;
import org.activityinfo.shared.dto.InvitationRow;
import org.activityinfo.shared.exception.CommandException;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.*;
import org.hibernate.ejb.HibernateEntityManager;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @see org.activityinfo.shared.command.GetInvitationList
 *
 * @author Alex Bertram
 */
public class GetInvitationListHandler implements CommandHandler<GetInvitationList> {

    private final EntityManager em;

    @Inject
    public GetInvitationListHandler(EntityManager em) {
        this.em = em;
    }

    public CommandResult execute(GetInvitationList cmd, User currentUser) throws CommandException {

        ReportTemplate template = em.find(ReportTemplate.class, cmd.getReportTemplateId());

        // index subscriptions
        Map<Integer, ReportSubscription> subs = new HashMap<Integer, ReportSubscription>();
        for(ReportSubscription sub : template.getSubscriptions()) {
            subs.put(sub.getUser().getId(), sub);
        }

        Session session = ((HibernateEntityManager)em).getSession();
        Criteria criteria = session.createCriteria(User.class);

        if(template.getDatabase() != null) {

            criteria.add( Restrictions.or(
                 Subqueries.propertyIn("user",
                    DetachedCriteria.forClass(UserDatabase.class)
                        .add( Restrictions.eq("id", template.getDatabase().getId()))
                        .setProjection( Projections.property("owner")) ),
                 Subqueries.propertyIn("user",
                    DetachedCriteria.forClass(UserPermission.class)
                        .add( Restrictions.eq("database", template.getDatabase()))
                        .add( Restrictions.eq("viewAll", true))
                        .setProjection( Projections.property("user")) ) ) );
        }

        Style.SortDir sortDir = cmd.getSortInfo().getSortDir();
        if(sortDir != Style.SortDir.NONE) {
            String sortField = cmd.getSortInfo().getSortField();
            if("userEmail".equals(sortField)) {
                criteria.addOrder( sortDir == Style.SortDir.ASC ? Order.asc("email") : Order.desc("email") );
            } else if("userName".equals(sortField)) {
                criteria.addOrder( sortDir == Style.SortDir.ASC ? Order.asc("name") : Order.desc("name") );
            }
        }

        List<User> users = criteria.list();

        List<InvitationRow> dtos = new ArrayList<InvitationRow>(users.size());
        for(User user : users) {
            InvitationRow dto = new InvitationRow();
            dto.setUserId(user.getId());
            dto.setUserName(user.getName());
            dto.setUserEmail(user.getEmail());

            ReportSubscription sub = subs.get(user.getId());
            dto.setSubscribed(sub!=null && sub.isSubscribed());

            dtos.add(dto);
        }

        if(cmd.getOffset() > 0 || cmd.getLimit() > 0) {
            dtos = dtos.subList(cmd.getOffset(), cmd.getOffset() + cmd.getLimit());    
        }

        return new InvitationList(dtos);
    }
}
