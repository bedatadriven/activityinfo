/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.dao;

import org.sigmah.server.domain.Activity;

/**
 * DAO for the {@link org.sigmah.server.domain.Activity} domain object. Implemented automatically by proxy,
 * see the Activity class for query definitions.
 *
 * @author Alex Bertram
 */
public interface ActivityDAO extends DAO<Activity, Integer> {

    Integer queryMaxSortOrder(int databaseId);

}
