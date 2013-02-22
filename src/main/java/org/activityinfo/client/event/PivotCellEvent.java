package org.activityinfo.client.event;

/*
 * #%L
 * ActivityInfo Server
 * %%
 * Copyright (C) 2009 - 2013 UNICEF
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import org.activityinfo.shared.report.content.PivotTableData;
import org.activityinfo.shared.report.model.PivotReportElement;

import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.EventType;

/**
 * Event fired when a pivot cell is double-clicked. Other listening components
 * may show details, drill-downs, etc.
 */
public class PivotCellEvent extends BaseEvent {

    private PivotReportElement element;
    private PivotTableData.Axis row;
    private PivotTableData.Axis column;

    /**
     * 
     * @param type
     * @param element
     *            the enclosing
     *            {@link org.activityinfo.shared.report.model.PivotTableReportElement}
     *            or
     *            {@link org.activityinfo.shared.report.model.PivotChartReportElement}
     * @param row
     *            the clicked row
     * @param column
     *            the clicked column
     */
    public PivotCellEvent(EventType type, PivotReportElement element,
        PivotTableData.Axis row, PivotTableData.Axis column) {
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
