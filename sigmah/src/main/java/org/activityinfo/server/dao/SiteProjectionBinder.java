package org.activityinfo.server.dao;

import org.activityinfo.server.domain.AdminEntity;

/**
 *
 * Binds the results of a {@link org.activityinfo.server.dao.SiteTableDAO SiteTableDAO} query
 * to a particular storage class.
 *
 * TODO: Probably remove, there isn't a convincing case for another storage class outside of
 * {@link org.activityinfo.shared.dto.SiteDTO}
 *
 * @param <SiteT>
 */
public interface SiteProjectionBinder<SiteT> {
	SiteT newInstance(String[] properties, Object[] values);
	void setAdminEntity(SiteT site, AdminEntity entity);
	void addIndicatorValue(SiteT site, int indicatorId, int aggregationMethod, double value);
	void setAttributeValue(SiteT site, int attributeId, boolean value);
}
