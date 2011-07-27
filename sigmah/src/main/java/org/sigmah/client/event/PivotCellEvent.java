/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.event;

import org.sigmah.shared.report.content.PivotTableData;
import org.sigmah.shared.report.model.PivotReportElement;

import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.EventType;

/**
 * Event fired when a pivot cell is double-clicked. Other listening components may 
 * show details, drill-downs, etc.
 */
public class PivotCellEvent extends BaseEvent {

    private PivotReportElement element;
    private PivotTableData.Axis row;
    private PivotTableData.Axis column;

    /**
     * 
     * @param type
     * @param element the enclosing {@link org.sigmah.shared.report.model.PivotTableReportElement} or 
     * {@link org.sigmah.shared.report.model.PivotChartReportElement}
     * @param row  the clicked row
     * @param column  the clicked column
     */
    public PivotCellEvent(EventType type, PivotReportElement element, PivotTableData.Axis row, PivotTableData.Axis column) {
        super(type);
        this.element = element;
        this.row = row;
        this.column = column;
    }

    public PivotReportElement getElement() {
        return element;
    }

    public PivotTableData.Axis getRow() {
        return row;
    }

    public PivotTableData.Axis getColumn() {
        return column;
    }
}
