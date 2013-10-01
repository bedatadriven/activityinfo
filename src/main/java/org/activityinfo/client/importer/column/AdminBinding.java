package org.activityinfo.client.importer.column;

import org.activityinfo.shared.command.MatchLocation;
import org.activityinfo.shared.dto.AdminLevelDTO;

public class AdminBinding extends ColumnBinding {
    private final AdminLevelDTO level;
    
    public AdminBinding(AdminLevelDTO level) {
        this.level = level;
    }

    @Override
    public String getLabel() {
        return level.getName();
    }
    
    @Override
    public int hashCode() {
        return level.hashCode();
    }

    @Override
    public void bindLocation(String columnValue, MatchLocation location) {
        location.getAdminLevels().put(level.getId(), columnValue);
    }
}
