package org.sigmah.server.domain;

import org.sigmah.shared.domain.*;
import org.sigmah.shared.map.TileBaseMap;

/**
 * List of persistent classes managed by Hibernate.
 *
 * IMPORTANT: the order of these classes is important:
 *
 */
public class PersistentClasses {
    public static final Class<?>[] LIST = {
            TileBaseMap.class,
            IndicatorValue.class,
            AttributeValue.class,
            ReportingPeriod.class,
            Site.class,
            OrgUnitPermission.class,
            UserPermission.class,
            OrgUnit.class,
            Location.class,
            Indicator.class,
            Attribute.class,
            AttributeGroup.class,
            Activity.class,
            ReportSubscription.class,
            ReportDefinition.class,
            UserDatabase.class,
            Authentication.class,
            User.class,
            LocationType.class,
            AdminEntity.class,
            AdminLevel.class,
            Country.class,
            Project2.class
    };
}
