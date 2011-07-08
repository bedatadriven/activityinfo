package org.sigmah.shared.report.model.layers;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

/*
 * Icons used to visualize POI's on an IconMapLayer
 */
public interface MapIcons extends ClientBundle {
	
	public static final MapIcons INSTANCE = GWT.create(MapIcons.class);
	
	@Source("Water.png")
	public ImageResource water();
	
	@Source("Fire.png")
	public ImageResource fire();		
	
	@Source("Doctor.png")
	public ImageResource doctor();		
	
	@Source("Food.png")
	public ImageResource food();		
}
