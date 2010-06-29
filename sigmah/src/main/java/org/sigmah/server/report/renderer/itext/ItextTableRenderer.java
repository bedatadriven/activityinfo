/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.report.renderer.itext;

import com.google.inject.Inject;
import com.lowagie.text.*;
import org.sigmah.shared.report.content.TableData;
import org.sigmah.shared.report.model.TableColumn;
import org.sigmah.shared.report.model.TableElement;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.Date;
import java.util.List;

/**
 * Renders a {@link org.sigmah.shared.report.model.TableElement} to an iText Document.
 *
 * @author Alex Bertram
 */
public class ItextTableRenderer implements ItextRenderer<TableElement> {

    private final ItextMapRenderer mapRenderer;

    @Inject
    public ItextTableRenderer(ItextMapRenderer mapRenderer) {
        this.mapRenderer = mapRenderer;
    }

    @Override
    public void render(DocWriter writer, Document document, TableElement element) throws DocumentException {
        document.add(ThemeHelper.elementTitle(element.getTitle()));
        ItextRendererHelper.addFilterDescription(document, element.getContent().getFilterDescriptions());

        TableData data = element.getContent().getData();

        if(data.isEmpty()) {
            renderEmptyText(document);

        } else {
            if(element.getMap() != null) {
                mapRenderer.renderMap(writer, element.getMap(), document);
            }
            renderTable(document, data);
        }
    }

    private void renderEmptyText(Document document) throws DocumentException {
        document.add(new Paragraph("Aucune Données"));  // TODO: i18n
    }

    private void renderTable(Document document, TableData data) throws DocumentException {
        int colDepth = data.getRootColumn().getDepth();
        List<TableColumn> colLeaves = data.getRootColumn().getLeaves();
        int colBreadth = colLeaves.size();

        Table table = new Table(colBreadth, 1);
        table.setUseVariableBorders(true);
        table.setWidth(100.0f);
        table.setBorderWidth(0);

        // first write the column headers

        for(int depth = 1; depth<=colDepth; ++depth) {
            List<TableColumn> columns = data.getRootColumn().getDescendantsAtDepth(depth);
            for(TableColumn column : columns) {
                Cell cell = ThemeHelper.columnHeaderCell(column.getLabel(), column.isLeaf(),
                        computeHAlign(column));
                cell.setColspan(Math.max(1, column.getChildren().size()));
                cell.setRowspan(colDepth-depth-column.getDepth()+1);
                table.addCell(cell);
            }
        }
        table.endHeaders();

        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM);
        NumberFormat numberFormat = NumberFormat.getIntegerInstance();
        numberFormat.setGroupingUsed(true);

        for(TableData.Row row : data.getRows()) {
            for(TableColumn column : colLeaves) {

                Object value = row.values[data.getColumnIndex(column)];

                String label = "";
                if(value instanceof Date) {
                    label = dateFormat.format(value);
                } else if(value instanceof Number) {
                    label = numberFormat.format(value);
                } else if(value != null) {
                    label = value.toString();
                }

                table.addCell(ThemeHelper.bodyCell(label, false, 0, true, computeHAlign(column)));
            }
        }
        document.add(table);
    }

    protected int computeHAlign(TableColumn column) {
        if(!column.isLeaf()) {
            return Cell.ALIGN_CENTER;
        } else if("indicator".equals(column.getProperty())) {
            return Cell.ALIGN_RIGHT;
        } else if("map".equals(column.getProperty())) {
            return Cell.ALIGN_CENTER;
        } else {
            return Cell.ALIGN_LEFT;
        }
    }
}
