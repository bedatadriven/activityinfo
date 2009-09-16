package org.activityinfo.client.event;

import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.EventType;

import org.activityinfo.shared.report.content.PivotTableData;
import org.activityinfo.shared.report.model.PivotElement;

public class PivotCellEvent extends BaseEvent {

    private PivotElement element;
    private PivotTableData.Axis row;
    private PivotTableData.Axis column;

    public PivotCellEvent(EventType type, PivotElement element, PivotTableData.Axis row, PivotTableData.Axis column) {
        super(type);
        this.element = element;
        this.row = row;
        this.column = column;
    }

    public PivotElement getElement() {
        return element;
    }

    public PivotTableData.Axis getRow() {
        return row;
    }

    public PivotTableData.Axis getColumn() {
        return column;
    }
}
