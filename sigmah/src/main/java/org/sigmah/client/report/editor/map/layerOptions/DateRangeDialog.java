package org.sigmah.client.report.editor.map.layerOptions;

import org.sigmah.client.i18n.I18N;
import org.sigmah.shared.report.model.DateRange;

import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.DateField;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.google.gwt.i18n.client.DateTimeFormat;

public class DateRangeDialog extends Dialog {
	
	private DateField date1;
	private DateField date2;
		
	private SelectionCallback<DateRange> callback;
	
	public DateRangeDialog() {
			
		FormLayout layout = new FormLayout();
		layout.setLabelWidth(50);
		layout.setDefaultWidth(100);
		
		setLayout(layout);
		setButtons(OKCANCEL);
		setHeading(I18N.CONSTANTS.customDateRange());
		setWidth(200);
		setHeight(150);
		setBodyStyle("padding: 5px");
		
		date1 = new DateField();
		date1.setFieldLabel(I18N.CONSTANTS.fromDate());
		date1.getPropertyEditor().setFormat(DateTimeFormat.getMediumDateFormat());
		
		date2 = new DateField();
		date2.setFieldLabel(I18N.CONSTANTS.toDate());
		date2.getPropertyEditor().setFormat(DateTimeFormat.getMediumDateFormat());
		
		add(date1);
		add(date2);
		
	}

	public void show(SelectionCallback<DateRange> callback) {
		this.callback = callback;
		show();
	}

	@Override
	protected void onButtonPressed(Button button) {
		if(button.getItemId().equals("ok")) {
			if(callback != null) {
				callback.onSelected(new DateRange(date1.getValue(), date2.getValue()));
			}
		}
		this.callback = null;
		hide();
	}
}
