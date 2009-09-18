package org.activityinfo.shared.report.content;

import java.io.Serializable;
import java.util.List;

/**
 * @author Alex Bertram (akbertram@gmail.com)
 */
public class TableContent implements Content {

    private List<FilterDescription> filterDescriptions;
    private TableData data;
    private List<Marker> markers;

    public TableContent() {
    }

    public List<FilterDescription> getFilterDescriptions() {
        return filterDescriptions;
    }

    public void setFilterDescriptions(List<FilterDescription> filterDescriptions) {
        this.filterDescriptions = filterDescriptions;
    }

    public TableData getData() {
        return data;
    }

    public void setData(TableData data) {
        this.data = data;
    }

    public List<Marker> getMarkers() {
        return markers;
    }

    public void setMarkers(List<Marker> markers) {
        this.markers = markers;
    }
}
