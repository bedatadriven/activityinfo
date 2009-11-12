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

package org.activityinfo.server.report.renderer.itext;

import com.google.inject.Inject;
import com.lowagie.text.*;
import org.activityinfo.shared.report.content.PivotTableData;
import org.activityinfo.shared.report.content.TableData;
import org.activityinfo.shared.report.model.TableColumn;
import org.activityinfo.shared.report.model.TableElement;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.Date;
import java.util.List;

/**
 * @author Alex Bertram
 */
public class ItextTableRenderer implements ItextRenderer<TableElement> {

    private final ItextMapRenderer mapRenderer;

    @Inject
    public ItextTableRenderer(ItextMapRenderer mapRenderer) {
        this.mapRenderer = mapRenderer;
    }

    @Override
    public void render(DocWriter writer, TableElement element, Document document) throws DocumentException {

        document.add(ThemeHelper.elementTitle(element.getTitle()));

        TableData data = element.getContent().getData();

        if(data.isEmpty()) {
            document.add(new Paragraph("Aucune Données"));  // TODO: i18n

        } else {

            if(element.getMap() != null) {
                mapRenderer.renderMap(writer, element.getMap(), document);
            }

            int colDepth = data.getRootColumn().getDepth();
            List<TableColumn> colLeaves = data.getRootColumn().getLeaves();
            int colBreadth = colLeaves.size();

            Table table = new Table(colBreadth, 1);
            table.setUseVariableBorders(true);
            table.setWidth(100.0f);
            //table.setWidths(calcColumnWidths(document, data, colLeaves));
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
                    if(value instanceof Date)
                        label = dateFormat.format(value);
                    else if(value instanceof Number)
                        label = numberFormat.format(value);
                    else if(value != null)
                        label = value.toString();

                    table.addCell(ThemeHelper.bodyCell(label, false, 0, true, computeHAlign(column)));
                }
            }

            document.add(table);
        }
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
}
