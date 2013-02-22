package org.activityinfo.server.command.handler.crud;

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

import org.activityinfo.server.database.hibernate.entity.User;

/**
 * EntityPolicies are responsible for creating and updating entities on behalf
 * of users.
 * 
 * @param <T>
 */
public interface EntityPolicy<T> {

    /**
     * Creates the entity of type T on behalf of the given user, initialized
     * with the given properties.
     * 
     * @param user
     *            The user on whose behalf this entity is to be created. The
     *            user most have appropriate authorization to create the
     *            particular entity.
     * @param properties
     *            A map between property names and property values
     * @return the primary key of the newly created entity
     */
    Object create(User user, PropertyMap properties);

    void update(User user, Object entityId, PropertyMap changes);

}
