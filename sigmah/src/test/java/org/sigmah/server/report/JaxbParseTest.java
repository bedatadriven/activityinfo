/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.report;

import org.junit.Assert;
import org.junit.Test;
import org.sigmah.shared.report.model.*;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.InputStreamReader;
import java.util.Calendar;

/**
 * @author Alex Bertram
 */
public class JaxbParseTest {

    public Report parseXml(String filename) throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance(Report.class.getPackage().getName());
        Unmarshaller um = jc.createUnmarshaller();
        um.setEventHandler(new javax.xml.bind.helpers.DefaultValidationEventHandler());
        return (Report) um.unmarshal(new InputStreamReader(
                getClass().getResourceAsStream("/report-def/parse-test/" + filename)));
    }

    public static void dumpXml(Report report) throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance(Report.class.getPackage().getName());
        Marshaller m = jc.createMarshaller();
        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        m.marshal(report, System.out);
    }

    @Test
    public void testReport() throws JAXBException {
        Report report = parseXml("report.xml");
        Assert.assertEquals("My Title", report.getTitle());
        Assert.assertEquals(ReportFrequency.Monthly, report.getFrequency());
    }                        

    @Test
    public void testElements() throws JAXBException {
        Report report = parseXml("report-elements.xml");
        Assert.assertEquals("element count", 4, report.getElements().size());
        Assert.assertTrue("pivotTable", report.getElements().get(0) instanceof PivotTableElement);
        Assert.assertTrue("pivotChart", report.getElements().get(1) instanceof PivotChartElement);
        Assert.assertTrue("table", report.getElements().get(2) instanceof TableElement);
        Assert.assertTrue("map", report.getElements().get(3) instanceof MapElement);

    }

    @Test
    public void testDimensions() throws Throwable {
        Report report = parseXml("dimensions.xml");
        dumpXml(report);

        Assert.assertEquals("element count", 1, report.getElements().size());
        Assert.assertTrue("is pivotTable", report.getElements().get(0) instanceof PivotTableElement);

        PivotTableElement table = report.getElement(0);
        Assert.assertEquals("dimension count", 2, table.getRowDimensions().size());
        Assert.assertEquals("indicator type", DimensionType.Indicator, table.getRowDimensions().get(0).getType());
        Assert.assertTrue("admin subclass", table.getRowDimensions().get(1) instanceof AdminDimension);
        Assert.assertEquals("level is 91", 91, ((AdminDimension) table.getRowDimensions().get(1)).getLevelId() );
    }

    @Test
    public void testMarshallElements() throws Throwable {
        Report report = new Report();
        report.addElement(new PivotChartElement());
        report.addElement(new PivotTableElement());

        dumpXml(report);
    }

    @Test
    public void testTable() throws Throwable {
        Report report = parseXml("table.xml");
        dumpXml(report);

        TableElement table = report.getElement(0);

        Assert.assertEquals("column count", 8, table.getRootColumn().getChildren().size());
//        Assert.assertEquals(TableColumn.SortOrder.Descending, table.getSortBy().get(0).getOrder());
//        Assert.assertEquals(TableColumn.SortOrder.Ascending, table.getSortBy().get(1).getOrder());
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

        Assert.assertTrue(report.getFilter().isRestricted(DimensionType.Indicator));
        Assert.assertTrue(report.getFilter().getRestrictions(DimensionType.Indicator).contains(21));

    }
}
