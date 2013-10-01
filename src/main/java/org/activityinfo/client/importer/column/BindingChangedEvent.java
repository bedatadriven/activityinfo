package org.activityinfo.client.importer.column;

import org.activityinfo.client.importer.ImportColumnModel;
import org.activityinfo.client.importer.ImportModel;

import com.extjs.gxt.ui.client.event.BaseEvent;

public class BindingChangedEvent extends BaseEvent {
    private ImportColumnModel columnModel;
    
    public BindingChangedEvent(ImportColumnModel columnModel) {
        super(ImportModel.COLUMNS_CHANGED);
        this.columnModel = columnModel;
    }

    public ImportColumnModel getColumnModel() {
        return columnModel;
    }    
}
