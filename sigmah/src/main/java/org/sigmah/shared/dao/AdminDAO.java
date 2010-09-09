/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.shared.dao;

import java.util.List;
import java.util.Set;

import org.sigmah.shared.domain.AdminEntity;
import org.sigmah.shared.domain.AdminLevel;

/**
 * Data Access Object for {@link org.sigmah.shared.domain.AdminEntity} classes.
 *
 * @author Alex Bertram
 */
public interface AdminDAO extends DAO<AdminEntity, Integer> {

    /**
     * @param levelId The id of the administrative level for which to return the entities
     * @return A list of administrative entities that constitute an administrative
     *         level. (e.g. return all provinces, return all districts, etc)
     */
    List<AdminEntity> findRootEntities(int levelId);

    /**
     * Returns
     *
     * @param levelId id of the {@link AdminLevel} to search
     * @param parentEntityId the entity parent 
     * @return A list of the children of a given admin entity for at a given level.
     */
    List<AdminEntity> findChildEntities(int levelId, int parentEntityId);

    /**
     * Returns
     *
     * @param entityLevelId id of the {@link AdminLevel} to search
     * @param parentEntityId the entity parent 
     * @param activityId activity linked to a related site
     * @return A list of the children of a given admin entity for at a given level.
     */
    List<AdminEntity> find(int entityLevelId, int parentEntityId, int activityId);

    
    List<AdminEntity> findBySiteIds(Set<Integer> siteIds);
    
    
    
    public interface Query {
        Query level(int levelId);
        Query withParentEntityId(int parentEntityId);
        Query withSitesOfActivityId(int activityId);

        List<AdminEntity> execute();
    }
    
    Query query();
}


