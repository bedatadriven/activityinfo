package org.sigmah.server.report.renderer.excel;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.sigmah.server.report.renderer.Renderer;
import org.sigmah.shared.report.content.BubbleMapMarker;
import org.sigmah.shared.report.content.IconMapMarker;
import org.sigmah.shared.report.content.MapContent;
import org.sigmah.shared.report.content.MapMarker;
import org.sigmah.shared.report.model.MapElement;
import org.sigmah.shared.report.model.ReportElement;

import java.io.IOException;
import java.io.OutputStream;

/**
 * 
 * @author Alex Bertram
 */
public class ExcelMapDataExporter implements Renderer {

    public void render(ReportElement element, OutputStream stm) throws IOException {

        if(!(element instanceof MapElement)) {
            throw new RuntimeException("ExcelMapDataExporter accepts only MapElements");
        }

        MapContent content = ((MapElement)element).getContent();

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

    private class Helper {
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
