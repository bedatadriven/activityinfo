package org.sigmah.server.report.util;

public class HtmlTableCellTag extends HtmlTag {

	public HtmlTableCellTag() { 
		super("td");
	}
	
	public HtmlTableCellTag colSpan(int cols) {
		if(cols > 1 || hasAttribute("colspan")) {
			getAttribute("colspan").setValue(cols);
		}
		return this;
	}
	
	public HtmlTableCellTag rowSpan(int rows) { 
		if(rows > 1 || hasAttribute("rowspan")) {
			getAttribute("rowspan").setValue(rows);
		}
		return this;
	}
}
