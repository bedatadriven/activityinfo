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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

import com.google.common.collect.Maps;
import org.activityinfo.server.report.DummyPivotTableData;
import org.activityinfo.server.report.DummyPivotTableData2;
import org.activityinfo.shared.report.content.FilterDescription;
import org.activityinfo.shared.report.model.DimensionType;
import org.activityinfo.shared.report.model.PivotTableReportElement;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.junit.Assert;
import org.junit.Test;


/**
 * @author Alex Bertram (akbertram@gmail.com)
 */
public class PivotTableRendererTest {

    @Test
    public void test() {

        // input test data : element + content
        DummyPivotTableData testData = new DummyPivotTableData();
        PivotTableReportElement element = testData.Foobar1612Element();

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
                new FileOutputStream(outputFile.getAbsoluteFile()
                    + "/PivotTableRendererTest.xls");

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
        PivotTableReportElement element = testData.testElement();

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
                new FileOutputStream(outputFile.getAbsoluteFile()
                    + "/testNoColumnDimensions.xls");

            book.write(out);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // VERIFY output

        Sheet sheet = book.getSheetAt(0);
        Assert.assertEquals(100, (int) sheet.getRow(5).getCell(1)
            .getNumericCellValue());

    }
    
    @Test
    public void veryLongFilter() {


        // input test data : element + content
        DummyPivotTableData2 testData = new DummyPivotTableData2();
        PivotTableReportElement element = testData.testElement();
        
        // Filtering on 4000 indicators....
        Map<Integer, String> labelMap = Maps.newHashMap();
        for(int i=1;i!=10000;++i) {
            element.getFilter().addRestriction(DimensionType.Indicator, i);
            labelMap.put(i, "Very long indicator name " + i);
        }

        element.getContent().getFilterDescriptions().add(
            new FilterDescription(DimensionType.Indicator, labelMap));
        

        // Destination book
        HSSFWorkbook book = new HSSFWorkbook();

        // CLASS under test

        ExcelPivotTableRenderer renderer = new ExcelPivotTableRenderer();
        renderer.render(book, element);
    }

}
