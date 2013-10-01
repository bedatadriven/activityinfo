package org.activityinfo.client.importer.column;

import org.activityinfo.shared.command.MatchLocation;
import org.activityinfo.shared.dto.LocationTypeDTO;

public class LocationNameBinding extends ColumnBinding {

    private LocationTypeDTO locationType;

    public LocationNameBinding(LocationTypeDTO locationType) {
        this.locationType = locationType;
    }

    @Override
    public String getLabel() {
        return locationType.getName();
    }

    @Override
    public void bindLocation(String columnValue, MatchLocation location) {
        location.setName(columnValue);
    }
}
