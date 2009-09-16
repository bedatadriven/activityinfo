package org.activityinfo.server.command.handler;

import org.activityinfo.server.domain.Partner;
import org.activityinfo.server.domain.User;
import org.activityinfo.server.domain.UserDatabase;
import org.activityinfo.server.domain.UserPermission;
import org.activityinfo.shared.command.RemovePartner;
import org.activityinfo.shared.command.result.CommandResult;
import org.activityinfo.shared.command.result.VoidResult;
import org.activityinfo.shared.exception.CommandException;
import org.activityinfo.shared.exception.IllegalAccessCommandException;
import org.activityinfo.shared.exception.PartnerHasSitesException;

import javax.persistence.EntityManager;

import com.google.inject.Inject;
/*
 * @author Alex Bertram
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
        if(db.getOwner().getId() != user.getId()) {
            UserPermission perm = db.getPermissionByUser(user);
            if(perm == null || perm.isAllowDesign()) {
                throw new IllegalAccessCommandException();
            }
        }
        
        // check to see if there are already sites associated with this
        // partner

        int siteCount = ((Number)em.createQuery("select count(s) from Site s where " +
                         "s.activity.id in (select a.id from Activity a where a.database.id = :dbId) and " +
                         "s.partner.id = :partnerId and " +
                         "s.dateDeleted is null")
             .setParameter("dbId", cmd.getDatabaseId())
             .setParameter("partnerId", cmd.getPartnerId())
             .getSingleResult()).intValue();

        if(siteCount > 0) {
            throw new PartnerHasSitesException();
        }

        db.getPartners().remove(em.getReference(Partner.class, cmd.getPartnerId()));


        return new VoidResult();
    }
}
