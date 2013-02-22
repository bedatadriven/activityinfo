

package org.activityinfo.server.report.renderer.excel;

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
