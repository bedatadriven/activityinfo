/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.server.report.renderer.itext;

import java.text.NumberFormat;
import java.util.List;

import org.activityinfo.shared.report.content.PivotTableData;
import org.activityinfo.shared.report.model.PivotTableReportElement;

import com.lowagie.text.BadElementException;
import com.lowagie.text.Cell;
import com.lowagie.text.DocWriter;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Table;

/**
 * Renders a {@link org.activityinfo.shared.report.model.PivotTableReportElement} to an iText
 * document (either PDF or RTF)
 *
 */
public class ItextPivotTableRenderer implements ItextRenderer<PivotTableReportElement> {	
	
	@Override
	public void render(DocWriter writer, Document document, PivotTableReportElement element) throws DocumentException {

        document.add(ThemeHelper.elementTitle(element.getTitle()));
        ItextRendererHelper.addFilterDescription(document, element.getContent().getFilterDescriptions());
        ItextRendererHelper.addDateFilterDescription(document, element.getFilter().getDateRange());
        PivotTableData data = element.getContent().getData();

        if(data.isEmpty()) {
            document.add(new Paragraph("Aucune Données"));  // TODO: i18n

        } else {
            int colDepth = data.getRootColumn().getDepth();
            List<PivotTableData.Axis> colLeaves = data.getRootColumn().getLeaves();
            int colBreadth = colLeaves.size();

            Table table = new Table(colBreadth+1, 1);
            table.setUseVariableBorders(true);
            table.setWidth(100.0f);
            table.setWidths(calcColumnWidths(document, data, colLeaves));
            table.setBorderWidth(0);

            // first write the column headers

            for(int depth = 1; depth<=colDepth; ++depth) {

                if(depth == 1) {
                    Cell cell = ThemeHelper.cornerCell();
                    cell.setRowspan(colDepth);
                    table.addCell(cell);
                }

                List<PivotTableData.Axis> columns = data.getRootColumn().getDescendantsAtDepth(depth);
                for(PivotTableData.Axis column : columns) {
                    Cell cell = ThemeHelper.columnHeaderCell(column.getLabel(), column.isLeaf());
                    cell.setColspan(Math.max(1, column.getLeaves().size()));
                    table.addCell(cell);
                }

            }
            table.endHeaders();

            for(PivotTableData.Axis row : data.getRootRow().getChildren()) {
                writeRow(table, row, colLeaves, 0);
            }
            document.add(table);
        }
    }

    private float[] calcColumnWidths(Document doc, PivotTableData data, List<PivotTableData.Axis> leafColumns) {
        float[] widths = new float[leafColumns.size()+1];
        float tableWidth = doc.getPageSize().getWidth() - doc.leftMargin() - doc.rightMargin();
        float valueColumnWidth = 47f;
        
		widths[0] = tableWidth - (leafColumns.size() * valueColumnWidth);
		if(widths[0] < valueColumnWidth) {
			widths[0] = valueColumnWidth;
		}
		for(int i = 1; i!=widths.length; ++i) {
            widths[i] = valueColumnWidth;
        }
        return widths;
    }

    private void writeRow(Table table, PivotTableData.Axis row, List<PivotTableData.Axis> leafColumns, int depth) throws BadElementException {

        table.addCell(ThemeHelper.bodyCell(row.getLabel(), true, depth, row.isLeaf()));

        NumberFormat format = NumberFormat.getIntegerInstance();
        format.setGroupingUsed(true);

        for(PivotTableData.Axis column : leafColumns) {

            PivotTableData.Cell pivotCell = row.getCell(column);
            String label;
            if(pivotCell == null) {
                label = null;
            } else {
                label = format.format(pivotCell.getValue());
            }
            table.addCell(ThemeHelper.bodyCell(label, false, depth, row.isLeaf()));
        }

        for(PivotTableData.Axis childRow : row.getChildren()) {
            writeRow(table, childRow, leafColumns, depth+1);
        }
    }
}
