package org.sigmah.client.page.map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.TextResource;

public interface MapResources extends ClientBundle {
	
	public static final MapResources INSTANCE = GWT.create(MapResources.class);
	@Source("LayerTemplate.html")
	public TextResource layerTemplate();
	
	@Source("MapStyle.css")
	public MapStyle layerStyle();
	
	@Source("GrabSprite.png")
	public ImageResource grabSprite();		
	
	@Source("Poi.png")
	public ImageResource poi();	
	
	@Source("CheckboxUnchecked.png")
	public ImageResource checkboxUnchecked();	
	
	@Source("CheckboxChecked.png")
	public ImageResource checkboxChecked();
	
	@Source("RemoveLayer.png")
	public ImageResource removeLayer();
	
	public interface MapStyle extends CssResource {}
}
