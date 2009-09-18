package org.activityinfo.server.report.renderer.excel;

import java.util.List;
import java.util.Map.Entry;

import org.activityinfo.shared.report.content.FilterDescription;
import org.activityinfo.shared.report.content.PivotTableData;
import org.activityinfo.shared.report.model.PivotTableElement;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;

public class ExcelPivotTableRenderer implements ExcelRenderer<PivotTableElement> {
	


	@Override
	public void render(Workbook book,
			PivotTableElement element) {
	
		/* Generate the actual pivot table data */
		
		final PivotTableData table = element.getContent().getData();
		
		/* Generate the excel sheet */
		
		new BaseExcelTableRenderer<PivotTableElement, PivotTableData.Axis>(book, element) {
		

			@Override
			public List<FilterDescription> generateFilterDescriptions() {
                return element.getContent().getFilterDescriptions();
			}
			
			
			protected CellStyle[] rowHeaderStyles;

			
			@Override
			public void generate() { 

				/* Initialize Cell Styles */
				
				initColHeaderStyles(table.getRootColumn());
				initRowHeaderStyles();
		
				/* Generate the column headers */
				
				generateColumnHeaders(1, table.getRootColumn());
			
				int headerHeight = rowIndex;
				
				/* Create the rows */
				// row headers are in column 1
				sheet.setColumnWidth(0, 40 * 256);
				generateRows(table.getRootRow().getChildList(), 0);
				
				/* Finalize the sheet */
				
				sheet.setRowSumsBelow(false);
				sheet.createFreezePane(1, headerHeight);		
		
			}
			
			protected void initRowHeaderStyles() {
				
				int depth = table.getRootRow().getDepth();
				rowHeaderStyles = new CellStyle[depth];
				
				for(int i=0;i!=depth;++i) {
					
					CellStyle style = book.createCellStyle();
					style.setIndention((short) i);
					style.setWrapText(true);
					
					Font font = createBaseFont();
					if(i+1!=depth) {
						/* Has sub headers */
						font.setBoldweight(Font.BOLDWEIGHT_BOLD);
					} else {
						font.setBoldweight(Font.BOLDWEIGHT_NORMAL);
					}
					style.setFont(font);
					
					rowHeaderStyles[i] = style;
				}
			}					
			
				
			protected void generateRows(List<PivotTableData.Axis> rows, int indent) {
				
				for(PivotTableData.Axis pivotRow : rows) {
				
					Row row = sheet.createRow(rowIndex++);
					Cell headerCell = row.createCell(0);
					headerCell.setCellValue(factory.createRichTextString(pivotRow.getLabel()));
					headerCell.setCellStyle(rowHeaderStyles[indent]);	
					
					if(pivotRow.isLeaf()) {
						
						for(Entry<PivotTableData.Axis, Integer> entry : colIndexMap.entrySet()) {
						
							PivotTableData.Cell pivotCell = pivotRow.getCell(entry.getKey());
							if(pivotCell != null) {
								
								Cell cell = row.createCell(entry.getValue());
								cell.setCellValue(pivotCell.getValue());
							}
						}
					} else {
						
						int groupStart = rowIndex;
						
						generateRows(pivotRow.getChildList(), indent+1);
						
						int groupEnd = rowIndex;
						
						sheet.groupRow(groupStart, groupEnd);
					}
				}
			}
		};
	}
}
