package org.sigmah.client.page;

import org.sigmah.shared.report.model.ReportElement;

public interface PageElement {

    void bindReportElement(ReportElement element);
    ReportElement retriveReportElement();
}
