package org.activityinfo.server.report;

import org.junit.BeforeClass;

public class TableTest extends ReportDbUnitTest {

	@BeforeClass
	public static void runBefore() {
		populate("sites-simple1");
	}
	

//	@Test
//	public void testTableElement() throws SAXException, IOException {
//		
//		ReportParser parser = new ReportParser();
//		InputStream in = TableTest.class.getResourceAsStream("/report-def/table.xml");
//
//		/* Parse the Table Definition */
//
//		parser.parse( new InputSource( in ));
//
//		Report report = parser.getReport();
//
//		Assert.assertEquals(1, report.getElements().size());
//		Assert.assertTrue(report.getElements().get(0) instanceof TableElement);
//
//		TableElement table = (TableElement) report.getElements().get(0);
//
//		/* Generate the Table */
//
//		Injector injector = Guice.createInjector(new TestModule());
//
//		TableGenerator gtor = injector.getInstance(TableGenerator.class);
//
//		TableData tableData = gtor.generateData(table);
//
//		Assert.assertEquals("row count", 3, tableData.getRows().size());
//	}

	
}
