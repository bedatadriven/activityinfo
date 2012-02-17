package org.sigmah.client.page.report;

import org.sigmah.client.EventBus;

import com.extjs.gxt.ui.client.event.Listener;

public class ReportEventHelper {

	private EventBus eventBus;
	private HasReportElement source;
	private Listener<ReportChangeEvent> innerListener;

	public ReportEventHelper(EventBus eventBus, HasReportElement source) {
		this.eventBus = eventBus;
		this.source = source;
	}
	
	public void fireChange() {
		eventBus.fireEvent(new ReportChangeEvent(source, source.getModel()));
	}
	
	public void listen(final ReportChangeHandler handler) {
		innerListener = new Listener<ReportChangeEvent>() {

			@Override
			public void handleEvent(ReportChangeEvent be) {
				if(be.getModel() == source.getModel() && be.getSource() != source) {
					handler.onChanged();
				}
			}
		};
		eventBus.addListener(ReportChangeEvent.TYPE, innerListener);
	}
	
	public void disconnect() {
		if(innerListener != null) {
			eventBus.removeListener(ReportChangeEvent.TYPE, innerListener);
		}
	}
}
