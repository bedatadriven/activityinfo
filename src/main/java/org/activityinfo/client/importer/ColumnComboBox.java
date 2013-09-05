package org.activityinfo.client.importer;

import com.extjs.gxt.ui.client.widget.form.ComboBox;

public class ColumnComboBox extends ComboBox<ImportColumnModel> {
    
    public ColumnComboBox() {
        setSimpleTemplate("{header}<br>{examples}");
        setDisplayField("header");
        setValueField("id");
        setTriggerAction(TriggerAction.ALL);
        setForceSelection(true);
    }
    
}
