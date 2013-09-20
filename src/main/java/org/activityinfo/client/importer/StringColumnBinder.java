package org.activityinfo.client.importer;

import com.extjs.gxt.ui.client.data.ModelData;

public class StringColumnBinder<T extends ModelData> implements ColumnBinder<T> {

    private final int columnIndex;
    private final String propertyName;
    
    public StringColumnBinder(int columnIndex, String propertyName) {
        super();
        this.columnIndex = columnIndex;
        this.propertyName = propertyName;
    }

    @Override
    public void bind(String[] row, T model) {
        if(columnIndex < row.length) {
            String value = row[columnIndex].trim();
            if(!value.equals("null")) {
                model.set(propertyName, value);
            }
        }
    }

}
