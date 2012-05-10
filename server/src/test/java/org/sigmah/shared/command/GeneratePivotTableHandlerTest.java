package org.sigmah.shared.command;

import java.util.Arrays;

import org.activityinfo.shared.command.GeneratePivotTable;
import org.activityinfo.shared.exception.CommandException;
import org.activityinfo.shared.report.content.PivotContent;
import org.activityinfo.shared.report.model.AttributeGroupDimension;
import org.activityinfo.shared.report.model.Dimension;
import org.activityinfo.shared.report.model.DimensionType;
import org.activityinfo.shared.report.model.PivotTableReportElement;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.sigmah.server.command.CommandTestCase2;
import org.sigmah.server.database.OnDataSet;
import org.sigmah.server.database.TestDatabaseModule;
import org.sigmah.server.report.ReportModule;
import org.sigmah.test.InjectionSupport;
import org.sigmah.test.Modules;

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
