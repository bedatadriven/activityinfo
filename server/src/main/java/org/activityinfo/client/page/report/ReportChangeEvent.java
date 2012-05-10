package org.activityinfo.client.page.report;

import org.activityinfo.shared.report.model.ReportElement;

import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.EventType;

public class ReportChangeEvent extends BaseEvent {

	public static final EventType TYPE = new EventType();
	
	private final ReportElement model;
	
	public ReportChangeEvent(Object source, ReportElement model) {
		super(TYPE);
		this.setSource(source);
		this.model = model;
	}
	
	@Override
	public String toString() {
		return "ReportChangeEvent{ source= " + getSource().getClass() + ", model = " + model + "}";
	}

	public ReportElement getModel() {
		return model;
	}
	

}
