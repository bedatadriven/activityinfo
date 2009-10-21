package org.activityinfo.server.report.generator;

import org.activityinfo.server.dao.SiteProjectionBinder;
import org.activityinfo.server.domain.AdminEntity;
import org.activityinfo.server.domain.SiteData;

public class SiteDataBinder implements SiteProjectionBinder<SiteData>{

	@Override
	public SiteData newInstance(String[] properties, Object[] values) {
		return new SiteData(values);
	}

	
	@Override
	public void addIndicatorValue(SiteData site, int indicatorId,
			int aggregationMethod, double value) {
		
		site.indicatorValues.put(indicatorId, value);
	}


	@Override
	public void setAdminEntity(SiteData site, AdminEntity entity) {
		site.adminNames.put(entity.getLevel().getId(), entity.getName());
        site.adminEntities.put(entity.getLevel().getId(), entity);
	}

	public void setAttributeValue(SiteData site, int attributeId, boolean value) {
        if(value) {
            site.attributes.put(attributeId, value);
        }
	}

}
