package org.activityinfo.client.local.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;

public interface SyncStatusResources extends ClientBundle {

	public static SyncStatusResources INSTANCE = GWT.create(SyncStatusResources.class);
	
	@Source("SyncStatus.css")
	Style style();
	
	public interface Style extends CssResource {
		String warningIcon();
	}
}
