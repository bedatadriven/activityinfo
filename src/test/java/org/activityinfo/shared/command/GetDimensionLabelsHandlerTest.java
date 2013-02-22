package org.activityinfo.shared.command;

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


import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Map;

import org.activityinfo.server.command.CommandTestCase2;
import org.activityinfo.server.database.OnDataSet;
import org.activityinfo.server.database.TestDatabaseModule;
import org.activityinfo.shared.command.Filter;
import org.activityinfo.shared.command.GetDimensionLabels;
import org.activityinfo.shared.exception.CommandException;
import org.activityinfo.shared.report.model.DimensionType;
import org.activityinfo.test.InjectionSupport;
import org.activityinfo.test.Modules;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(InjectionSupport.class)
@Modules({TestDatabaseModule.class})
@OnDataSet("/dbunit/sites-simple1.db.xml")
public class GetDimensionLabelsHandlerTest extends CommandTestCase2 {

	@Test
	public void labelsTest() throws CommandException {
		Filter filter = new Filter();
		filter.addRestriction(DimensionType.Activity, Arrays.asList(1,2,3,4,5));
		filter.addRestriction(DimensionType.AdminLevel, Arrays.asList(1,2,3,4,5));
		filter.addRestriction(DimensionType.Partner, Arrays.asList(1,2,3,4,5));
		filter.addRestriction(DimensionType.Project, Arrays.asList(1,2,3,4,5));
		filter.addRestriction(DimensionType.Database, Arrays.asList(1,2,3,4,5));
		filter.addRestriction(DimensionType.Indicator, Arrays.asList(1,2,3,4,5));
		
		for (DimensionType dimension : filter.getRestrictedDimensions()) {
			Map<Integer, String> labels = execute(filter, dimension);
			 
		
			// We don't make assumptions about the data, only about that there should be data (working query)
			assertTrue("Expected at least one label for entity " + dimension.toString(), labels.size() != 0);
		}
	}

	private Map<Integer, String> execute(Filter filter, DimensionType dimension) throws CommandException {
		return execute(new GetDimensionLabels(dimension, filter.getRestrictions(dimension))).getLabels();	
	}
}
