/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.endpoint.gwtrpc.handler;

import com.google.inject.Inject;
import org.sigmah.server.domain.ReportDefinition;
import org.sigmah.server.domain.ReportSubscription;
import org.sigmah.server.domain.User;
import org.sigmah.shared.command.UpdateSubscription;
import org.sigmah.shared.command.result.CommandResult;
import org.sigmah.shared.command.result.VoidResult;
import org.sigmah.shared.exception.CommandException;
import org.sigmah.shared.exception.IllegalAccessCommandException;

import javax.persistence.EntityManager;
import java.util.List;

/**
 * @author Alex Bertram
 * @see org.sigmah.shared.command.UpdateSubscription
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

        if (results.size() == 0) {

            // new subscriptions can be created either by the user themselves
            // or by a second user (invitation)

            if (cmd.isSubscribed()) {

                ReportSubscription sub = new ReportSubscription(
                        em.getReference(ReportDefinition.class, cmd.getReportTemplateId()),
                        em.getReference(User.class, userId));

                sub.setSubscribed(true);

                if (userId != currentUser.getId()) {
                    sub.setInvitingUser(currentUser);
                }

                em.persist(sub);
            }
        } else {

            // only the user themselves can change a subscription once it's been
            // created.

            if (userId != currentUser.getId()) {
                throw new IllegalAccessCommandException();
            }

            ReportSubscription sub = results.get(0);
            sub.setSubscribed(cmd.isSubscribed());
        }

        return new VoidResult();
    }
}
