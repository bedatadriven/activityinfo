package org.sigmah.client.page.map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.CssResource.NotStrict;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.TextResource;

public interface MapResources extends ClientBundle {
	
	public static final MapResources INSTANCE = GWT.create(MapResources.class);
	
	@Source("LayerTemplate.html")
	public TextResource layerTemplate();
	
	@NotStrict
	@Source("MapStyle.css")
	public MapStyle style();
	
	@NotStrict
	@Source("BaseMapDialog.css")
	public BaseMapDialogStyle baseMapDialogStyle();
	
	@Source("GrabSprite.png")
	public ImageResource grabSprite();		
	
	@Source("Poi.png")
	public ImageResource poi();	
	
	@Source("RemoveLayer.png")
	public ImageResource removeLayer();	
	
	@Source("Icon.png")
	public ImageResource icon();
	
	@Source("Piechart.png")
	public ImageResource piechart();
	
	@Source("Bubble.png")
	public ImageResource bubble();
	
	@Source("SingleSelect.png")
	public ImageResource singleSelect();
	
	@Source("MultiSelect.png")
	public ImageResource multiSelect();
	
	@Source("AddLayer.png")
	public ImageResource addLayer();
	
	@Source("Error.png")
	public ImageResource error();
	
    @Source("Layers.png")
	public ImageResource layers();
    
    @Source("globe.png")
    public ImageResource globe();

    @Source("Style.png")
    public ImageResource styleIcon();
    
    @Source("Cluster.png")
    public ImageResource clusterIcon();
	
	public interface MapStyle extends CssResource {
	}
	
	public interface BaseMapDialogStyle extends CssResource {
	}
	
}
