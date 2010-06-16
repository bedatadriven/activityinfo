/*
 * This file is part of ActivityInfo.
 *
 * ActivityInfo is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * ActivityInfo is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with ActivityInfo.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright 2009 Alex Bertram and contributors.
 */

package org.activityinfo.server.report.renderer.excel;

import org.apache.poi.ss.usermodel.*;
import org.activityinfo.shared.report.model.PivotTableElement;
import org.activityinfo.shared.report.model.PivotChartElement;
import org.activityinfo.shared.report.content.PivotTableData;
import org.activityinfo.shared.report.content.FilterDescription;

import java.util.List;
import java.util.Map;

/**
 * @author Alex Bertram
 */
public class ExcelChartRenderer implements ExcelRenderer<PivotChartElement> {

	@Override
	public void render(Workbook book,
			PivotChartElement element) {

		/* Generate the actual pivot table data */

		final PivotTableData table = element.getContent().getData();

		/* Generate the excel sheet */

		new BaseExcelRenderer<PivotChartElement>(book, element) {


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
