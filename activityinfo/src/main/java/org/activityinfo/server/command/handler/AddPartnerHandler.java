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
import org.activityinfo.server.domain.Partner;
import org.activityinfo.server.domain.User;
import org.activityinfo.server.domain.UserDatabase;
import org.activityinfo.server.domain.UserPermission;
import org.activityinfo.shared.command.AddPartner;
import org.activityinfo.shared.command.result.CommandResult;
import org.activityinfo.shared.command.result.CreateResult;
import org.activityinfo.shared.exception.CommandException;
import org.activityinfo.shared.exception.DuplicateException;
import org.activityinfo.shared.exception.IllegalAccessCommandException;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Set;

/**
 *
 * @see org.activityinfo.shared.command.AddPartner
 *
 * @author Alex Bertram
 */
public class AddPartnerHandler implements CommandHandler<AddPartner> {

    private final EntityManager em;

    @Inject
    public AddPartnerHandler(EntityManager em) {
        this.em = em;
    }

    public CommandResult execute(AddPartner cmd, User user) throws CommandException {

        UserDatabase db = em.find(UserDatabase.class, cmd.getDatabaseId());
        if(db.getOwner().getId() != user.getId()) {
            UserPermission perm = db.getPermissionByUser(user);
            if(perm == null || perm.isAllowDesign()) {
                throw new IllegalAccessCommandException();
            }
        }

        // first check to see if an organization by this name is already
        // a partner

        Set<Partner> dbPartners = db.getPartners();
        for(Partner partner : dbPartners) {
            if(partner.getName().equals(cmd.getPartner().getName())) {
                throw new DuplicateException();
            }
        }

        // now try to match this partner by name
        List<Partner> allPartners = em.createQuery("select p from Partner p where p.name = ?1")
                .setParameter(1, cmd.getPartner().getName())
                .getResultList();

        if(allPartners.size()!=0) {
            db.getPartners().add(allPartners.get(0));
            return new CreateResult(allPartners.get(0).getId());
        }

        // nope, have to create a new record
        Partner newPartner = new Partner();
        newPartner.setName(cmd.getPartner().getName());
        newPartner.setFullName(cmd.getPartner().getFullName());
        em.persist(newPartner);

        db.getPartners().add(newPartner);

        return new CreateResult(newPartner.getId());
    }
}
