/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.dao;

import org.sigmah.shared.dao.DAO;
import org.sigmah.shared.domain.Location;

/**
 * DAO for the {@link org.sigmah.shared.domain.Location} domain object.
 *
 * @author Alex Bertram
 */
public interface LocationDAO extends DAO<Location, Integer> {

    /**
     * Adds a link between the given {@link org.sigmah.shared.domain.Location} and the
     * given {@link org.sigmah.shared.domain.AdminEntity AdminEntity}. If a link with another
     * AdminEntity exists belonging to the same {@link org.sigmah.shared.domain.AdminLevel AdminLevel},
     * it is removed.
     *
     */
    void updateAdminMembership(int locationId, int adminLevelId, int adminEntityId);


    /**
     * Adds a link between the given {@link org.sigmah.shared.domain.Location Location} and
     * {@link org.sigmah.shared.domain.AdminEntity AdminEntity}
     * @param locationId
     * @param adminEntityId
     */
    void addAdminMembership(int locationId, int adminEntityId);

    /**
     * Removes the link between the given {@link org.sigmah.shared.domain.Location Location}
     * and any {@link org.sigmah.shared.domain.AdminEntity AdminEntity} belonging to the
     * given {@link org.sigmah.shared.domain.AdminLevel}
     *
     * @param locationId
     * @param adminLevelId
     */
    void removeMembership(int locationId, int adminLevelId);
    
}
