/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
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
