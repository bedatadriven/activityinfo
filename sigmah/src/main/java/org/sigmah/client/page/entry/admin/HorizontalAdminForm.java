package org.sigmah.client.page.entry.admin;

import org.sigmah.shared.dto.AdminEntityDTO;

import com.extjs.gxt.ui.client.Style.Orientation;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.FormPanel.LabelAlign;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.extjs.gxt.ui.client.widget.layout.RowData;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;

public class HorizontalAdminForm extends LayoutContainer {

	public HorizontalAdminForm(AdminComboBoxSet set) {
    	setLayout(new RowLayout(Orientation.HORIZONTAL));
    	
    	for(AdminComboBox comboBox : set) {
        	LayoutContainer container = new LayoutContainer();
            FormLayout formLayout = new FormLayout();
            formLayout.setDefaultWidth(120);
            formLayout.setLabelAlign(LabelAlign.TOP);
            container.setLayout(formLayout);
    		container.add(comboBox);
    		add(container, new RowData(130, 50, new Margins(5,2,5,2)));
    	}
	}
}
