package org.activityinfo.client.page.config.link;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;

public interface IndicatorLinkResources extends ClientBundle {

	static IndicatorLinkResources INSTANCE = GWT.create(IndicatorLinkResources.class);
	
	@Source("IndicatorLink.css")
	Style style();
	
	interface Style extends CssResource {
		
		String highlight();

		String linkToggle();
		
	}
	
}
