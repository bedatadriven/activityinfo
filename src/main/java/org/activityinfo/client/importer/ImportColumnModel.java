package org.activityinfo.client.importer;

import org.activityinfo.client.importer.column.ColumnBinding;
import org.activityinfo.client.importer.column.Ignore;

import com.extjs.gxt.ui.client.data.BaseModelData;

public class ImportColumnModel extends BaseModelData {

    private int columnIndex;
    private ColumnBinding binding = Ignore.INSTANCE;
    
    public ImportColumnModel(int columnIndex, String header) {
        this.columnIndex = columnIndex;
        set("id", Integer.toString(columnIndex));
        set("header", header);
    }
    
    public String getId() {
        return get("id");
    }
    
    public String getHeader() {
        return get("header");
    }
    
    public String getExamples() {
        return get("examples");
    }

    public int getColumnIndex() {
        return columnIndex;
    }

    public ColumnBinding getBinding() {
        return binding;
    }

    public void setBinding(ColumnBinding binding) {
        this.binding = binding;
    }
}

