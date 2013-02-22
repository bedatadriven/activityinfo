

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

import java.util.List;

import org.activityinfo.shared.report.content.FilterDescription;
import org.activityinfo.shared.report.content.PivotTableData;
import org.activityinfo.shared.report.model.PivotChartReportElement;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;

/**
 * @author Alex Bertram
 */
public class ExcelChartRenderer implements ExcelRenderer<PivotChartReportElement> {

	@Override
	public void render(Workbook book,
			PivotChartReportElement element) {

		/* Generate the actual pivot table data */

		final PivotTableData table = element.getContent().getData();

		/* Generate the excel sheet */

		new BaseExcelRenderer<PivotChartReportElement>(book, element) {


			@Override
			public List<FilterDescription> generateFilterDescriptions() {
                return element.getContent().getFilterDescriptions();
			}


			@Override
			public void generate() {

                PivotTableData data = this.element.getContent().getData();
                List<PivotTableData.Axis> rows = data.getRootRow().getLeaves();
                List<PivotTableData.Axis> cols = data.getRootColumn().getLeaves();

                Row headerRow = sheet.createRow(rowIndex++);
                for(int i=0;i!=cols.size();++i) {
                    Cell colHeaderCell = headerRow.createCell(i+1);
                    colHeaderCell.setCellValue(factory.createRichTextString(cols.get(i).flattenLabel()));
                }

                for(int i=0;i!=rows.size();++i) {

                    Row row = sheet.createRow(rowIndex++);

                    // header
                    Cell rowHeaderCell = row.createCell(0);
                    rowHeaderCell.setCellValue(factory.createRichTextString(rows.get(i).flattenLabel()));

                    // values
                    for(int j=0;j!=cols.size();++j) {
                        Cell valueCell = row.createCell(j+1);
                        valueCell.setCellValue(rows.get(i).getCell(cols.get(j)).getValue());
                    }
                }
			}

        };
	}
}
