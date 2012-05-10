/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.shared.report.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlTransient;

import org.activityinfo.shared.report.content.TableContent;


public class TableElement extends ReportElement<TableContent> implements Serializable {

    private TableColumn rootColumn = new TableColumn();
    private List<TableColumn> sortBy = new ArrayList<TableColumn>();

    private int frozenColumns = 0;
    private MapReportElement map;

    @XmlElement(name="columns")
	public TableColumn getRootColumn() {
		return rootColumn;
	}

    public void setRootColumn(TableColumn rootColumn) {
        this.rootColumn = rootColumn;
    }

    @XmlElement(name="column")
    @XmlElementWrapper(name="sortBy")
	public List<TableColumn> getSortBy() {
		return sortBy;
	}

    /**
	 * 
	 * @return The number of left-most columns that should be frozen 
	 * when the table is presented as a spreadsheet
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
