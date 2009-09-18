package org.activityinfo.shared.report.content;

import java.io.Serializable;
import java.util.List;

/**
 * @author Alex Bertram (akbertram@gmail.com)
 */
public class ReportContent implements Content {

    private String fileName;
    private List<FilterDescription> filterDescriptions;

    public ReportContent() {

    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public List<FilterDescription> getFilterDescriptions() {
        return filterDescriptions;
    }

    public void setFilterDescriptions(List<FilterDescription> filterDescriptions) {
        this.filterDescriptions = filterDescriptions;
    }
}
