/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.shared.command.result;

import java.util.List;

import org.activityinfo.shared.map.TileBaseMap;

/**
 *
 * List of <code>BaseMap</code>s returned by the <code>GetBaseMaps</code> command.
 *
 * @see org.activityinfo.shared.map.TileBaseMap
 * @see org.activityinfo.shared.command.GetBaseMaps
 *
 * @author Alex Bertram
 */
public class BaseMapResult implements CommandResult  {

    List<TileBaseMap> baseMaps;
    
    public BaseMapResult() {
    	
    }

    public BaseMapResult(List<TileBaseMap> maps) {
        this.baseMaps = maps;
    }

    public List<TileBaseMap> getBaseMaps() {
        return baseMaps;
    }
}
