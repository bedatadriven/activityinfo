/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.map;

import java.util.Collection;

import org.sigmah.shared.dto.ActivityDTO;
import org.sigmah.shared.dto.AdminEntityDTO;
import org.sigmah.shared.dto.AdminLevelDTO;
import org.sigmah.shared.dto.CountryDTO;
import org.sigmah.shared.dto.HasAdminEntityValues;
import org.sigmah.shared.dto.SiteDTO;
import org.sigmah.shared.util.mapping.BoundingBoxDTO;

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
     * by {@link org.sigmah.shared.dto.AdminEntityDTO#getBounds()}  and
     * {@link org.sigmah.shared.dto.CountryDTO#getBounds()}
     *
     * @param activity
     * @param site
     * @return the normative lat/lng bounds
     */
    public static BoundingBoxDTO calculate(ActivityDTO activity, final SiteDTO site) {
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
     * by {@link org.sigmah.shared.dto.AdminEntityDTO#getBounds()}  and
     * {@link org.sigmah.shared.dto.CountryDTO#getBounds()}
     *
     * @param activity
     * @param entityAccessor  an adapter class that provides AdminEntity membership for some representation
     * of a site.
     * @return the normative lat/lng bounds
     */
    public static BoundingBoxDTO calculate(ActivityDTO activity, HasAdminEntityValues entityAccessor) {
        return calculate(activity.getDatabase().getCountry(), activity.getAdminLevels(),  entityAccessor);
    }


    /**
     * Calculates the normative lat/lng bounds for a given site as function of AdminEntity entity
     * membership.
     *
     * For example, if a site is marked as being within Country A, the Province B and Health Zone C,
     * the method will return the intersection of the bounds for A, B, and C, which are provided
     * by {@link org.sigmah.shared.dto.AdminEntityDTO#getBounds()}  and
     * {@link org.sigmah.shared.dto.CountryDTO#getBounds()}
     * @param country TODO
     * @param entityAccessor  an adapter class that provides AdminEntity membership for some representation
     * of a site.
     * @return the normative lat/lng bounds
     */
    public static BoundingBoxDTO calculate(CountryDTO country, Collection<AdminLevelDTO> levels, HasAdminEntityValues entityAccessor) {
        BoundingBoxDTO bounds = null;
        if(country != null) {
            bounds = new BoundingBoxDTO(country.getBounds());
        }
        if(bounds == null) {
            bounds = BoundingBoxDTO.maxGeoBounds();
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
    public static String name(ActivityDTO activity, BoundingBoxDTO bounds, final SiteDTO site) {
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
    public static String name(BoundingBoxDTO bounds, Collection<AdminLevelDTO> levels, HasAdminEntityValues getter) {
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

