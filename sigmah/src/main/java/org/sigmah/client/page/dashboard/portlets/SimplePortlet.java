package org.sigmah.client.page.dashboard.portlets;

import com.extjs.gxt.ui.client.widget.custom.Portlet;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;

public class SimplePortlet extends Portlet {

	public SimplePortlet() {
		super();

		setLayout(new FitLayout());
		addText("woeiwoeiwoeiwoeiwoeiwoei");
	}

	
}
