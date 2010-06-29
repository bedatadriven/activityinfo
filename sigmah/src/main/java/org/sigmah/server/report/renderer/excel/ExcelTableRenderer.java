package org.sigmah.server.report.renderer.excel;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.sigmah.shared.report.content.FilterDescription;
import org.sigmah.shared.report.content.TableData;
import org.sigmah.shared.report.model.TableColumn;
import org.sigmah.shared.report.model.TableElement;

import java.util.Date;
import java.util.List;

public class ExcelTableRenderer implements ExcelRenderer<TableElement> {


	@Override
	public void render(Workbook book, TableElement element) {
		
		final TableData tableData = element.getContent().getData();

		new BaseExcelTableRenderer<TableElement, TableColumn>(book, element) {

			@Override
			public List<FilterDescription> generateFilterDescriptions() {
				return element.getContent().getFilterDescriptions();
			}
			
			@Override 
			public void generate() {
		
				/* Generate the column headers for the table */
				
				initColHeaderStyles(element.getRootColumn());
				
				generateColumnHeaders(0, element.getRootColumn());
				
				int headerHeight = rowIndex;
			
				sheet.createFreezePane(
						element.getFrozenColumns(), 
						headerHeight);
				
				
				/* Prepare the generators and indexes */
				
				List<TableColumn> leaves = element.getRootColumn().getLeaves();
				int[] colIndexes = new int[leaves.size()];

				for(int i=0; i!=leaves.size(); ++i) {	
					colIndexes[i] = tableData.getColumnIndex(leaves.get(i));
				}
				
				/* Now write the actual rows */
				
				for(TableData.Row rowData : tableData.getRows()) {
					
					Row row = sheet.createRow(rowIndex++);
					
					for(int i=0; i!=colIndexes.length;++i) {
						
						Object value = null;
						
						if(colIndexes[i] >= 0) {
							value = rowData.values[colIndexes[i]];
						}
						
						Cell cell = row.createCell(i);
						
						if(value == null) {
							
						} else if(value instanceof Number) {
							cell.setCellValue(((Number)value).doubleValue());
						} else if(value instanceof Date) {
							cell.setCellValue((Date)value);
						} else if(value instanceof Boolean) {
							cell.setCellValue((Boolean)value);
						} else {
							cell.setCellValue(factory.createRichTextString(value.toString()));
						}	
					}
				}
			}		
								
		}; 
		
	}

}
