package org.sigmah.server.domain;

import org.sigmah.shared.domain.Activity;
import org.sigmah.shared.domain.AdminEntity;
import org.sigmah.shared.domain.AdminLevel;
import org.sigmah.shared.domain.Attribute;
import org.sigmah.shared.domain.AttributeGroup;
import org.sigmah.shared.domain.AttributeValue;
import org.sigmah.shared.domain.Country;
import org.sigmah.shared.domain.Indicator;
import org.sigmah.shared.domain.IndicatorValue;
import org.sigmah.shared.domain.Location;
import org.sigmah.shared.domain.LocationType;
import org.sigmah.shared.domain.LockedPeriod;
import org.sigmah.shared.domain.OrgUnit;
import org.sigmah.shared.domain.Project;
import org.sigmah.shared.domain.ReportingPeriod;
import org.sigmah.shared.domain.Site;
import org.sigmah.shared.domain.User;
import org.sigmah.shared.domain.UserDatabase;
import org.sigmah.shared.domain.UserPermission;
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
            Project.class,
            LockedPeriod.class
    };
}
