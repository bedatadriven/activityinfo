package org.activityinfo.shared.command.result;

import org.activityinfo.shared.dto.MapIconDTO;

import java.util.List;

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
