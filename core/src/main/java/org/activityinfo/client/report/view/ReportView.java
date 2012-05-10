package org.activityinfo.client.report.view;

import org.activityinfo.shared.report.model.ReportElement;

import com.extjs.gxt.ui.client.widget.Component;

public interface ReportView <T extends ReportElement> {

	void show(T element);
	
	Component asComponent();
	
}
