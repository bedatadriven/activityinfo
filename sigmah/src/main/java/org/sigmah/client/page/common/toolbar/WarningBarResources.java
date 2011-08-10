package org.sigmah.client.page.common.toolbar;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;

public interface WarningBarResources extends ClientBundle {
	
	@Source("warningbar.css")
	public WarningStyle warningStyle();
	
	@Source("LockedPeriod.png")
	public ImageResource lockedPeriod();	
	
	public interface WarningStyle extends CssResource {}
	
	public static final WarningBarResources INSTANCE = GWT.create(WarningBarResources.class);
}
