package org.activityinfo.client.importer;

import java.util.List;

import com.extjs.gxt.ui.client.data.BaseListLoader;
import com.extjs.gxt.ui.client.data.ListLoadResult;
import com.extjs.gxt.ui.client.data.ListLoader;
import com.extjs.gxt.ui.client.store.ListStore;

public class ImportData {
    
    private TextRowProxy proxy;
    private ListStore<ImportRowModel> rowStore;
    private ListLoader<ListLoadResult<ImportRowModel>> rowLoader;
    
    private List<ImportColumnModel> columns;
    
    public ImportData(String text) {
        super();
        this.proxy = new TextRowProxy(text);
        this.rowLoader = new BaseListLoader<ListLoadResult<ImportRowModel>>(proxy);
        this.rowStore = new ListStore<ImportRowModel>(rowLoader);
        
        this.columns = proxy.createColumnStore().getModels();
        
        this.rowLoader.load();
    }

    public ListStore<ImportRowModel> getRowStore() {
        return rowStore;
    }
    
    public List<ImportColumnModel> getColumns() {
        return columns;
    }

    public int getNumColumns() {
        return proxy.getNumColumns();
    }
}
