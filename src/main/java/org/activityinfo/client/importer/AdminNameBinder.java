package org.activityinfo.client.importer;

import org.activityinfo.shared.command.MatchLocation;

public class AdminNameBinder implements ColumnBinder<MatchLocation> {

    private int columnIndex;
    private int levelId;
    
    public AdminNameBinder(int columnIndex, int levelId) {
        super();
        this.columnIndex = columnIndex;
        this.levelId = levelId;
    }

    @Override
    public void bind(String[] row, MatchLocation model) {
        if(columnIndex < row.length) {
            model.getAdminLevels().put(levelId, row[columnIndex]);
        }
    }

}
