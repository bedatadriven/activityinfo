package org.sigmah.client.page.common.columns;

import com.extjs.gxt.ui.client.widget.form.DateField;
import com.extjs.gxt.ui.client.widget.grid.CellEditor;
import com.google.gwt.i18n.client.DateTimeFormat;

public class EditDateColumn extends ReadDateColumn {

	public EditDateColumn(String property, String header, int width) {
		super(property, header, width);
		
		DateField datefieldStartDate = new DateField();
	    datefieldStartDate.getPropertyEditor().setFormat(DateTimeFormat.getFormat("dd/MM/yyyy")); 
	    
	    setEditor(new CellEditor(datefieldStartDate));
	}
}