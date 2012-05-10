package org.activityinfo.client.report.editor.map.layerOptions;

import com.extjs.gxt.ui.client.widget.DatePicker;
import com.extjs.gxt.ui.client.widget.menu.Menu;

public class DateRangeMenu extends Menu {

	private DatePicker date1;
	private DatePicker date2;
	
	public DateRangeMenu() {
	    date1 = new DatePicker();
	    date2 = new DatePicker();
	    add(date1);
	    add(date2);
	    addStyleName("x-date-menu");
	    setAutoHeight(true);
	    plain = true;
	    showSeparator = false;
	    setEnableScrolling(false);
	}
}