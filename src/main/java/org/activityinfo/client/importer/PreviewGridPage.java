package org.activityinfo.client.importer;

import java.util.List;

import org.activityinfo.client.widget.wizard.WizardPage;


import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.common.collect.Lists;

public class PreviewGridPage extends WizardPage {

    private ImportModel model;
    private Grid<ImportRowModel> grid;
    
    public PreviewGridPage(ImportModel model) {
        this.model = model;
        
        setLayout(new FitLayout());
        
    }

    @Override
    public void beforeShow() {
        
        ImportData data = new ImportData(model.getText());
        model.setData(data); 
        
        List<ColumnConfig> columns = Lists.newArrayList();

        for(ImportColumnModel column : data.getColumns()) {
            columns.add(new ColumnConfig(column.getId(), column.getHeader(), 50));
        }
        ColumnModel columnModel = new ColumnModel(columns);
        
        if(grid == null) {
            grid = new Grid<ImportRowModel>(data.getRowStore(), columnModel);
            add(grid);
        } else {
            grid.reconfigure(data.getRowStore(), columnModel);
        }
    }
    
}
