package org.activityinfo.client.page.map;

import org.activityinfo.shared.dto.Schema;
import org.activityinfo.shared.report.model.ReportElement;

public interface MapForm {

    public void setSchema(Schema schema);

    public ReportElement getMapElement();


    public boolean validate();
}
