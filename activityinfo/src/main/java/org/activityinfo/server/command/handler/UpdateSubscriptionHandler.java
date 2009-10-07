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
import org.activityinfo.server.domain.ReportSubscription;
import org.activityinfo.server.domain.ReportTemplate;
import org.activityinfo.server.domain.User;
import org.activityinfo.shared.command.UpdateSubscription;
import org.activityinfo.shared.command.result.CommandResult;
import org.activityinfo.shared.command.result.VoidResult;
import org.activityinfo.shared.exception.CommandException;
import org.activityinfo.shared.exception.IllegalAccessCommandException;

import javax.persistence.EntityManager;
import java.util.List;

/**
 * @see org.activityinfo.shared.command.UpdateSubscription
 *
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
