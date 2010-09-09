/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.shared.domain;

import com.bedatadriven.rebar.persistence.client.PersistenceUnit;
import com.bedatadriven.rebar.persistence.mapping.client.BindEntities;

@BindEntities({ Country.class, LocationType.class, User.class,
		UserPermission.class, Activity.class, AdminEntity.class,
		UserDatabase.class, AdminLevel.class, Attribute.class,
		AttributeGroup.class, AttributeValue.class, Indicator.class,
		IndicatorValue.class, Location.class, OrgUnit.class, Site.class })
public interface ActivityInfoOfflineUnit extends PersistenceUnit {

}
