package org.activityinfo.client.report.editor.map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.CssResource.NotStrict;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.TextResource;

public interface MapResources extends ClientBundle {
	
	static final MapResources INSTANCE = GWT.create(MapResources.class);
	
	@Source("LayerTemplate.html")
	TextResource layerTemplate();
	
	@NotStrict
	@Source("MapStyle.css")
	MapStyle style();
	
	@NotStrict
	@Source("BaseMapDialog.css")
	BaseMapDialogStyle baseMapDialogStyle();
	
	@Source("GrabSprite.png")
	ImageResource grabSprite();
	
	@Source("Poi.png")
	ImageResource poi();	
	
	@Source("RemoveLayer.png")
	ImageResource removeLayer();	
	
	@Source("Icon.png")
	ImageResource icon();
	
	@Source("Piechart.png")
	ImageResource piechart();
	
	@Source("Bubble.png")
	ImageResource bubble();
	
	@Source("indicator.png")
	ImageResource singleSelect();
	
	@Source("indicators.png")
	ImageResource multiSelect();
	
	@Source("AddLayer.png")
	ImageResource addLayer();
	
	@Source("Error.png")
	ImageResource error();
	
    @Source("Layers.png")
	ImageResource layers();
    
    @Source("globe.png")
    ImageResource globe();

    @Source("Style.png")
    ImageResource styleIcon();
    
    @Source("Cluster.png")
    ImageResource clusterIcon();
	
	interface MapStyle extends CssResource {
	}
	
	interface BaseMapDialogStyle extends CssResource {
	}
	
}
