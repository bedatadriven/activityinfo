package org.activityinfo.client.importer;

import java.util.List;

import org.activityinfo.client.widget.wizard.WizardPage;

import com.extjs.gxt.ui.client.data.ModelData;

public abstract class ColumnBinderProvider<T extends ModelData> extends WizardPage {

    public abstract List<ColumnBinder> getBinders();
    
    
}
