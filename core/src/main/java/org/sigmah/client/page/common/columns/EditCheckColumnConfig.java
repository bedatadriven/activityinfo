package org.sigmah.client.page.common.columns;

import com.extjs.gxt.ui.client.widget.form.CheckBox;
import com.extjs.gxt.ui.client.widget.grid.CellEditor;
import com.extjs.gxt.ui.client.widget.grid.CheckColumnConfig;

public class EditCheckColumnConfig extends CheckColumnConfig {

	public EditCheckColumnConfig(String id, String name, int width) {
		super(id, name, width);

	    setEditor(new CellEditor(new CheckBox()));
	}
}