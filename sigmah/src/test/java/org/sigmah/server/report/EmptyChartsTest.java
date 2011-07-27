package org.sigmah.server.report;

import static org.easymock.EasyMock.anyInt;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import org.junit.Test;
import org.sigmah.server.dao.PivotDAO;
import org.sigmah.server.report.generator.PivotChartGenerator;
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
	
		PivotDAO pivotDAO = createMock(PivotDAO.class);
		expect(pivotDAO.aggregate(anyInt(), isA(Filter.class), (Set<Dimension>)isA(Set.class)))
			.andReturn(Collections.EMPTY_LIST);
		expect(pivotDAO.getFilterLabels(eq(DimensionType.Indicator), (java.util.Collection<Integer>)isA(Collection.class)))
			.andReturn(Collections.EMPTY_LIST);

		replay(pivotDAO);
		
		IndicatorDAO indicatorDAO = createMock(IndicatorDAO.class);
		expect(indicatorDAO.findById(eq(1))).andReturn(new Indicator());
		replay(indicatorDAO);
		
				
		PivotChartGenerator generator = new PivotChartGenerator(pivotDAO, indicatorDAO);
		generator.generate(new User(), element, new Filter(), new DateRange());		
	}
	
	
}
