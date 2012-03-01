package org.sigmah.client.page.report.template;

import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.i18n.I18N;
import org.sigmah.shared.report.model.MapReportElement;
import org.sigmah.shared.report.model.Report;

import com.google.gwt.user.client.rpc.AsyncCallback;

public class MapTemplate extends ReportTemplate {

	public MapTemplate(Dispatcher dispatcher) {
		super(dispatcher);
		setName(I18N.CONSTANTS.maps());
		setDescription(I18N.CONSTANTS.mapsDescription());
		setImagePath("map.png");
	}

	@Override
	public void create(AsyncCallback<Integer> callback) {
		Report report = new Report();
		report.addElement(new MapReportElement());
		
		save(report, callback);		
	}

}
