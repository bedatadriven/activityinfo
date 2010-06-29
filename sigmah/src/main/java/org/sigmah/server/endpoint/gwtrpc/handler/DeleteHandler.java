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

package org.sigmah.server.endpoint.gwtrpc.handler;

import com.google.inject.Inject;
import org.sigmah.server.domain.Deleteable;
import org.sigmah.server.domain.User;
import org.sigmah.server.domain.UserDatabase;
import org.sigmah.shared.command.Delete;
import org.sigmah.shared.command.result.CommandResult;

import javax.persistence.EntityManager;

/**
 * @author Alex Bertram
 * @see org.sigmah.shared.command.Delete
 * @see org.sigmah.server.domain.Deleteable
 */
public class DeleteHandler implements CommandHandler<Delete> {
    private EntityManager em;

    @Inject
    public DeleteHandler(EntityManager em) {
        this.em = em;
    }

    @Override
    public CommandResult execute(Delete cmd, User user) {

        // TODO check permissions for delete!
        // These handler should redirect to one of the Entity policy classes.
        Class entityClass = entityClassForEntityName(cmd.getEntityName());
        Deleteable entity = (Deleteable) em.find(entityClass, cmd.getId());

        entity.delete();

        return null;
    }

    private Class<Deleteable> entityClassForEntityName(String entityName) {
        try {
            return (Class<Deleteable>) Class.forName(UserDatabase.class.getPackage().getName() + "." + entityName);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Invalid entity name '" + entityName + "'");
        } catch (ClassCastException e) {
            throw new RuntimeException("Entity type '" + entityName + "' not Deletable");
        }
    }
}
