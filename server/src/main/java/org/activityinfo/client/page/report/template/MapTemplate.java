package org.activityinfo.client.page.report.template;

import org.activityinfo.client.dispatch.Dispatcher;
import org.activityinfo.client.i18n.I18N;
import org.activityinfo.client.page.report.json.ReportSerializer;
import org.activityinfo.shared.report.model.MapReportElement;
import org.activityinfo.shared.report.model.Report;

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
