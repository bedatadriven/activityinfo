package org.activityinfo.shared.command;

import java.util.Arrays;

import org.activityinfo.server.command.CommandTestCase2;
import org.activityinfo.server.database.OnDataSet;
import org.activityinfo.server.database.TestDatabaseModule;
import org.activityinfo.server.report.ReportModule;
import org.activityinfo.shared.command.GeneratePivotTable;
import org.activityinfo.shared.exception.CommandException;
import org.activityinfo.shared.report.content.PivotContent;
import org.activityinfo.shared.report.model.AttributeGroupDimension;
import org.activityinfo.shared.report.model.Dimension;
import org.activityinfo.shared.report.model.DimensionType;
import org.activityinfo.shared.report.model.PivotTableReportElement;
import org.activityinfo.test.InjectionSupport;
import org.activityinfo.test.Modules;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(InjectionSupport.class)
@Modules({TestDatabaseModule.class, ReportModule.class})
@OnDataSet("/dbunit/sites-simple1.db.xml")
public class GeneratePivotTableHandlerTest extends CommandTestCase2 {

	
	@Test
	public void serverSide() throws CommandException {
		
		PivotTableReportElement element = new PivotTableReportElement();
		element.setRowDimensions(Arrays.asList(new Dimension(DimensionType.Indicator)));
		element.setColumnDimensions(Arrays.asList(new Dimension(DimensionType.Partner)));

		PivotContent content = execute(new GeneratePivotTable(element));
		
		System.out.println(content.getData());
		
	}
	
	@Test
	public void withNullAttribute() throws CommandException {
		PivotTableReportElement element = new PivotTableReportElement();
		element.setRowDimensions(Arrays.asList(new Dimension(DimensionType.Indicator)));
		element.setColumnDimensions(Arrays.asList((Dimension)new AttributeGroupDimension(1)));
		
		PivotContent content = execute(new GeneratePivotTable(element));
		
		System.out.println(content.getData());
		
	}
	
}
