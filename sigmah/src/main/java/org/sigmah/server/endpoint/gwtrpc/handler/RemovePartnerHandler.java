/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.endpoint.gwtrpc.handler;

import com.google.inject.Inject;
import org.sigmah.server.domain.OrgUnit;
import org.sigmah.server.domain.User;
import org.sigmah.server.domain.UserDatabase;
import org.sigmah.server.domain.UserPermission;
import org.sigmah.shared.command.RemovePartner;
import org.sigmah.shared.command.result.CommandResult;
import org.sigmah.shared.command.result.VoidResult;
import org.sigmah.shared.exception.CommandException;
import org.sigmah.shared.exception.IllegalAccessCommandException;
import org.sigmah.shared.exception.PartnerHasSitesException;

import javax.persistence.EntityManager;

/**
 * @author Alex Bertram
 * @see org.sigmah.shared.command.RemovePartner
 */
public class RemovePartnerHandler implements CommandHandler<RemovePartner> {

    private EntityManager em;

    @Inject
    public RemovePartnerHandler(EntityManager em) {
        this.em = em;
    }

    public CommandResult execute(RemovePartner cmd, User user) throws CommandException {

        // verify the current user has access to this site
        UserDatabase db = em.find(UserDatabase.class, cmd.getDatabaseId());
        if (db.getOwner().getId() != user.getId()) {
            UserPermission perm = db.getPermissionByUser(user);
            if (perm == null || perm.isAllowDesign()) {
                throw new IllegalAccessCommandException();
            }
        }

        // check to see if there are already sites associated with this
        // partner

        int siteCount = ((Number) em.createQuery("select count(s) from Site s where " +
                "s.activity.id in (select a.id from Activity a where a.database.id = :dbId) and " +
                "s.partner.id = :partnerId and " +
                "s.dateDeleted is null")
                .setParameter("dbId", cmd.getDatabaseId())
                .setParameter("partnerId", cmd.getPartnerId())
                .getSingleResult()).intValue();

        if (siteCount > 0) {
            throw new PartnerHasSitesException();
        }

        db.getPartners().remove(em.getReference(OrgUnit.class, cmd.getPartnerId()));


        return new VoidResult();
    }
}
