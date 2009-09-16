package org.activityinfo.server.dao;

import org.activityinfo.server.domain.AdminEntity;

public interface SiteProjectionBinder<SiteT> {

	public SiteT newInstance(String[] properties, Object[] values);
	
	public void setAdminEntity(SiteT site, AdminEntity entity);
	
	public void addIndicatorValue(SiteT site, int indicatorId, int aggregationMethod, double value);
	
	public void setAttributeValue(SiteT site, int attributeId, boolean value);

}
