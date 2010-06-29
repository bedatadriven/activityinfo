/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.dao;

import com.google.inject.ImplementedBy;
import org.sigmah.server.dao.hibernate.AdminHibernateDAO;
import org.sigmah.server.domain.AdminEntity;
import org.sigmah.server.domain.AdminLevel;

import java.util.List;

/**
 * Data Access Object for {@link org.sigmah.server.domain.AdminEntity} classes.
 *
 * @author Alex Bertram
 */
@ImplementedBy(AdminHibernateDAO.class)
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


    Query query();

    public interface Query {
        Query level(int levelId);
        Query withParentEntityId(int parentEntityId);
        Query withSitesOfActivityId(int activityId);

        List<AdminEntity> execute();
    }
}