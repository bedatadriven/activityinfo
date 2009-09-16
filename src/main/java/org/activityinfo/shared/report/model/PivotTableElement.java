package org.activityinfo.shared.report.model;

import org.activityinfo.shared.report.content.PivotContent;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PivotTableElement extends PivotElement<PivotContent> {

	private List<Dimension> columnDimensions = new ArrayList<Dimension>();
	private List<Dimension> rowDimensions = new ArrayList<Dimension>();

	public PivotTableElement() {
		
	}

	public List<Dimension> getColumnDimensions() {
		return columnDimensions;
	}

	public void setColumnDimensions(List<Dimension> columnDimensions) {
		this.columnDimensions = columnDimensions;
	}
	
	public void addColDimension(Dimension dim) {
		this.columnDimensions.add(dim);
		
	}

	public List<Dimension> getRowDimensions() {
		return rowDimensions;
	}

	public void setRowDimensions(List<Dimension> rowDimensions) {
		this.rowDimensions = rowDimensions;
	}
	
	public void addRowDimension(Dimension dim) {
		this.rowDimensions.add(dim);
	}
	
	@Override
	public Set<Dimension> allDimensions() {
		Set<Dimension> set = new HashSet<Dimension>();
		set.addAll(columnDimensions);
		set.addAll(rowDimensions);
		
		return set;
	}
   
}
