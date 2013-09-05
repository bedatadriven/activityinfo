package org.activityinfo.client.importer;

import java.util.List;

import com.extjs.gxt.ui.client.store.ListStore;

public class ImportData {
    
    private ImportRowStore rowStore;
    private List<ImportColumnModel> columns;
    
    public ImportData(String text) {
        super();
        this.rowStore = new ImportRowStore(text);
        this.columns = rowStore.createColumnStore().getModels();
    }

    public ImportRowStore getRowStore() {
        return rowStore;
    }

    public ListStore<ImportColumnModel> createColumnStore() {
        return rowStore.createColumnStore();
    }
    
    public List<ImportColumnModel> getColumns() {
        return columns;
    }
 
}
