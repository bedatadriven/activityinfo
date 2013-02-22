

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

import java.io.IOException;
import java.io.OutputStream;

import org.activityinfo.server.report.renderer.Renderer;
import org.activityinfo.shared.report.content.BubbleMapMarker;
import org.activityinfo.shared.report.content.IconMapMarker;
import org.activityinfo.shared.report.content.MapContent;
import org.activityinfo.shared.report.content.MapMarker;
import org.activityinfo.shared.report.model.MapReportElement;
import org.activityinfo.shared.report.model.ReportElement;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

/**
 * 
 * @author Alex Bertram
 */
public class ExcelMapDataExporter implements Renderer {

    @Override
	public void render(ReportElement element, OutputStream stm) throws IOException {

        if(!(element instanceof MapReportElement)) {
            throw new RuntimeException("ExcelMapDataExporter accepts only MapElements");
        }

        MapContent content = ((MapReportElement)element).getContent();

        Workbook book = new HSSFWorkbook();
        Sheet sheet = book.createSheet();

        Helper helper = new Helper(book);

        Row headerRow = sheet.createRow(0);
        helper.addCell(headerRow, 0, "Latitude");
        helper.addCell(headerRow, 1, "Longitude");
        helper.addCell(headerRow, 2, "Value");
        helper.addCell(headerRow, 3, "Color");
        helper.addCell(headerRow, 4, "Icon");

        int rowIndex = 1;

        for(MapMarker marker : content.getMarkers()) {

            Row dataRow = sheet.createRow(rowIndex++);
            helper.addCell(dataRow, 0, marker.getLat());
            helper.addCell(dataRow, 1, marker.getLng());
            if(marker instanceof BubbleMapMarker) {
                BubbleMapMarker bmarker = (BubbleMapMarker) marker;
                helper.addCell(dataRow, 2, bmarker.getValue());
                helper.addCell(dataRow, 3, bmarker.getColor());
            }
            if(marker instanceof IconMapMarker) {
                IconMapMarker imarker = (IconMapMarker) marker;
                if(imarker.getIcon() != null) {
                    helper.addCell(dataRow, 4, imarker.getIcon().getName());
                }
            }

        }

        book.write(stm);

    }

    @Override
    public String getMimeType() {
        return "application/vnd.ms-excel";
    }

    @Override
    public String getFileSuffix() {
        return ".xls";
    }

    private final class Helper {
        private CreationHelper factory;

        private Helper(Workbook book) {
            this.factory = book.getCreationHelper();
        }

        private void addCell(Row row, int colIndex, String value) {
            if(value != null) {
                Cell cell = row.createCell(colIndex);
                cell.setCellValue(factory.createRichTextString(value));
            }
        }

        private void addCell(Row row, int colIndex, double value) {
            Cell cell = row.createCell(colIndex);
            cell.setCellValue(value);
        }

    }


}
