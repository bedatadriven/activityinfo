/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.client.map;

import java.util.Collection;

import org.activityinfo.shared.dto.ActivityDTO;
import org.activityinfo.shared.dto.AdminEntityDTO;
import org.activityinfo.shared.dto.AdminLevelDTO;
import org.activityinfo.shared.dto.CountryDTO;
import org.activityinfo.shared.dto.HasAdminEntityValues;
import org.activityinfo.shared.dto.SiteDTO;
import org.activityinfo.shared.util.mapping.Extents;

/**
 * Utility class to help calculate the lat/lng bounds of an
 * activity site from the admin entity membership
 *
 * @author Alex Bertram
 */
public final class AdminBoundsHelper {

    private AdminBoundsHelper() {}

    /**
     * Calculates the normative lat/lng bounds for a given site as function of AdminEntity entity
     * membership.
     *
     * For example, if a site is marked as being within Country A, the Province B and Health Zone C,
     * the method will return the intersection of the bounds for A, B, and C, which are provided
     * by {@link org.activityinfo.shared.dto.AdminEntityDTO#getBounds()}  and
     * {@link org.activityinfo.shared.dto.CountryDTO#getBounds()}
     *
     * @param activity
     * @param site
     * @return the normative lat/lng bounds
     */
    public static Extents calculate(ActivityDTO activity, final SiteDTO site) {
        return calculate(activity, new HasAdminEntityValues() {
            @Override
			public AdminEntityDTO getAdminEntity(int levelId) {
                return site.getAdminEntity(levelId);
            }
        });
    }

    /**
     * Calculates the normative lat/lng bounds for a given site as function of AdminEntity entity
     * membership.
     *
     * For example, if a site is marked as being within Country A, the Province B and Health Zone C,
     * the method will return the intersection of the bounds for A, B, and C, which are provided
     * by {@link org.activityinfo.shared.dto.AdminEntityDTO#getBounds()}  and
     * {@link org.activityinfo.shared.dto.CountryDTO#getBounds()}
     *
     * @param activity
     * @param entityAccessor  an adapter class that provides AdminEntity membership for some representation
     * of a site.
     * @return the normative lat/lng bounds
     */
    public static Extents calculate(ActivityDTO activity, HasAdminEntityValues entityAccessor) {
        return calculate(activity.getDatabase().getCountry(), activity.getAdminLevels(),  entityAccessor);
    }


    /**
     * Calculates the normative lat/lng bounds for a given site as function of AdminEntity entity
     * membership.
     *
     * For example, if a site is marked as being within Country A, the Province B and Health Zone C,
     * the method will return the intersection of the bounds for A, B, and C, which are provided
     * by {@link org.activityinfo.shared.dto.AdminEntityDTO#getBounds()}  and
     * {@link org.activityinfo.shared.dto.CountryDTO#getBounds()}
     * @param country TODO
     * @param entityAccessor  an adapter class that provides AdminEntity membership for some representation
     * of a site.
     * @return the normative lat/lng bounds
     */
    public static Extents calculate(CountryDTO country, Collection<AdminLevelDTO> levels, HasAdminEntityValues entityAccessor) {
        Extents bounds = null;
        if(country != null) {
            bounds = new Extents(country.getBounds());
        }
        if(bounds == null) {
            bounds = Extents.maxGeoBounds();
        }

        for(AdminLevelDTO level  : levels) {
            AdminEntityDTO entity = entityAccessor.getAdminEntity(level.getId());
            if(entity != null && entity.hasBounds()) {
                bounds = bounds.intersect(entity.getBounds());
            }
        }

        return bounds;
    }

    /**
     * Computes a human-readable name for bounding box returned by the <code>calculate()</code> methods
     * 
     *
     *
     * @param activity
     * @param bounds
     * @param site
     * @return
     */
    public static String name(ActivityDTO activity, Extents bounds, final SiteDTO site) {
        return name(bounds, activity.getAdminLevels(), new HasAdminEntityValues() {
            @Override
			public AdminEntityDTO getAdminEntity(int levelId) {
                return site.getAdminEntity(levelId);
            }
        });
    }

    /**
     * @param bounds
     * @param levels
     * @param getter
     * @return
     */
    public static String name(Extents bounds, Collection<AdminLevelDTO> levels, HasAdminEntityValues getter) {
        // find the entities that are the limiting bounds.
        // E.g., if the user selects North Kivu, distict de North Kivu, and territoire
        // de Beni, the name we give to this bounds should just be 'Beni'.

        if(bounds == null) {
            return null;
        } else {
            StringBuilder sb = new StringBuilder();
            for(AdminLevelDTO level : levels) {
                AdminEntityDTO entity = getter.getAdminEntity(level.getId());
                if(entity!=null && entity.hasBounds()) {
                    Extents b = entity.getBounds();

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

