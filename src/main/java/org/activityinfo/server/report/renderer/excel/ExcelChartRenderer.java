/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.server.report.renderer.excel;

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
