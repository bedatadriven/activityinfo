package org.activityinfo.server.command.handler;

import org.activityinfo.shared.command.result.CommandResult;
import org.activityinfo.shared.command.result.InvitationList;
import org.activityinfo.shared.command.GetInvitationList;
import org.activityinfo.shared.exception.CommandException;
import org.activityinfo.shared.dto.InvitationDTO;
import org.activityinfo.server.domain.*;
import org.hibernate.ejb.HibernateEntityManager;
import org.hibernate.Session;
import org.hibernate.Criteria;
import org.hibernate.criterion.*;

import javax.persistence.EntityManager;

import com.google.inject.Inject;
import com.extjs.gxt.ui.client.Style;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
/*
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

        List<InvitationDTO> dtos = new ArrayList<InvitationDTO>(users.size());
        for(User user : users) {
            InvitationDTO dto = new InvitationDTO();
            dto.setUserId(user.getId());
            dto.setUserName(user.getName());
            dto.setUserEmail(user.getEmail());

            ReportSubscription sub = subs.get(user.getId());
            if(sub != null) {
                dto.setSubscriptionFrequency(sub.getFrequency());
                dto.setSubscriptionDay(sub.getDay());
            }

            dtos.add(dto);
        }

        if(cmd.getOffset() > 0 || cmd.getLimit() > 0) {
            dtos = dtos.subList(cmd.getOffset(), cmd.getOffset() + cmd.getLimit());    
        }

        return new InvitationList(dtos);
    }
}
