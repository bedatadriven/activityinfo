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
