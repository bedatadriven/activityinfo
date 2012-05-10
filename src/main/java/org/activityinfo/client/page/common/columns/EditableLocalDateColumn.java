package org.activityinfo.client.page.common.columns;

import java.util.Date;

import com.bedatadriven.rebar.time.calendar.LocalDate;
import com.extjs.gxt.ui.client.widget.form.DateField;
import com.extjs.gxt.ui.client.widget.grid.CellEditor;
import com.google.gwt.i18n.client.DateTimeFormat;

public class EditableLocalDateColumn extends LocalDateColumn {

	public EditableLocalDateColumn(String property, String header, int width) {
		super(property, header, width);
		
		DateField datefieldStartDate = new DateField();
	    datefieldStartDate.getPropertyEditor().setFormat(FORMAT); 
	    setEditor(new CellEditor(datefieldStartDate) {

			@Override
			public Object preProcessValue(Object value) {
				if(value == null) {
					return null;
				} else {
					return ((LocalDate)value).atMidnightInMyTimezone();
				}
			}
	    	
			@Override
			public Object postProcessValue(Object value) {
				if(value == null) {
					return null;
				} else {
					return new LocalDate((Date)value);
				}
			}
		});
	}
}