package org.activityinfo.client.importer;

import org.activityinfo.shared.command.MatchLocation;

public class LocationNameBinder implements ColumnBinder<MatchLocation> {

    private int columnIndex;
    
    public LocationNameBinder(int columnIndex) {
        super();
        this.columnIndex = columnIndex;
    }

    @Override
    public void bind(String[] row, MatchLocation model) {
        if(columnIndex < row.length) {
            model.setName(row[columnIndex]);
        }
    }

}
