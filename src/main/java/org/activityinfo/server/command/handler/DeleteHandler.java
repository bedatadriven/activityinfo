package org.activityinfo.server.command.handler;

/*
 * #%L
 * ActivityInfo Server
 * %%
 * Copyright (C) 2009 - 2013 UNICEF
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import java.util.Date;

import javax.persistence.EntityManager;

import org.activityinfo.server.database.hibernate.entity.Deleteable;
import org.activityinfo.server.database.hibernate.entity.ReallyDeleteable;
import org.activityinfo.server.database.hibernate.entity.Site;
import org.activityinfo.server.database.hibernate.entity.User;
import org.activityinfo.server.database.hibernate.entity.UserDatabase;
import org.activityinfo.shared.command.Delete;
import org.activityinfo.shared.command.result.CommandResult;

import com.google.inject.Inject;

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
        Object entity = em.find(entityClass, cmd.getId());

        if (entity instanceof Deleteable) {
            Deleteable deleteable = (Deleteable) entity;
            deleteable.delete();

            if (entity instanceof Site) {
                ((Site) entity).setDateEdited(new Date());
            }
        }

        if (entity instanceof ReallyDeleteable) {
            ReallyDeleteable reallyDeleteable = (ReallyDeleteable) entity;
            reallyDeleteable.deleteReferences();
            em.remove(reallyDeleteable);
        }

        return null;
    }

    private Class<Deleteable> entityClassForEntityName(String entityName) {
        try {
            return (Class<Deleteable>) Class.forName(UserDatabase.class
                .getPackage().getName() + "." + entityName);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Invalid entity name '" + entityName
                + "'", e);
        } catch (ClassCastException e) {
            throw new RuntimeException("Entity type '" + entityName
                + "' not Deletable", e);
        }
    }
}
