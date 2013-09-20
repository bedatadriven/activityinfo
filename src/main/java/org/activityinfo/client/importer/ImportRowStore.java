package org.activityinfo.client.importer;

import com.extjs.gxt.ui.client.store.ListStore;

public class ImportRowStore extends ListStore<ImportRowModel> {

    private String delimeter = "\t";
    private String[] rows;
    private String[] headers;
    private int columnCount;
    private int numHeaderRows = 1;
    
    public ImportRowStore(String text) {
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

    @Override
    public ImportRowModel getAt(int index) {
        return new ImportRowModel(parseRow(index + numHeaderRows));
    }

    @Override
    public int getCount() {
        return rows.length - numHeaderRows;
    }
  
    public ListStore<ImportColumnModel> createColumnStore() {
        
        ListStore<ImportColumnModel> store = new ListStore<ImportColumnModel>();
        
        for(int i=0;i!=columnCount;++i) {
            store.add(new ImportColumnModel(i, headers[i], composeSampleList(i)));
        }
        return store;
    }

    private String composeSampleList(int columnIndex) {
        StringBuilder example = new StringBuilder();
        int exampleCount = 0;
        int rowIndex = 0;
        while(exampleCount < 10 && rowIndex < getCount()) {
            String[] row = parseRow(rowIndex+numHeaderRows);
            if(columnIndex < row.length) {
                String value = row[columnIndex].trim();
                if(value.length() > 0) {
                    if(exampleCount > 0) {
                        example.append(", ");
                    }
                    example.append(value);
                    exampleCount ++;
                }
            }
            rowIndex ++;
        }
        return example.toString();
    }
    
    public void forEachRow(ImportRowProcessor processor) {
        for(int i=0;i!=getCount();++i) {
            String[] columns = parseRow(i+numHeaderRows);
            processor.process(i, columns);
        }
    }
    
}
