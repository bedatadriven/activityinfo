package org.activityinfo.client.page.common.columns;

import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.grid.CellEditor;

public class EditTextColumn extends ReadTextColumn {

	public EditTextColumn(String property, String header, int width) {
		super(property, header, width);
		
        TextField<String> locationField = new TextField<String>();
        locationField.setAllowBlank(false);
        setEditor(new CellEditor(locationField));
	}
}