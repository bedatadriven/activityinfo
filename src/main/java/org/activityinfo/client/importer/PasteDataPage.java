package org.activityinfo.client.importer;

import org.activityinfo.client.widget.wizard.WizardPage;

import com.extjs.gxt.ui.client.widget.form.TextArea;
import com.extjs.gxt.ui.client.widget.layout.VBoxLayout;
import com.extjs.gxt.ui.client.widget.layout.VBoxLayoutData;
import com.extjs.gxt.ui.client.widget.layout.VBoxLayout.VBoxLayoutAlign;
import com.google.gwt.user.client.ui.Label;

public class PasteDataPage extends WizardPage {
    
    private ImportModel model;
    private TextArea pasteArea;
    

    public PasteDataPage(ImportModel model) {
        
        this.model = model;
        
        VBoxLayout layout = new VBoxLayout();
        layout.setVBoxLayoutAlign(VBoxLayoutAlign.STRETCH);
        setLayout(layout);
        
        Label helpLabel = new Label();
        helpLabel.setText("Please copy and paste the data to import from Excel or another spreadsheet program.");
        
        add(helpLabel);
        
        pasteArea = new TextArea();
        VBoxLayoutData pasteAreaLayout = new VBoxLayoutData();
        pasteAreaLayout.setFlex(1);
        
        add(pasteArea, pasteAreaLayout);
    }


    @Override
    public void beforeHide() {
        model.setText(pasteArea.getValue());
    }
   
}
