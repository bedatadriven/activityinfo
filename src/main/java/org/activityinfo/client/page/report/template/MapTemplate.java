package org.activityinfo.client.page.report.template;

import org.activityinfo.client.dispatch.Dispatcher;
import org.activityinfo.client.i18n.I18N;
import org.activityinfo.client.report.editor.map.NewLayerWizard;
import org.activityinfo.client.report.editor.map.WizardCallback;
import org.activityinfo.client.report.editor.map.WizardDialog;
import org.activityinfo.shared.report.model.MapReportElement;
import org.activityinfo.shared.report.model.Report;
import org.activityinfo.shared.report.model.ReportElement;
import org.activityinfo.shared.report.model.layers.MapLayer;

import com.extjs.gxt.ui.client.GXT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class MapTemplate extends ReportElementTemplate {
	
	public MapTemplate(Dispatcher dispatcher) {
		super(dispatcher);
		setName(I18N.CONSTANTS.maps());
		setDescription(I18N.CONSTANTS.mapsDescription());
		setImagePath("map.png");
	}

	@Override
	public void createElement(final AsyncCallback<ReportElement> callback) {
		
		final NewLayerWizard wizard = new NewLayerWizard(dispatcher);
		WizardDialog dialog = new WizardDialog(wizard);
		dialog.setHeading(I18N.CONSTANTS.newMap());
		dialog.show(new WizardCallback() {

			@Override
			public void onFinished() {
				MapReportElement map = new MapReportElement();
				map.addLayer(wizard.createLayer());
				
				callback.onSuccess(map);
			}
		});
		
//		dialog.addValueChangeHandler(new ValueChangeHandler<MapLayer>() {
//			
//			@Override
//			public void onValueChange(ValueChangeEvent<MapLayer> event) {
//				createMap(callback, event.getValue());		
//			}
//		});
		
	}

	private void createMap(final AsyncCallback<ReportElement> callback, MapLayer layer) {
		
		
	}

}
