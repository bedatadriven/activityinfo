package org.sigmah.client.page.dashboard.portlets;

import com.extjs.gxt.ui.client.widget.Html;
import com.extjs.gxt.ui.client.widget.custom.Portlet;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.TextResource;

public class GoogleEarthPortlet extends Portlet {
	public interface Templates extends ClientBundle {

		@Source("GoogleEarth.html")
		TextResource googleEarthText();

	}

	public static final Templates TEMPLATES = GWT.create(Templates.class);

	public GoogleEarthPortlet() {
		setHeading("Google Earth");
		setLayout(new FitLayout());
		add(new Html(TEMPLATES.googleEarthText().getText()));
		
	}
	
}
