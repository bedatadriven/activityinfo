package org.activityinfo.client.importer;

import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;

public class ImportGridCellRenderer implements GridCellRenderer<ImportRowModel> {

    private final ImportColumnModel columnModel;
    
    public ImportGridCellRenderer(ImportColumnModel columnModel) {
        super();
        this.columnModel = columnModel;
    }

    @Override
    public Object render(ImportRowModel model, String property,
        ColumnData config, int rowIndex, int colIndex,
        ListStore<ImportRowModel> store, Grid<ImportRowModel> grid) {

        
      //  config.style = columnModel.getBinding().styleCell()
        
        return null;
    }

}
