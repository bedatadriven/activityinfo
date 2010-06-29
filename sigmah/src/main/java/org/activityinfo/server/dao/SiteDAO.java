package org.activityinfo.server.dao;

import org.activityinfo.server.domain.Site;

import java.util.Map;

/**
 * Data Access Object for the {@link org.activityinfo.server.domain.Site} domain object.
 */
public interface SiteDAO extends DAO<Site, Integer> {

    /**
     * Efficiently updates the {@link org.activityinfo.server.domain.AttributeValue} of a
     * given {@link org.activityinfo.server.domain.Site}
     *
     * @param siteId the id of the Site entity to update
     * @param attributeValues a map of attribute ids => attribute value
     */
    void updateAttributeValues(int siteId, Map<Integer, Boolean> attributeValues);

}
