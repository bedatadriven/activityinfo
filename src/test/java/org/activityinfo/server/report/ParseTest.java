package org.activityinfo.server.report;

import java.io.IOException;
import java.io.InputStream;

import org.activityinfo.server.report.ReportParser;
import org.activityinfo.shared.report.model.*;
import org.junit.Assert;
import org.junit.Test;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;


import junit.framework.TestCase;

public class ParseTest extends TestCase {



    @Test
    public void testTableElement() throws SAXException, IOException {

        ReportParser parser = new ReportParser();
        InputStream in = ParseTest.class.getResourceAsStream("/report-def/table.xml");

        parser.parse( new InputSource( in ));

        Report report = parser.getReport();

        Assert.assertEquals(1, report.getElements().size());
        Assert.assertTrue(report.getElements().get(0) instanceof TableElement);

        TableElement table = (TableElement) report.getElements().get(0);

        Assert.assertEquals("site.date1", table.getRootColumn().getChildren().get(0).getProperty());
        Assert.assertEquals("location.name", table.getRootColumn().getChildren().get(3).getProperty());
        Assert.assertEquals(1, table.getRootColumn().getChildren().get(2).getPropertyQualifyingId());
        Assert.assertEquals(2, table.getRootColumn().getChildren().get(6).getChildren().size());
        //Assert.assertEquals(2, table.getRootColumn().getChildren().get(6).getChildList().get(1).getChildren().size());
        Assert.assertEquals(false, table.getSortBy().get(0).isOrderAscending());
    }

    @Test
    public void testAdmin() throws Exception {
        ReportParser parser = new ReportParser();
        InputStream in = ParseTest.class.getResourceAsStream("/report-def/table.xml");

        parser.parse( new InputSource( in ));

        Report report = parser.getReport();


    }

    @Test
    public void testDimensions() throws SAXException, IOException {

        ReportParser parser = new ReportParser();
        InputStream in = ParseTest.class.getResourceAsStream("/report-def/indicators.xml");

        parser.parse( new InputSource( in ));

        Report report = parser.getReport();

        Assert.assertEquals(2, report.getElements().size());
        Assert.assertTrue(report.getElements().get(0) instanceof PivotTableElement);
        Assert.assertTrue(report.getElements().get(1) instanceof MapElement);

        PivotTableElement table = (PivotTableElement) report.getElements().get(0);
        Assert.assertEquals(2, table.getColumnDimensions().size());

        MapElement map = (MapElement) report.getElements().get(1);
        GsMapLayer layer = (GsMapLayer) map.getLayers().get(0);
        Assert.assertEquals(1, layer.getColorDimensions().size());



    }


}
