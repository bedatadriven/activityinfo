package org.sigmah.client.page.report.editor;

import java.util.List;

import org.sigmah.shared.report.model.Dimension;
import org.sigmah.shared.report.model.PivotChartReportElement;
import org.sigmah.shared.report.model.ReportElement;

import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.util.DelayedTask;

public class PivotEditor implements AbstractEditor {
		
	ReportElement element;
	
	public PivotEditor(ReportElement element) {
		
	}

	
	
	
	@Override
	public Object getWidget() {
		return null;
	}

	@Override
	public void bindReportElement(ReportElement element) {
	}

	@Override
	public ReportElement getReportElement() {
		return element;
	}

}
