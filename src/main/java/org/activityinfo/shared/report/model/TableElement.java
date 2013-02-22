package org.activityinfo.shared.report.model;

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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlTransient;

import org.activityinfo.shared.report.content.TableContent;

public class TableElement extends ReportElement<TableContent> implements
    Serializable {

    private TableColumn rootColumn = new TableColumn();
    private List<TableColumn> sortBy = new ArrayList<TableColumn>();

    private int frozenColumns = 0;
    private MapReportElement map;

    @XmlElement(name = "columns")
    public TableColumn getRootColumn() {
        return rootColumn;
    }

    public void setRootColumn(TableColumn rootColumn) {
        this.rootColumn = rootColumn;
    }

    @XmlElement(name = "column")
    @XmlElementWrapper(name = "sortBy")
    public List<TableColumn> getSortBy() {
        return sortBy;
    }

    /**
     * 
     * @return The number of left-most columns that should be frozen when the
     *         table is presented as a spreadsheet
     */
    @XmlElement(defaultValue = "0")
    public int getFrozenColumns() {
        return frozenColumns;
    }

    public void setFrozenColumns(int frozenColumns) {
        this.frozenColumns = frozenColumns;
    }

    @XmlElement
    public MapReportElement getMap() {
        return map;
    }

    public void setMap(MapReportElement map) {
        this.map = map;
    }

    public void addColumn(TableColumn tableColumn) {
        getRootColumn().addChild(tableColumn);
    }

    @Override
    @XmlTransient
    public Set<Integer> getIndicators() {
        return Collections.emptySet();
    }
}
