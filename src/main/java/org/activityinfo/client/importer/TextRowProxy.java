package org.activityinfo.client.importer;

import java.util.List;

import com.extjs.gxt.ui.client.data.BaseListLoadResult;
import com.extjs.gxt.ui.client.data.DataProxy;
import com.extjs.gxt.ui.client.data.DataReader;
import com.extjs.gxt.ui.client.data.ListLoadResult;
import com.extjs.gxt.ui.client.store.ListStore;
import com.google.common.collect.Lists;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class TextRowProxy implements DataProxy<ListLoadResult<ImportRowModel>> {

    private String delimeter = "\t";
    private String[] rows;
    private String[] headers;
    private int columnCount;
    private int numHeaderRows = 1;
    
    
    public TextRowProxy(String text) {
        this.rows = text.split("\n");
        
        // figure out how many columns we have
        if(rows.length == 0) {
            columnCount = 0;
        } else {
            headers = parseRow(0);
            columnCount = headers.length;
        }
    }
    
    private String[] parseRow(int index) {
        return getRowLine(index).split(delimeter);
    }
    
    private String getRowLine(int index) {
        return removeCarriageReturn(rows[index]);
    }

    private String removeCarriageReturn(String row) {
        if(row.endsWith("\r")) {
            return row.substring(0, row.length() - 1);
        } else {
            return row;
        }
    }

    public int getNumColumns() {
        return headers.length;
    }
    
    public ListStore<ImportColumnModel> createColumnStore() {
        
        ListStore<ImportColumnModel> store = new ListStore<ImportColumnModel>();
        
        for(int i=0;i!=columnCount;++i) {
            store.add(new ImportColumnModel(i, headers[i]));
        }
        return store;
    }

    @Override
    public void load(DataReader<ListLoadResult<ImportRowModel>> reader,
        Object loadConfig,
        AsyncCallback<ListLoadResult<ImportRowModel>> callback) {

        List<ImportRowModel> models = Lists.newArrayList();
        
        for(int i=numHeaderRows;i!=rows.length;++i) {
            String[] columns = parseRow(i);
            models.add(new ImportRowModel(columns));
        }
        callback.onSuccess(new BaseListLoadResult(models));
        
    }


    
}
