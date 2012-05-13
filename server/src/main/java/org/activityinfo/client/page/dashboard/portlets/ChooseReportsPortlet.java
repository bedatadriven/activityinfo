package org.activityinfo.client.page.dashboard.portlets;

import org.activityinfo.client.i18n.I18N;

import com.extjs.gxt.ui.client.widget.Label;
import com.extjs.gxt.ui.client.widget.Text;
import com.extjs.gxt.ui.client.widget.custom.Portlet;

public class ChooseReportsPortlet extends Portlet {

	public ChooseReportsPortlet() {
		
		setHeading(I18N.CONSTANTS.personalizeDashboard());
		Text label = new Text(I18N.CONSTANTS.emptyDashboard());
		label.setStyleAttribute("padding", "15px");
		label.setStyleAttribute("fontSize", "14px");
		add(label);
		
	}
	
}
