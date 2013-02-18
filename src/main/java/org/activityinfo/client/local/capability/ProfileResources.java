package org.activityinfo.client.local.capability;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.TextResource;

public interface ProfileResources extends ClientBundle {

	public static ProfileResources INSTANCE = GWT.create(ProfileResources.class);
	
	
	@Source("StartupMessageIE.html")
	TextResource startupMessageIE();

	@Source("StartupMessageIE9.html")
	TextResource startupMessageIE9();
	
	@Source("StartupMessageFirefox.html")
	TextResource startupMessageFirefox();
	
	@Source("Startup.css")
	StartupStyle style();
	
	interface StartupStyle extends CssResource {
		String startupDialogBody();
	}
	
}
