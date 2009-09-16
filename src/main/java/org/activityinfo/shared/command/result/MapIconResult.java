package org.activityinfo.shared.command.result;

import org.activityinfo.shared.dto.MapIconModel;

import java.util.List;
/*
 * @author Alex Bertram
 */

public class MapIconResult extends ListResult<MapIconModel> {

    public MapIconResult() {
    }

    public MapIconResult(List<MapIconModel> data) {
        super(data);
    }
}
