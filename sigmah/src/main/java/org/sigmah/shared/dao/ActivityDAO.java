/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.shared.dao;

import java.util.Set;

import org.sigmah.shared.domain.Activity;

/**
 * DAO for the {@link org.sigmah.shared.domain.Activity} domain object. Implemented automatically by proxy,
 * see the Activity class for query definitions.
 *
 * @author Alex Bertram
 */
public interface ActivityDAO extends DAO<Activity, Integer> {

    Integer queryMaxSortOrder(int databaseId);
    
    Set<Activity> getActivitiesByDatabaseId(int databaseId); 

}
