package org.activityinfo.server.report.renderer.excel;

import org.activityinfo.server.report.DummyPivotTableData;
import org.activityinfo.server.report.DummyPivotTableData2;
import org.activityinfo.shared.report.model.PivotTableElement;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @author Alex Bertram (akbertram@gmail.com)
 */
public class PivotTableRendererTest {

    @Test
    public void test() {


        // input test data : element + content
        DummyPivotTableData testData = new DummyPivotTableData();
        PivotTableElement element = testData.Foobar1612Element();

        // Destination book
        HSSFWorkbook book = new HSSFWorkbook();

        // CLASS under test

        ExcelPivotTableRenderer renderer = new ExcelPivotTableRenderer();
        renderer.render(book, element);


        // VERIFY output

        Sheet sheet = book.getSheetAt(0);
        Assert.assertEquals("title cell", element.getTitle(),
                sheet.getRow(0).getCell(0).getRichStringCellValue().getString());
        Assert.assertEquals("last column header", "Abri",
                sheet.getRow(4).getCell(5).getRichStringCellValue().getString());
        Assert.assertEquals("columns headers indentation", (short) 0,
                sheet.getRow(4).getCell(3).getCellStyle().getIndention());

        // Write output to disk
        try {
            File outputFile = new File("target/report-tests");
            outputFile.mkdirs();
            OutputStream out =
                    new FileOutputStream(outputFile.getAbsoluteFile() + "/PivotTableRendererTest.xls");

            book.write(out);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testNoColumnDimensions() {

        // input test data : element + content
        DummyPivotTableData2 testData = new DummyPivotTableData2();
        PivotTableElement element = testData.testElement();

        // Destination book
        HSSFWorkbook book = new HSSFWorkbook();

        // CLASS under test

        ExcelPivotTableRenderer renderer = new ExcelPivotTableRenderer();
        renderer.render(book, element);

        // Write output to disk
        try {
            File outputFile = new File("target/report-tests");
            outputFile.mkdirs();
            OutputStream out =
                    new FileOutputStream(outputFile.getAbsoluteFile() + "/testNoColumnDimensions.xls");

            book.write(out);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


        // VERIFY output

        Sheet sheet = book.getSheetAt(0);
        Assert.assertEquals(100, (int) sheet.getRow(5).getCell(1).getNumericCellValue());

    }

}
