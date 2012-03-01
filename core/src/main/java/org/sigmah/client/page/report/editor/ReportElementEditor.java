package org.sigmah.client.page.report.editor;

import org.sigmah.client.page.report.HasReportElement;
import org.sigmah.shared.report.model.ReportElement;

import com.extjs.gxt.ui.client.widget.Component;

public interface ReportElementEditor<M extends ReportElement> extends HasReportElement<M> {

	
	Component getWidget();

	
}
