/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.endpoint.gwtrpc.handler;

import com.google.inject.Inject;
import org.sigmah.shared.command.AddPartner;
import org.sigmah.shared.command.handler.CommandHandler;
import org.sigmah.shared.command.result.CommandResult;
import org.sigmah.shared.command.result.CreateResult;
import org.sigmah.shared.domain.OrgUnit;
import org.sigmah.shared.domain.User;
import org.sigmah.shared.domain.UserDatabase;
import org.sigmah.shared.domain.UserPermission;
import org.sigmah.shared.exception.CommandException;
import org.sigmah.shared.exception.DuplicateException;
import org.sigmah.shared.exception.IllegalAccessCommandException;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Set;

/**
 * @author Alex Bertram
 * @see org.sigmah.shared.command.AddPartner
 */
public class AddPartnerHandler implements CommandHandler<AddPartner> {

    private final EntityManager em;

    @Inject
    public AddPartnerHandler(EntityManager em) {
        this.em = em;
    }

    public CommandResult execute(AddPartner cmd, User user) throws CommandException {

        UserDatabase db = em.find(UserDatabase.class, cmd.getDatabaseId());
        if (db.getOwner().getId() != user.getId()) {
            UserPermission perm = db.getPermissionByUser(user);
            if (perm == null || !perm.isAllowManageAllUsers()) {
                throw new IllegalAccessCommandException("The user does not have the manageAllUsers permission.");
            }
        }

        // first check to see if an organization by this name is already
        // a partner

        Set<OrgUnit> dbPartners = db.getPartners();
        for (OrgUnit partner : dbPartners) {
            if (partner.getName().equals(cmd.getPartner().getName())) {
                throw new DuplicateException();
            }
        }

        // now try to match this partner by name
        List<OrgUnit> allPartners = em.createQuery("select p from OrgUnit p where p.name = ?1")
                .setParameter(1, cmd.getPartner().getName())
                .getResultList();

        if (allPartners.size() != 0) {
            db.getPartners().add(allPartners.get(0));
            return new CreateResult(allPartners.get(0).getId());
        }

        // nope, have to create a new record
        OrgUnit newPartner = new OrgUnit();
        newPartner.setName(cmd.getPartner().getName());
        newPartner.setFullName(cmd.getPartner().getFullName());
        em.persist(newPartner);

        db.getPartners().add(newPartner);

        return new CreateResult(newPartner.getId());
    }
}
