package org.activityinfo.client.page.map;

import org.activityinfo.shared.dto.Schema;
import org.activityinfo.shared.report.model.ReportElement;

public interface MapForm {

    public ReportElement getMapElement();


    public boolean validate();
}
