/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.dao;

import java.util.List;

import org.sigmah.shared.map.TileBaseMap;

import com.google.inject.ImplementedBy;

/**
 * Data Access Object for the {@link org.sigmah.shared.map.TileBaseMap} objects
 * available to the user.
 *
 * @author Alex Bertram
 */
@ImplementedBy(BaseMapDAOImpl.class)
public interface BaseMapDAO {

    TileBaseMap getBaseMap(String id);

    List<TileBaseMap> getBaseMaps();


}
