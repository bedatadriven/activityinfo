package org.sigmah.client.page.map.layerOptions;

import java.util.HashMap;
import java.util.Map;

import org.sigmah.shared.report.model.MapIcon;
import org.sigmah.shared.report.model.MapIcon.Icon;
import org.sigmah.shared.report.model.layers.IconMapLayer;

import com.extjs.gxt.ui.client.Style.Orientation;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.FieldEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.HorizontalPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.form.Radio;
import com.extjs.gxt.ui.client.widget.form.RadioGroup;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Image;

/*
 * Displays a list of options the user can choose to configure an IconMapLayer
 */
public class IconLayerOptions extends LayoutContainer implements LayerOptionsWidget<IconMapLayer> {
	private IconMapLayer iconMapLayer;
	private RadioGroup radiogroupIcons =  new RadioGroup();
	private HorizontalPanel contentpanelIcons =  new HorizontalPanel();
	// Are Bidimaps from commons collections supported GWT clientside?
	private Map<Radio, Icon> radioIcons = new HashMap<Radio, Icon>();
	private Map<Icon, Radio> iconsRadio = new HashMap<Icon, Radio>();
	
	public IconLayerOptions() {
		super();
		
		initializeComponent();

		populateWithIcons();

		radiogroupIcons.addListener(Events.Change, new Listener<FieldEvent>() {
			@Override
			public void handleEvent(FieldEvent be) {
				iconMapLayer.setIcon(radioIcons.get(radiogroupIcons.getValue()).name());
				ValueChangeEvent.fire(IconLayerOptions.this, iconMapLayer);
			}
		});
	}

	private void initializeComponent() {
		contentpanelIcons.setAutoWidth(true);
		add(contentpanelIcons);
	}

	private void populateWithIcons() {
		boolean isFirst = true;
		for (Icon mapIcon : Icon.values())
		{
			ContentPanel iconPanel = new ContentPanel();
			iconPanel.setHeaderVisible(false);
			iconPanel.setLayout(new RowLayout(Orientation.VERTICAL));
			iconPanel.setAutoWidth(true);
			
			Radio radiobuttonIcon = new Radio();
			
			iconPanel.add(radiobuttonIcon);
			iconPanel.add(new Image(MapIcon.fromEnum(mapIcon)));
			
			radioIcons.put(radiobuttonIcon, mapIcon);
			iconsRadio.put(mapIcon, radiobuttonIcon);
			radiogroupIcons.add(radiobuttonIcon);
			contentpanelIcons.add(iconPanel);
			
			if (isFirst) {
				radiobuttonIcon.setValue(true);
				isFirst=false;
			}
		}
	}
	
	@Override
	public IconMapLayer getValue() {
		return iconMapLayer;
	}
	@Override
	public void setValue(IconMapLayer value) {
		this.iconMapLayer=value;
		updateUI();
	}
	private void updateUI() {
		iconsRadio.get(Icon.valueOf(iconMapLayer.getIcon())).setValue(true);
	}

	@Override
	public void setValue(IconMapLayer value, boolean fireEvents) {
		setValue(value);
	}
	@Override
	public HandlerRegistration addValueChangeHandler(ValueChangeHandler<IconMapLayer> handler) {
		return this.addHandler(handler, ValueChangeEvent.getType());
	}
	
}
