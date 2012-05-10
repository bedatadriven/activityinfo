package org.activityinfo.client.report.view;

import org.activityinfo.client.map.GoogleMapsReportOverlays;
import org.activityinfo.client.widget.GoogleMapsPanel;
import org.activityinfo.shared.report.model.MapReportElement;

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
