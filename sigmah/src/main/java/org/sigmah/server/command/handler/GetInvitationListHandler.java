/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.command.handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;
import org.hibernate.ejb.HibernateEntityManager;
import org.sigmah.server.database.hibernate.entity.ReportDefinition;
import org.sigmah.server.database.hibernate.entity.ReportSubscription;
import org.sigmah.server.database.hibernate.entity.User;
import org.sigmah.server.database.hibernate.entity.UserDatabase;
import org.sigmah.server.database.hibernate.entity.UserPermission;
import org.sigmah.shared.command.GetInvitationList;
import org.sigmah.shared.command.result.CommandResult;
import org.sigmah.shared.command.result.InvitationList;
import org.sigmah.shared.dto.ReportSubscriptionDTO;
import org.sigmah.shared.exception.CommandException;

import com.extjs.gxt.ui.client.Style;
import com.google.inject.Inject;

/**
 * @author Alex Bertram
 * @see org.sigmah.shared.command.GetInvitationList
 */
public class GetInvitationListHandler implements CommandHandler<GetInvitationList> {

    private final EntityManager em;

    @Inject
    public GetInvitationListHandler(EntityManager em) {
        this.em = em;
    }

    public CommandResult execute(GetInvitationList cmd, User currentUser) throws CommandException {

        ReportDefinition template = em.find(ReportDefinition.class, cmd.getReportTemplateId());

        // index subscriptions
        Map<Integer, ReportSubscription> subs = new HashMap<Integer, ReportSubscription>();
        for (ReportSubscription sub : template.getSubscriptions()) {
            subs.put(sub.getUser().getId(), sub);
        }

        Session session = ((HibernateEntityManager) em).getSession();
        Criteria criteria = session.createCriteria(User.class);

        if (template.getDatabase() != null) {

            criteria.add(Restrictions.or(
                    Subqueries.propertyIn("user",
                            DetachedCriteria.forClass(UserDatabase.class)
                                    .add(Restrictions.eq("id", template.getDatabase().getId()))
                                    .setProjection(Projections.property("owner"))),
                    Subqueries.propertyIn("user",
                            DetachedCriteria.forClass(UserPermission.class)
                                    .add(Restrictions.eq("database", template.getDatabase()))
                                    .add(Restrictions.eq("viewAll", true))
                                    .setProjection(Projections.property("user")))));
        }

        Style.SortDir sortDir = cmd.getSortInfo().getSortDir();
        if (sortDir != Style.SortDir.NONE) {
            String sortField = cmd.getSortInfo().getSortField();
            if ("userEmail".equals(sortField)) {
                criteria.addOrder(sortDir == Style.SortDir.ASC ? Order.asc("email") : Order.desc("email"));
            } else if ("userName".equals(sortField)) {
                criteria.addOrder(sortDir == Style.SortDir.ASC ? Order.asc("name") : Order.desc("name"));
            }
        }

        List<User> users = criteria.list();

        List<ReportSubscriptionDTO> dtos = new ArrayList<ReportSubscriptionDTO>(users.size());
        for (User user : users) {
            ReportSubscriptionDTO dto = new ReportSubscriptionDTO();
            dto.setUserId(user.getId());
            dto.setUserName(user.getName());
            dto.setUserEmail(user.getEmail());

            ReportSubscription sub = subs.get(user.getId());
            dto.setSubscribed(sub != null && sub.isSubscribed());

            dtos.add(dto);
        }

        if (cmd.getOffset() > 0 || cmd.getLimit() > 0) {
            dtos = dtos.subList(cmd.getOffset(), cmd.getOffset() + cmd.getLimit());
        }

        return new InvitationList(dtos);
    }
}
