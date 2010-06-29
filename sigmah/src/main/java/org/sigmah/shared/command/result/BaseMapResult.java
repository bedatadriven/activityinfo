/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.shared.command.result;

import org.sigmah.shared.map.BaseMap;

import java.util.List;

/**
 *
 * List of <code>BaseMap</code>s returned by the <code>GetBaseMaps</code> command.
 *
 * @see org.sigmah.shared.map.BaseMap
 * @see org.sigmah.shared.command.GetBaseMaps
 *
 * @author Alex Bertram
 */
public class BaseMapResult implements CommandResult  {

    List<BaseMap> baseMaps;

    private BaseMapResult() {
    }

    public BaseMapResult(List<BaseMap> baseMaps) {
        this.baseMaps = baseMaps;
    }

    public List<BaseMap> getBaseMaps() {
        return baseMaps;
    }

    public void setBaseMaps(List<BaseMap> baseMaps) {
        this.baseMaps = baseMaps;
    }
}
