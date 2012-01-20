package org.sigmah.client.page.report.editor;

import org.sigmah.client.EventBus;
import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.page.map.AbstractMap;
import org.sigmah.shared.report.model.MapReportElement;
import org.sigmah.shared.report.model.ReportElement;

import com.google.inject.Inject;

public class MapEditor extends AbstractMap implements AbstractEditor {

	@Inject
	public MapEditor(Dispatcher dispatcher, EventBus eventBus) {
		super(dispatcher, eventBus);		
	}
	
	public void bindElement(final MapReportElement element) {
		aiMapWidget.setValue(element);
		layersWidget.setValue(element);
	}

	@Override
	public ReportElement getReportElement() {
		return aiMapWidget.getValue();
	}

	@Override
	public Object getWidget() {
		return this;
	}

	@Override
	public void bindReportElement(ReportElement element) {
		bindElement((MapReportElement)element);
	}
}
