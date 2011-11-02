/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.database.hibernate.dao;

import java.util.Map;

import org.sigmah.server.database.hibernate.entity.Site;

/**
 * Data Access Object for the {@link org.sigmah.server.database.hibernate.entity.Site} domain object.
 */
public interface SiteDAO extends DAO<Site, Integer> {

    /**
     * Efficiently updates the {@link org.sigmah.server.database.hibernate.entity.AttributeValue} of a
     * given {@link org.sigmah.server.database.hibernate.entity.Site}
     *
     * @param siteId the id of the Site entity to update
     * @param attributeValues a map of attribute ids => attribute value
     */
    void updateAttributeValues(int siteId, Map<Integer, Boolean> attributeValues);
}
