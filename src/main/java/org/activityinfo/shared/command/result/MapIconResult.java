/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.shared.command.result;

import java.util.List;

import org.activityinfo.shared.dto.MapIconDTO;

/**
 *
 * @see org.activityinfo.shared.command.GetMapIcons
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
