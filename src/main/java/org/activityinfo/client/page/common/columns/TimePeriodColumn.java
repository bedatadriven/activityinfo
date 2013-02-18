package org.activityinfo.client.page.common.columns;

import java.util.Date;

import com.bedatadriven.rebar.time.calendar.LocalDate;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;
import com.google.gwt.i18n.client.DateTimeFormat;

public class TimePeriodColumn extends ReadTextColumn {

	protected static final DateTimeFormat FORMAT = DateTimeFormat.getFormat("yyyy-MMM-dd");

	public TimePeriodColumn(String property, String header, int width) {
		super(property, header, width);
		
	    setId(property);
	    setHeader(header);
	    setWidth(width);
	    setRowHeader(true);
	    setRenderer(new GridCellRenderer<ModelData>() {

			@Override
			public Object render(ModelData model, String property,
					ColumnData config, int rowIndex, int colIndex,
					ListStore<ModelData> store, Grid<ModelData> grid) {
				
				Object value = model.get("date1");
				if(value == null) {
					return "";
				}
				Date date1;
				if(value instanceof Date) {
					date1 = (Date)value;
				} else if(value instanceof LocalDate) {
					date1 = ((LocalDate)value).atMidnightInMyTimezone(); 
				} else {
					throw new RuntimeException("Don't know how to handle date as class " + value.getClass().getName());
				}
				
				Object value2 = model.get("date2");
				if(value2 == null) {
					return "";
				}
				Date date2;
				if(value2 instanceof Date) {
					date2 = (Date)value2;
				} else if(value2 instanceof LocalDate) {
					date2 = ((LocalDate)value2).atMidnightInMyTimezone(); 
				} else {
					throw new RuntimeException("Don't know how to handle date as class " + value2.getClass().getName());
				}
				
				return FORMAT.format(date1) + " to "+ FORMAT.format(date2);
			}
	    	
		});
	}
}