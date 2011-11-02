package org.sigmah.server.report;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;

import java.util.Collections;
import java.util.Set;

import org.junit.Test;
import org.sigmah.server.command.DispatcherSync;
import org.sigmah.server.report.generator.PivotChartGenerator;
import org.sigmah.shared.command.GetDimensionLabels;
import org.sigmah.shared.command.PivotSites;
import org.sigmah.shared.dao.Filter;
import org.sigmah.shared.dao.IndicatorDAO;
import org.sigmah.shared.domain.Indicator;
import org.sigmah.shared.domain.User;
import org.sigmah.shared.report.model.DateRange;
import org.sigmah.shared.report.model.Dimension;
import org.sigmah.shared.report.model.DimensionType;
import org.sigmah.shared.report.model.PivotChartReportElement;
import org.sigmah.shared.report.model.PivotChartReportElement.Type;

public class EmptyChartsTest {

	
	@Test
	public void generate() {
		PivotChartReportElement element = new PivotChartReportElement(Type.StackedBar);
		element.setIndicator(1);
		element.addCategoryDimension(new Dimension(DimensionType.Partner));
		element.addSeriesDimension(new Dimension(DimensionType.Database));
	
		DispatcherSync dispatcher = createMock(DispatcherSync.class);
		expect(dispatcher.execute(new PivotSites(isA(Set.class), isA(Filter.class))))
			.andReturn(new PivotSites.PivotResult(Collections.EMPTY_LIST));
		
		expect(dispatcher.execute(new GetDimensionLabels(eq(DimensionType.Indicator), isA(Set.class))))
			.andReturn(new GetDimensionLabels.DimensionLabels(Collections.EMPTY_MAP));
		
		replay(dispatcher);
		
		IndicatorDAO indicatorDAO = createMock(IndicatorDAO.class);
		expect(indicatorDAO.findById(eq(1))).andReturn(new Indicator());
		replay(indicatorDAO);
		
				
		PivotChartGenerator generator = new PivotChartGenerator(dispatcher, indicatorDAO);
		generator.generate(new User(), element, new Filter(), new DateRange());		
	}
	
	
}
