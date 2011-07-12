package org.sigmah.client.page.map;

import java.util.ArrayList;
import java.util.List;

import org.sigmah.shared.map.BaseMap;

import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.FieldEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.Layout;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.form.FieldSet;
import com.extjs.gxt.ui.client.widget.form.Radio;
import com.extjs.gxt.ui.client.widget.form.RadioGroup;

/*
 * Displays a list of options and hints for the map
 */
public class MapOptionsWidget extends LayoutContainer {
	private FieldSet fieldsetBaseMaps = new FieldSet();
	private RadioGroup radiogroupBaseMap = new RadioGroup();
	private Radio radioOsm = new Radio();
	private Radio radioGoogleMaps = new Radio();
	
	public MapOptionsWidget() {
		createBaseMapOptions(new ArrayList<BaseMap>());
		createFixedZoomHint();
		createFixedPanningHint();
	}

	private void createFixedPanningHint() {
		// TODO Auto-generated method stub
		
	}

	private void createFixedZoomHint() {
		// TODO Auto-generated method stub
		
	}

	/*
	 * Adds a radiobutton for every found basemap
	 */
	private void createBaseMapOptions(List<BaseMap> basemaps) {
		for (BaseMap baseMap : basemaps) {
			Radio radioBaseMap = new Radio();
			radioBaseMap.setTitle(baseMap.getName());
			radiogroupBaseMap.add(radioBaseMap);
		}
		
		radiogroupBaseMap.addListener(Events.Change, new Listener<FieldEvent>() {
			@Override
			public void handleEvent(FieldEvent be) {
				// Update the map to display the correct basemap
		}});
	}
	
	
	
}
