package org.activityinfo.client.offline.capability;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.TextResource;

public interface ProfileResources extends ClientBundle {

	public static ProfileResources INSTANCE = GWT.create(ProfileResources.class);
	
	@Source("StartupMessage.html")
	TextResource startupMessage();
	
	@Source("StartupMessageIE.html")
	TextResource startupMessageIE();

	@Source("StartupMessageIE9.html")
	TextResource startupMessageIE9();
	
	@Source("StartupMessageFirefox.html")
	TextResource startupMessageFirefox();
	
	@Source("StartupMessageFirefox36.html")
	TextResource startupMessageFirefox36();

	@Source("Startup.css")
	StartupStyle style();
	
	interface StartupStyle extends CssResource {
		String startupDialogBody();
	}
	
}
