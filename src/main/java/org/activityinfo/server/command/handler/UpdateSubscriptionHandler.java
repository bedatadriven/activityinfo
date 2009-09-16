package org.activityinfo.server.command.handler;

import org.activityinfo.shared.command.UpdateSubscription;
import org.activityinfo.shared.command.result.CommandResult;
import org.activityinfo.shared.command.result.VoidResult;
import org.activityinfo.shared.exception.CommandException;
import org.activityinfo.shared.exception.IllegalAccessCommandException;
import org.activityinfo.server.domain.User;
import org.activityinfo.server.domain.ReportSubscription;
import org.activityinfo.server.domain.ReportTemplate;

import javax.persistence.EntityManager;

import com.google.inject.Inject;

import java.util.List;
/*
 * @author Alex Bertram
 */

public class UpdateSubscriptionHandler implements CommandHandler<UpdateSubscription> {

    private final EntityManager em;

    @Inject
    public UpdateSubscriptionHandler(EntityManager em) {
        this.em = em;
    }

    public CommandResult execute(UpdateSubscription cmd, User currentUser) throws CommandException {

        int userId = cmd.getUserId() == null ? currentUser.getId() : cmd.getUserId();

        List<ReportSubscription> results = em.createQuery("select sub from ReportSubscription sub where sub.template.id = :templateId and " +
                "sub.user.id = :userId")
                .setParameter("userId", userId)
                .setParameter("templateId", cmd.getReportTemplateId())
                .getResultList();

        if(results.size() == 0) {

            // new subscriptions can be created either by the user themselves
            // or by a second user (invitation)
            
            ReportSubscription sub = new ReportSubscription(
                    em.getReference(ReportTemplate.class, cmd.getReportTemplateId()),
                    em.getReference(User.class, userId));

            sub.setFrequency(cmd.getFrequency());
            sub.setDay(cmd.getDay());

            if(userId != currentUser.getId()) {
                sub.setInvitingUser(currentUser);
            }

            em.persist(sub);
        } else {

            // only the user themselves can change a subscription once it's been
            // created.

            if(userId != currentUser.getId()) {
                throw new IllegalAccessCommandException();
            }

            ReportSubscription sub = results.get(0);
            sub.setFrequency(cmd.getFrequency());
            sub.setDay(cmd.getDay());
        }

        return new VoidResult();
    }
}
