package org.sigmah.client.page.map.layerOptions;

import java.util.ArrayList;
import java.util.List;

import org.sigmah.client.page.map.MapResources;
import org.sigmah.shared.report.model.layers.IconMapLayer;
import org.sigmah.shared.report.model.layers.MapLayer;

import com.extjs.gxt.ui.client.Style.Orientation;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.HorizontalPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.Radio;
import com.extjs.gxt.ui.client.widget.form.RadioGroup;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.Image;

/*
 * Displays a list of options the user can choose to configure an IconMapLayer
 */
public class IconMapLayerOptions extends LayoutContainer implements LayerOptionsWidget<IconMapLayer> {
	private IconMapLayer iconMapLayer;
	private RadioGroup radiogroupIcons =  new RadioGroup();
	private HorizontalPanel contentpanelIcons =  new HorizontalPanel();
    private static List<ImageResource> possibleIcons = new ArrayList<ImageResource>();

	static 	{
		possibleIcons.add(MapResources.INSTANCE.poi());
		possibleIcons.add(MapResources.INSTANCE.poi());
		possibleIcons.add(MapResources.INSTANCE.poi());
		possibleIcons.add(MapResources.INSTANCE.poi());
		possibleIcons.add(MapResources.INSTANCE.poi());
		possibleIcons.add(MapResources.INSTANCE.poi());
	}	

	public IconMapLayerOptions() {
		super();
		
		boolean isFirst=true;
		
		// Indent icons to the right. Is there a better way?
		HorizontalPanel marginPanel = new HorizontalPanel();
		marginPanel.setWidth(32);
		contentpanelIcons.add(marginPanel);
		
		// Default setting: no icons selected
		radiogroupIcons.setEnabled(false);
		
		for (ImageResource icon : possibleIcons)
		{
			ContentPanel iconPanel = new ContentPanel();
			iconPanel.setHeaderVisible(false);
			iconPanel.setLayout(new RowLayout(Orientation.VERTICAL));
			
			Radio radiobuttonIcon = new Radio();
			
			iconPanel.add(radiobuttonIcon);
			iconPanel.add(new Image(icon));
			
			radiogroupIcons.add(radiobuttonIcon);
			contentpanelIcons.add(iconPanel);
			
			if (isFirst)
			{
				radiobuttonIcon.setValue(true);
				isFirst=false;
			}
		}		
		
		add(contentpanelIcons);
	}
	@Override
	public IconMapLayer getValue() {
		return iconMapLayer;
	}
	@Override
	public void setValue(IconMapLayer value) {
		this.iconMapLayer=value;
	}
	@Override
	public void setValue(IconMapLayer value, boolean fireEvents) {
		setValue(value);
	}
	@Override
	public HandlerRegistration addValueChangeHandler(
			ValueChangeHandler<IconMapLayer> handler) {
		return this.addHandler(handler, ValueChangeEvent.getType());
	}
	
	
}
