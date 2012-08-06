package org.activityinfo.server.report;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;

import java.util.Collections;
import java.util.Set;

import org.activityinfo.server.command.DispatcherSync;
import org.activityinfo.server.database.hibernate.dao.IndicatorDAO;
import org.activityinfo.server.database.hibernate.entity.Indicator;
import org.activityinfo.server.database.hibernate.entity.User;
import org.activityinfo.server.report.generator.PivotChartGenerator;
import org.activityinfo.shared.command.Filter;
import org.activityinfo.shared.command.GetDimensionLabels;
import org.activityinfo.shared.command.PivotSites;
import org.activityinfo.shared.report.model.DateRange;
import org.activityinfo.shared.report.model.Dimension;
import org.activityinfo.shared.report.model.DimensionType;
import org.activityinfo.shared.report.model.PivotChartReportElement;
import org.activityinfo.shared.report.model.PivotChartReportElement.Type;
import org.junit.Test;

public class EmptyChartsTest {

	
	@Test
	public void generate() {
		PivotChartReportElement element = new PivotChartReportElement(Type.StackedBar);
		element.setIndicator(1);
		element.addCategoryDimension(new Dimension(DimensionType.Partner));
		element.addSeriesDimension(new Dimension(DimensionType.Database));
	
		DispatcherSync dispatcher = createMock(DispatcherSync.class);
		expect(dispatcher.execute(isA(PivotSites.class)))
			.andReturn(new PivotSites.PivotResult(Collections.EMPTY_LIST));
		
		expect(dispatcher.execute(isA(GetDimensionLabels.class)))
			.andReturn(new GetDimensionLabels.DimensionLabels(Collections.EMPTY_MAP));
		
		replay(dispatcher);
		
		IndicatorDAO indicatorDAO = createMock(IndicatorDAO.class);
		expect(indicatorDAO.findById(eq(1))).andReturn(new Indicator());
		replay(indicatorDAO);
		
				
		PivotChartGenerator generator = new PivotChartGenerator(dispatcher, indicatorDAO);
		generator.generate(new User(), element, new Filter(), new DateRange());		
	}
	
	
}
