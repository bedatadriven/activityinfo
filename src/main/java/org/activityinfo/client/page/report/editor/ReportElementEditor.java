package org.activityinfo.client.page.report.editor;

import java.util.List;

import org.activityinfo.client.page.report.HasReportElement;
import org.activityinfo.shared.command.RenderElement;
import org.activityinfo.shared.report.model.ReportElement;

import com.extjs.gxt.ui.client.widget.Component;

public interface ReportElementEditor<M extends ReportElement> extends HasReportElement<M> {

	
	Component getWidget(); 

	List<RenderElement.Format> getExportFormats();
	
}
