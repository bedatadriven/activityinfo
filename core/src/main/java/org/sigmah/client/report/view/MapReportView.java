package org.sigmah.client.report.view;

import org.sigmah.client.map.GoogleMapsReportOverlays;
import org.sigmah.client.widget.GoogleMapsPanel;
import org.sigmah.shared.report.model.MapReportElement;

import com.extjs.gxt.ui.client.widget.Component;

public class MapReportView extends GoogleMapsPanel implements ReportView<MapReportElement> {

	private GoogleMapsReportOverlays overlays;
	private MapReportElement element; 
	
	public MapReportView() {
		setHeaderVisible(false);
	}
	
	@Override
	protected void onMapInitialized() {
		overlays = new GoogleMapsReportOverlays(getMapWidget());
		if(element != null) {
			addContent();
		}
	}

	@Override
	public void show(MapReportElement element) {
		this.element = element;
		if(isMapLoaded()) {
			addContent();
		}
	}

	private void addContent() {
		overlays.syncWith(element);
	}

	@Override
	public Component asComponent() {
		return this;
	}

}
