package org.activityinfo.server.report;

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

import java.io.InputStreamReader;
import java.util.Calendar;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.activityinfo.shared.report.model.AdminDimension;
import org.activityinfo.shared.report.model.DimensionType;
import org.activityinfo.shared.report.model.ImageReportElement;
import org.activityinfo.shared.report.model.MapReportElement;
import org.activityinfo.shared.report.model.PivotChartReportElement;
import org.activityinfo.shared.report.model.PivotTableReportElement;
import org.activityinfo.shared.report.model.Report;
import org.activityinfo.shared.report.model.TableElement;
import org.activityinfo.shared.report.model.TextReportElement;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author Alex Bertram
 */
public class JaxbParseTest {

    public Report parseXml(String filename) throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance(Report.class.getPackage()
            .getName());
        Unmarshaller um = jc.createUnmarshaller();
        um.setEventHandler(new javax.xml.bind.helpers.DefaultValidationEventHandler());
        return (Report) um.unmarshal(new InputStreamReader(
            getClass()
                .getResourceAsStream("/report-def/parse-test/" + filename)));
    }

    public static void dumpXml(Report report) throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance(Report.class.getPackage()
            .getName());
        Marshaller m = jc.createMarshaller();
        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        m.marshal(report, System.out);
    }

    @Test
    public void testReport() throws JAXBException {
        Report report = parseXml("report.xml");
        Assert.assertEquals("My Title", report.getTitle());
    }

    @Test
    public void testElements() throws JAXBException {
        Report report = parseXml("report-elements.xml");
        dumpXml(report);
        Assert.assertEquals("element count", 7, report.getElements().size());
        Assert.assertTrue("pivotTable",
            report.getElements().get(0) instanceof PivotTableReportElement);
        Assert.assertTrue("pivotChart",
            report.getElements().get(1) instanceof PivotChartReportElement);
        Assert.assertTrue("table",
            report.getElements().get(2) instanceof TableElement);
        Assert.assertTrue("map",
            report.getElements().get(3) instanceof MapReportElement);
        Assert.assertTrue("static",
            report.getElements().get(4) instanceof TextReportElement);
        Assert.assertTrue("static",
            report.getElements().get(5) instanceof TextReportElement);
        Assert.assertTrue("static",
            report.getElements().get(6) instanceof ImageReportElement);
    }

    @Test
    public void testDimensions() throws Throwable {
        Report report = parseXml("dimensions.xml");
        dumpXml(report);

        Assert.assertEquals("element count", 1, report.getElements().size());
        Assert.assertTrue("is pivotTable",
            report.getElements().get(0) instanceof PivotTableReportElement);

        PivotTableReportElement table = report.getElement(0);
        Assert.assertEquals("dimension count", 2, table.getRowDimensions()
            .size());
        Assert.assertEquals("indicator type", DimensionType.Indicator, table
            .getRowDimensions().get(0).getType());
        Assert.assertTrue("admin subclass",
            table.getRowDimensions().get(1) instanceof AdminDimension);
        Assert.assertEquals("level is 91", 91, ((AdminDimension) table
            .getRowDimensions().get(1)).getLevelId());
    }

    @Test
    public void testMarshallElements() throws Throwable {
        Report report = new Report();
        report.addElement(new PivotChartReportElement());
        report.addElement(new PivotTableReportElement());

        dumpXml(report);
    }

    @Test
    public void testTable() throws Throwable {
        Report report = parseXml("table.xml");
        dumpXml(report);

        TableElement table = report.getElement(0);

        Assert.assertEquals("column count", 8, table.getRootColumn()
            .getChildren().size());
        // Assert.assertEquals(TableColumn.SortOrder.Descending,
        // table.getSortBy().get(0).getOrder());
        // Assert.assertEquals(TableColumn.SortOrder.Ascending,
        // table.getSortBy().get(1).getOrder());
    }

    @Test
    public void testFilter() throws Throwable {
        Report report = parseXml("filter.xml");
        dumpXml(report);

        Calendar minDate = Calendar.getInstance();
        minDate.setTime(report.getFilter().getDateRange().getMinDate());
        Assert.assertEquals(2008, minDate.get(Calendar.YEAR));
        Assert.assertEquals(11, minDate.get(Calendar.MONTH));
        Assert.assertEquals(1, minDate.get(Calendar.DATE));

        Assert.assertTrue(report.getFilter().isRestricted(
            DimensionType.Indicator));
        Assert.assertTrue(report.getFilter()
            .getRestrictions(DimensionType.Indicator).contains(21));

    }
}
