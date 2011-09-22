package org.sigmah.client.page.entry.editor;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.TextResource;

public interface LocationResources extends ClientBundle {
	
	public static final LocationResources INSTANCE = GWT.create(LocationResources.class);
	
	@Source("LocationTemplate.html")
	public TextResource locationTemplate();
	
	@Source("LocationStyle.css")
	public LocationStyle locationStyle();
	
	@Source("marker.png")
	public ImageResource marker();
	
	public interface LocationStyle extends CssResource {}
}
