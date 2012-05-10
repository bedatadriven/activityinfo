package org.activityinfo.client.page.report.template;


import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.layout.CardLayout;

public class WizardDialog extends Dialog {

	public WizardDialog() {
		setHeading("New report wizard");
		setWidth(550);
		setHeight(350);
		
		CardLayout cardLayout = new CardLayout();
		setLayout(cardLayout);
		
	}
	
	
}
