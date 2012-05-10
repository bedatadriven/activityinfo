package org.activityinfo.client.page.report;

import org.activityinfo.shared.report.model.ReportElement;

public interface HasReportElement<M extends ReportElement> {

	/**
	 * Binds the given model to this component. This component
	 * is expected to listen for events and update itself when its model is changed.
	 * 
	 */
	void bind(M model);
	
	/**
	 * 
	 * @return the currently bound model
	 */
	M getModel();
	
	/**
	 * Instructs the component to stop listening for events
	 * and disconnect all handlers
	 */
	void disconnect();
			
}
