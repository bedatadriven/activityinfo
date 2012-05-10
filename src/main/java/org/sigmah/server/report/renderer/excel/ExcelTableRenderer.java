/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.report.renderer.excel;

import java.util.Date;
import java.util.List;

import org.activityinfo.shared.dto.SiteDTO;
import org.activityinfo.shared.report.content.FilterDescription;
import org.activityinfo.shared.report.content.TableData;
import org.activityinfo.shared.report.model.TableColumn;
import org.activityinfo.shared.report.model.TableElement;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;

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
				String[] colIndexes = new String[leaves.size()];

				for(int i=0; i!=leaves.size(); ++i) {	
					colIndexes[i] = leaves.get(i).getSitePropertyName();
				}
				
				/* Now write the actual rows */
				
				for(SiteDTO rowData : tableData.getRows()) {
					
					Row row = sheet.createRow(rowIndex++);
					
					for(int i=0; i!=colIndexes.length;++i) {
						
						Object value = null;
						
						if(colIndexes[i] != null) {
							value = rowData.get(colIndexes[i]);
						}
						
						Cell cell = row.createCell(i);
						
						if(value instanceof Number) {
							cell.setCellValue(((Number)value).doubleValue());
						} else if(value instanceof Date) {
							cell.setCellValue((Date)value);
						} else if(value instanceof Boolean) {
							cell.setCellValue((Boolean)value);
						} else if(value != null){
							cell.setCellValue(factory.createRichTextString(value.toString()));
						}	
					}
				}
			}		
								
		}; 
		
	}

}
