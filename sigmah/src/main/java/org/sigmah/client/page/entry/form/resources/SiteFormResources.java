package org.sigmah.client.page.entry.form.resources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.CssResource.NotStrict;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.TextResource;

public interface SiteFormResources extends ClientBundle {
	
	static final SiteFormResources INSTANCE = GWT.create(SiteFormResources.class);
	
	@Source("LocationTemplate.html")
	TextResource locationTemplate();
	
	@Source("FormNavigationTemplate.html")
	TextResource formNavigationTemplate();
	
	@Source("FormStyle.css")
	@NotStrict
	FormStyle style();
	
	@Source("marker.png")
	ImageResource marker();
	
	interface FormStyle extends CssResource {
		
		String adminClearSpan();
		String addLocationButton();
		String locationDialogHeader();
		String locationDialogHelp();
		String locationDialogPane();
		String locationSearchResults();
	
	}
}
