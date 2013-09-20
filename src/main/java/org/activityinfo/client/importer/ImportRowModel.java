package org.activityinfo.client.importer;

import java.util.Collection;
import java.util.Map;

import com.extjs.gxt.ui.client.data.ModelData;

public class ImportRowModel implements ModelData {

    private String[] columns;
    
    public ImportRowModel(String[] columns) {
        this.columns = columns;
    }

    @Override
    public <X> X get(String property) {
        int columnIndex = Integer.parseInt(property);
        return (X)get(columnIndex);
    }

    protected String get(int columnIndex) {
        if(columnIndex < columns.length) {
            return columns[columnIndex];
        } else {
            return "";
        }
    }

    @Override
    public Map<String, Object> getProperties() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Collection<String> getPropertyNames() {
        throw new UnsupportedOperationException();
    }

    @Override
    public <X> X remove(String property) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <X> X set(String property, X value) {
        throw new UnsupportedOperationException();
    }
}
