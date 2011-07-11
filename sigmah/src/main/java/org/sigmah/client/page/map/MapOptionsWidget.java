package org.sigmah.client.page.map;

import com.extjs.gxt.ui.client.widget.Layout;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.form.Radio;
import com.extjs.gxt.ui.client.widget.form.RadioGroup;

/*
 * Displays a list of options and hints for the map
 */
public class MapOptionsWidget extends LayoutContainer {
	private RadioGroup radiogroupBaseMap = new RadioGroup();
	private Radio radioOsm = new Radio();
	private Radio radioGoogleMaps = new Radio();
	
	public MapOptionsWidget() {
		createBaseMapOptions();
		createFixedZoomHint();
		createFixedPanningHint();
	}

	private void createFixedPanningHint() {
		// TODO Auto-generated method stub
		
	}

	private void createFixedZoomHint() {
		// TODO Auto-generated method stub
		
	}

	private void createBaseMapOptions() {
		// TODO Auto-generated method stub
		
	}
	
	
	
}
