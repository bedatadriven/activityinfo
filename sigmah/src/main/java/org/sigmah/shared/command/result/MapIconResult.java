/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.shared.command.result;

import org.sigmah.shared.dto.MapIconDTO;

import java.util.List;

/**
 *
 * @see org.sigmah.shared.command.GetMapIcons
 *
 * @author Alex Bertram
 */
public class MapIconResult extends ListResult<MapIconDTO> {

    public MapIconResult() {
    }

    public MapIconResult(List<MapIconDTO> data) {
        super(data);
    }
}
