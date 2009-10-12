package org.activityinfo.shared.command.result;

import org.activityinfo.shared.dto.MapIconModel;

import java.util.List;

/**
 *
 * @see org.activityinfo.shared.command.GetMapIcons
 *
 * @author Alex Bertram
 */
public class MapIconResult extends ListResult<MapIconModel> {

    public MapIconResult() {
    }

    public MapIconResult(List<MapIconModel> data) {
        super(data);
    }
}
