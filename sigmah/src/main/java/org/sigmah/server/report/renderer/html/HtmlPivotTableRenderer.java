package org.sigmah.server.report.renderer.html;

import org.sigmah.server.report.util.HtmlWriter;
import org.sigmah.shared.report.content.PivotTableData;
import org.sigmah.shared.report.model.PivotTableElement;

import java.text.NumberFormat;
import java.util.List;

public class HtmlPivotTableRenderer implements HtmlRenderer<PivotTableElement> {


    @Override
	public void render(HtmlWriter html, ImageStorageProvider isp, PivotTableElement element) {


		html.startDiv().styleName(CssStyles.ELEMENT_CONTAINER);
		
		/*
		 * Generate the title
		 */
		
		if(element.getTitle() != null) {
			html.header(2, element.getTitle());
		}
		
		/*
		 * Generate descriptions of the filter for dimensions that are
		 * filtered, but are not row/col headers
		 */
		
	    HtmlReportUtil.generateFilterDescriptionHtml(html, element.getContent().getFilterDescriptions());
		
		/*
		 * Generate the pivot table
		 */

        PivotTableData data = element.getContent().getData();
		if(data.isEmpty()) {
			html.div("Aucune données").styleName(CssStyles.NO_DATA);
		} else {
	
			html.startTable().styleName(CssStyles.PIVOT_TABLE);
			html.startTableHeader();
			
			// first write the column headers
			
			int colDepth = data.getRootColumn().getDepth();
			
			for(int depth = 1; depth<=colDepth; ++depth) {
				
				html.startTableRow();
			
				if(depth == 1) {
					
					html.emptyTableCell()
							.rowSpan(colDepth);

				}
				
				List<PivotTableData.Axis> columns = data.getRootColumn().getDescendantsAtDepth(depth);
				for(PivotTableData.Axis column : columns) {
					
					html.tableCell(column.getLabel())
							.colSpan(Math.max(1, column.getChildCount()));
	
				}
				
				html.endTableRow();
			}
	
			html.endTableHeader();
			
			// write body
			
			html.startTableBody();
			
			for(PivotTableData.Axis row : data.getRootRow().getChildren()) {
				writeRow(html, row, data.getRootColumn().getLeaves(), 0);
			}
			
			html.endTableBody();
			
			// 
			html.endTable();
		}
		html.endDiv();
	}
	
	protected void writeRow(HtmlWriter html, PivotTableData.Axis row, List<PivotTableData.Axis> leafColumns, int depth) {
		
		html.startTableRow()
			.styleNameIf(CssStyles.ROW_GROUP, row.getChildCount() > 0);
		
		html.tableCell(row.getLabel())
			.styleName(CssStyles.INDENT(depth));

        NumberFormat format = NumberFormat.getIntegerInstance();
        format.setGroupingUsed(true);


		for(PivotTableData.Axis column : leafColumns) {
			
			PivotTableData.Cell cell = row.getCell(column);
			if(cell == null) {
				html.emptyTableCell().styleName(CssStyles.EMPTY_VALUE_CELL);
			} else {
			    html.tableCell(format.format(cell.getValue())).styleName(CssStyles.VALUE_CELL);
			}
		}
		html.endTableRow();
		
		for(PivotTableData.Axis childRow : row.getChildren()) {
			writeRow(html, childRow, leafColumns, depth+1);
		}
	}
}
