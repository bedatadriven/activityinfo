package org.sigmah.client.page.map.mapOptions;

import com.extjs.gxt.ui.client.widget.DatePicker;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.VerticalPanel;

public class FilterOptionsWidget extends LayoutContainer {
	private DatePicker datepickerFromDate;
	private DatePicker datepickerToDate;
	private VerticalPanel panelDatePickers = new VerticalPanel();
	
	public FilterOptionsWidget() {
		super();
		
		initializeComponent();
		addDatePickers();
	}
	private void addDatePickers() {
		datepickerFromDate = new DatePicker();
		datepickerToDate = new DatePicker();
		panelDatePickers.add(datepickerFromDate);
		panelDatePickers.add(datepickerToDate);
	}
	
	private void initializeComponent() {
		panelDatePickers.setAutoWidth(true);
		add(panelDatePickers);
	}
}