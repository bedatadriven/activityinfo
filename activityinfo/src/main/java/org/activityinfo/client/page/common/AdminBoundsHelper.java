package org.activityinfo.client.page.common;

import org.activityinfo.shared.dto.*;

import java.util.Collection;

/**
 * Utility class to help calculate the lat/lng bounds of an
 * activity site from the admin entity membership
 *
 * @author Alex Bertram
 */
public class AdminBoundsHelper {

    public interface Getter {
        public AdminEntityModel get(int levelId);
    }

    public static Bounds calculate(ActivityModel activity, final SiteModel site) {
        return calculate(activity, new Getter() {
            public AdminEntityModel get(int levelId) {
                return site.getAdminEntity(levelId);
            }
        });
    }

    public static Bounds calculate(ActivityModel activity, Getter getter) {
        return calculate(activity, activity.getAdminLevels(),  getter);
    }

    public static Bounds calculate(ActivityModel activity, Collection<AdminLevelModel> levels, Getter getter) {

        Bounds bounds = null;

        if(activity.getDatabase().getCountry() != null) {
            bounds = new Bounds(activity.getDatabase().getCountry().getBounds());
        }

        if(bounds == null) {
            bounds = new Bounds(-180, -90, 180, 90);
        }

		for(AdminLevelModel level  : levels) {
			AdminEntityModel entity = getter.get(level.getId());
			if(entity != null && entity.hasBounds()) {
                bounds = bounds.intersect(entity.getBounds());
			}
		}

        return bounds;
    }

    public static String name(ActivityModel activity, Bounds bounds, final SiteModel site) {
        return name(activity, bounds, activity.getAdminLevels(), new Getter() {
            public AdminEntityModel get(int levelId) {
                return site.getAdminEntity(levelId);
            }
        });
    }

    public static String name(ActivityModel activity, Bounds bounds, Collection<AdminLevelModel> levels, Getter getter) {
        	// find the entities that are the limiting bounds.
		// E.g., if the user selects North Kivu, distict de North Kivu, and territoire
		// de Beni, the name we give to this bounds should just be 'Beni'.

		if(bounds == null) {
			return null;
		} else {
			StringBuilder sb = new StringBuilder();
			for(AdminLevelModel level : levels) {
				AdminEntityModel entity = getter.get(level.getId());
				if(entity!=null && entity.hasBounds()) {
					Bounds b = entity.getBounds();

					if(b!=null && (!b.contains(bounds) || b.equals(bounds))) {
						if(sb.length()!=0)
							sb.append(", ");
						sb.append(entity.getName());

					}
				}
			}
            return sb.toString();
        }
    }

}

