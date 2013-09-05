package org.activityinfo.client.importer;

import java.util.Date;

import com.extjs.gxt.ui.client.data.ModelData;
import com.google.gwt.i18n.client.DateTimeFormat;

public class DateColumnBinder<T extends ModelData> implements ColumnBinder<T> {

    private int columnIndex;
    private String propertyName;
    private DateTimeFormat format;
    
    
    public DateColumnBinder(int columnIndex, String propertyName, DateTimeFormat format) {
        super();
        this.columnIndex = columnIndex;
        this.propertyName = propertyName;
        this.format = format;
    }

    @Override
    public void bind(String[] row, T model) {
        String value = row[columnIndex].trim();
        if(value.length() == 0) {
            model.set(propertyName, null);
        } else {
            try {
                Date date = format.parse(value);
                model.set(propertyName, date);
            } catch(IllegalArgumentException e) {
                model.set(propertyName, null);
            }
        }
    }
}
