package org.activityinfo.client.page.config.form;

import org.activityinfo.client.Application;

import com.extjs.gxt.ui.client.widget.form.DateField;
import com.extjs.gxt.ui.client.widget.form.FormPanel;

public class DateRangeForm extends FormPanel {

	private DateField fromDate;
	private DateField toDate;
	
	public DateRangeForm() {

		fromDate = new DateField();
		fromDate.setFieldLabel(Application.CONSTANTS.fromDate());
		add(fromDate);
		
		toDate = new DateField();
		toDate.setFieldLabel(Application.CONSTANTS.toDate());
		add(toDate);
		
	}
	

}
