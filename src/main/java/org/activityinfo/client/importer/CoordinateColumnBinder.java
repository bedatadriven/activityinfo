package org.activityinfo.client.importer;

import org.activityinfo.client.widget.CoordinateEditor;
import org.activityinfo.client.widget.CoordinateField.Axis;
import org.activityinfo.shared.command.MatchLocation;
import org.activityinfo.shared.dto.LocationDTO;
import org.activityinfo.shared.map.CoordinateFormatException;

public class CoordinateColumnBinder implements ColumnBinder<MatchLocation> {

    private int columnIndex;
    private Axis axis;
    private CoordinateEditor editor;
    
    public CoordinateColumnBinder(int columnIndex, Axis axis) {
        super();
        this.columnIndex = columnIndex;
        this.editor = new CoordinateEditor(axis);
        this.editor.setRequireSign(false);
        this.axis = axis;
    }

    @Override
    public void bind(String[] row, MatchLocation model) {
        try {
            double coordinateValue = editor.parse(row[columnIndex]);
            switch(axis) {
            case LATITUDE:
                model.setLatitude(coordinateValue);
                break;
            case LONGITUDE:
                model.setLongitude(coordinateValue);
                break;
            }
        } catch(CoordinateFormatException e) {
          
        }
    }    
}
