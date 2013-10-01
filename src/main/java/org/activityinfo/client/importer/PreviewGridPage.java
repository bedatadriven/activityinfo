package org.activityinfo.client.importer;

import java.util.List;

import org.activityinfo.client.importer.column.ColumnBindingFactory;
import org.activityinfo.client.importer.column.ColumnBindingForm;
import org.activityinfo.client.widget.wizard.WizardPage;

import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.GridEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.layout.HBoxLayout;
import com.extjs.gxt.ui.client.widget.layout.HBoxLayout.HBoxLayoutAlign;
import com.extjs.gxt.ui.client.widget.layout.HBoxLayoutData;
import com.google.common.collect.Lists;

public class PreviewGridPage extends WizardPage {

    private ImportModel model;
    private Grid<ImportRowModel> grid;
    private ColumnBindingForm bindingPopup;
    
    public PreviewGridPage(ImportModel model) {
        this.model = model;
        
        HBoxLayout layout = new HBoxLayout();
        layout.setHBoxLayoutAlign(HBoxLayoutAlign.STRETCH);
        
        setLayout(layout);
        
    }

    @Override
    public void beforeShow() {
        
        ImportData data = new ImportData(model.getText());
        model.setData(data); 
        
        List<ColumnConfig> columns = Lists.newArrayList();

        for(ImportColumnModel column : data.getColumns()) {
            ColumnConfig config = new ColumnConfig(column.getId(), column.getHeader(), 100);
          //  config.setRenderer(new ImportGridCellRenderer(column));
            columns.add(config);
        }
        ColumnModel columnModel = new ColumnModel(columns);
       
        
        if(grid == null) {
            createGrid(data, columnModel);
        } else {
            grid.reconfigure(data.getRowStore(), columnModel);
        }
        
    }

    protected void createGrid(ImportData data, ColumnModel columnModel) {
        grid = new Grid<ImportRowModel>(data.getRowStore(), columnModel);
        grid.addListener(Events.CellClick, new Listener<GridEvent<ImportRowModel>>() {

            @Override
            public void handleEvent(GridEvent<ImportRowModel> event) {
                columnClicked(event);
            }
        });
        
        HBoxLayoutData gridLayout = new HBoxLayoutData();
        gridLayout.setFlex(1);
        
        add(grid, gridLayout);
        
        bindingPopup = new ColumnBindingForm(model, ColumnBindingFactory.forActivity(model.getActivity()));
        add(bindingPopup);
    }


    private void columnClicked(GridEvent<ImportRowModel> event) {
        bindingPopup.showColumnConfig(event.getColIndex());
    }
}
