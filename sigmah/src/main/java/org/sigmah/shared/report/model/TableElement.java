package org.sigmah.shared.report.model;

import org.sigmah.shared.report.content.TableContent;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import java.util.ArrayList;
import java.util.List;


public class TableElement extends ReportElement<TableContent> {

    private TableColumn rootColumn = new TableColumn();
    private List<TableColumn> sortBy = new ArrayList<TableColumn>();

    private int frozenColumns = 0;
    private MapElement map;

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
    public MapElement getMap() {
        return map;
    }

    public void setMap(MapElement map) {
        this.map = map;
    }

    public void addColumn(TableColumn tableColumn) {
        getRootColumn().addChild(tableColumn);
    }
}
