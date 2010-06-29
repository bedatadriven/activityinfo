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
 * Copyright 2010 Alex Bertram and contributors.
 */

package org.sigmah.server.policy;

import org.sigmah.server.domain.User;

/**
 * EntityPolicies are responsible for creating and updating entities on behalf of users.
 *
 * @param <T>
 */
public interface EntityPolicy<T> {

    /**
     * Creates the entity of type T on behalf of the given user, initialized with
     * the given properties.
     *
     * @param user  The user on whose behalf this entity is to be created. The user most
     * have appropriate authorization to create the particular entity.
     * @param properties  A map between property names and property values
     * @return the primary key of the newly created entity
     */
    Object create(User user, PropertyMap properties);

    void update(User user, Object entityId, PropertyMap changes);


}
