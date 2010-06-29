package org.sigmah.server.report.renderer.itext;

import com.lowagie.text.*;
import org.sigmah.shared.report.content.PivotTableData;
import org.sigmah.shared.report.model.PivotTableElement;

import java.text.NumberFormat;
import java.util.List;

/**
 * Renders a {@link org.sigmah.shared.report.model.PivotTableElement} to an iText
 * document (either PDF or RTF)
 *
 * @author Alex Bertram
 */
public class ItextPivotTableRenderer implements ItextRenderer<PivotTableElement> {

    public void render(DocWriter writer, Document document, PivotTableElement element) {
        try {

            document.add(ThemeHelper.elementTitle(element.getTitle()));
            ItextRendererHelper.addFilterDescription(document, element.getContent().getFilterDescriptions());

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
                        cell.setColspan(Math.max(1, column.getChildCount()));
                        table.addCell(cell);
                    }

                }
                table.endHeaders();

                for(PivotTableData.Axis row : data.getRootRow().getChildren()) {
                    writeRow(table, row, colLeaves, 0);
                }

                document.add(table);
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    protected float[] calcColumnWidths(Document doc, PivotTableData data, List<PivotTableData.Axis> leafColumns) {
        // assume fixed column size
        float[] widths = new float[leafColumns.size()+1];
        widths[0] = doc.getPageSize().getWidth() - doc.leftMargin() - doc.rightMargin() -
                 (leafColumns.size() * 47f);
        for(int i = 1; i!=widths.length; ++i) {
            widths[i] = 47f;
        }
        return widths;
    }

    protected void writeRow(Table table, PivotTableData.Axis row, List<PivotTableData.Axis> leafColumns, int depth) throws BadElementException {

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
