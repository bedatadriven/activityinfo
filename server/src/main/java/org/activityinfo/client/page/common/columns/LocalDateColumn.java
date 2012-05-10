package org.activityinfo.client.page.common.columns;

import java.util.Date;

import com.bedatadriven.rebar.time.calendar.LocalDate;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;
import com.google.gwt.i18n.client.DateTimeFormat;

public class LocalDateColumn extends ReadTextColumn {

	protected static final DateTimeFormat FORMAT = DateTimeFormat.getFormat("yyyy-MMM-dd");

	public LocalDateColumn(String property, String header, int width) {
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
				
				Object value = model.get(property);
				if(value == null) {
					return "";
				}
				Date date;
				if(value instanceof Date) {
					date = (Date)value;
				} else if(value instanceof LocalDate) {
					date = ((LocalDate)value).atMidnightInMyTimezone(); 
				} else {
					throw new RuntimeException("Don't know how to handle date as class " + value.getClass().getName());
				}
				return FORMAT.format(date);
			}
	    	
		});
	}
}