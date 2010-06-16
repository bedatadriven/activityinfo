package org.activityinfo.server.dao;

import org.activityinfo.server.domain.Site;

import java.util.Map;
import java.util.Set;

public interface SiteDAO extends DAO<Site, Integer> {

    void updateAttributeValues(int siteId, Map<Integer, Boolean> attributeValues);



}
