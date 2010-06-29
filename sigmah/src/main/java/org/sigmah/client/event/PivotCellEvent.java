/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.event;

import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.EventType;
import org.sigmah.shared.report.content.PivotTableData;
import org.sigmah.shared.report.model.PivotElement;

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
