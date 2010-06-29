package org.sigmah.client.page.common;

import org.sigmah.shared.dto.*;

import java.util.Collection;

/**
 * Utility class to help calculate the lat/lng bounds of an
 * activity site from the admin entity membership
 *
 * @author Alex Bertram
 */
public class AdminBoundsHelper {

    public interface Getter {
        public AdminEntityDTO get(int levelId);
    }

    public static BoundingBoxDTO calculate(ActivityDTO activity, final SiteDTO site) {
        return calculate(activity, new Getter() {
            public AdminEntityDTO get(int levelId) {
                return site.getAdminEntity(levelId);
            }
        });
    }

    public static BoundingBoxDTO calculate(ActivityDTO activity, Getter getter) {
        return calculate(activity, activity.getAdminLevels(),  getter);
    }

    public static BoundingBoxDTO calculate(ActivityDTO activity, Collection<AdminLevelDTO> levels, Getter getter) {

        BoundingBoxDTO bounds = null;

        if(activity.getDatabase().getCountry() != null) {
            bounds = new BoundingBoxDTO(activity.getDatabase().getCountry().getBounds());
        }

        if(bounds == null) {
            bounds = new BoundingBoxDTO(-180, -90, 180, 90);
        }

		for(AdminLevelDTO level  : levels) {
			AdminEntityDTO entity = getter.get(level.getId());
			if(entity != null && entity.hasBounds()) {
                bounds = bounds.intersect(entity.getBounds());
			}
		}

        return bounds;
    }

    public static String name(ActivityDTO activity, BoundingBoxDTO bounds, final SiteDTO site) {
        return name(activity, bounds, activity.getAdminLevels(), new Getter() {
            public AdminEntityDTO get(int levelId) {
                return site.getAdminEntity(levelId);
            }
        });
    }

    public static String name(ActivityDTO activity, BoundingBoxDTO bounds, Collection<AdminLevelDTO> levels, Getter getter) {
        	// find the entities that are the limiting bounds.
		// E.g., if the user selects North Kivu, distict de North Kivu, and territoire
		// de Beni, the name we give to this bounds should just be 'Beni'.

		if(bounds == null) {
			return null;
		} else {
			StringBuilder sb = new StringBuilder();
			for(AdminLevelDTO level : levels) {
				AdminEntityDTO entity = getter.get(level.getId());
				if(entity!=null && entity.hasBounds()) {
					BoundingBoxDTO b = entity.getBounds();

					if(b!=null && (!b.contains(bounds) || b.equals(bounds))) {
						if(sb.length()!=0) {
                            sb.append(", ");
                        }
						sb.append(entity.getName());

					}
				}
			}
            return sb.toString();
        }
    }

}

