/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.dao;

import com.google.inject.ImplementedBy;
import org.sigmah.shared.map.BaseMap;

import java.util.List;

/**
 * Data Access Object for the {@link org.sigmah.shared.map.BaseMap} objects
 * available to the user.
 *
 * @author Alex Bertram
 */
@ImplementedBy(BaseMapFsDAO.class)
public interface BaseMapDAO {

    BaseMap getBaseMap(String id);

    List<BaseMap> getBaseMaps();


}
