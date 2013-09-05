package org.activityinfo.client.importer;

import com.extjs.gxt.ui.client.data.BaseModelData;

public class ImportColumnModel extends BaseModelData {

    private int columnIndex;
    
    public ImportColumnModel(int columnIndex, String header, String examples) {
        this.columnIndex = columnIndex;
        set("id", Integer.toString(columnIndex));
        set("header", header);
        set("examples", examples);
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
}

