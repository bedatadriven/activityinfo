/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.shared.dao;

import org.sigmah.shared.domain.AdminEntity;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * Binds the results of a {@link org.sigmah.shared.dao.SiteTableDAO SiteTableDAO} query
 * to a particular storage class.
 *
 * TODO: Probably remove, there isn't a convincing case for another storage class outside of
 * {@link org.sigmah.shared.dto.SiteDTO}
 *
 * @param <SiteT>
 */
public interface SiteProjectionBinder<SiteT> {
	SiteT newInstance(String[] properties, ResultSet rs) throws SQLException;
	void setAdminEntity(SiteT site, AdminEntity entity);
	void addIndicatorValue(SiteT site, int indicatorId, int aggregationMethod, double value);
	void setAttributeValue(SiteT site, int attributeId, boolean value);
}
