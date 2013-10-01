package org.activityinfo.client.importer.column;

import java.util.List;
import java.util.Map;

import org.activityinfo.client.importer.ImportColumnModel;
import org.activityinfo.client.importer.ImportModel;

import com.extjs.gxt.ui.client.Style.Orientation;
import com.extjs.gxt.ui.client.Style.Scroll;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.FieldEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.form.Radio;
import com.extjs.gxt.ui.client.widget.form.RadioGroup;
import com.extjs.gxt.ui.client.widget.layout.FlowLayout;
import com.google.common.collect.Maps;

public class ColumnBindingForm extends ContentPanel {

    private List<ColumnBinding> bindings;
    private ImportModel model;
    private Map<ColumnBinding, Radio> radioMap = Maps.newHashMap();
    private RadioGroup radioGroup;
    private ImportColumnModel selectedColumn;
    
    
    public ColumnBindingForm(ImportModel model, List<ColumnBinding> bindings) {
        super();
        this.model = model;
        this.bindings = bindings;
        
        radioGroup = new RadioGroup();
        radioGroup.setOrientation(Orientation.VERTICAL);
        radioGroup.addListener(Events.Change, new Listener<FieldEvent>() {

            @Override
            public void handleEvent(FieldEvent be) {
                Radio selectedRadio = radioGroup.getValue();
                ColumnBinding binding = selectedRadio.getData("binding");
                onBindingChanged(binding);
            }
            
        });
        
        for(ColumnBinding binding : bindings) {
            Radio radioButton = new Radio();
            radioButton.setBoxLabel(binding.getLabel());
            radioButton.setData("binding", binding);
            radioGroup.add(radioButton);
            radioMap.put(binding, radioButton);
        }

        setScrollMode(Scroll.AUTOY);
        setLayout(new FlowLayout());
        setHeading("What does this column contain?");
        setWidth(350);
        add(radioGroup);
    }

    private void onBindingChanged(ColumnBinding binding) {
        selectedColumn.setBinding(binding);
        model.fireEvent(ImportModel.COLUMNS_CHANGED, new BindingChangedEvent(selectedColumn));
    }

    public void showColumnConfig(int columnIndex) {
        selectedColumn = model.getData().getColumns().get(columnIndex);
        
        setHeading(selectedColumn.getHeader());
        
        radioGroup.setValue( radioMap.get(selectedColumn.getBinding()) );
    }
}
