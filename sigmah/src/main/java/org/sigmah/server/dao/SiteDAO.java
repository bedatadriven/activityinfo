/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.dao;

import org.sigmah.shared.dao.DAO;
import org.sigmah.shared.domain.Site;

import java.util.Map;

/**
 * Data Access Object for the {@link org.sigmah.shared.domain.Site} domain object.
 */
public interface SiteDAO extends DAO<Site, Integer> {

    /**
     * Efficiently updates the {@link org.sigmah.shared.domain.AttributeValue} of a
     * given {@link org.sigmah.shared.domain.Site}
     *
     * @param siteId the id of the Site entity to update
     * @param attributeValues a map of attribute ids => attribute value
     */
    void updateAttributeValues(int siteId, Map<Integer, Boolean> attributeValues);

}
