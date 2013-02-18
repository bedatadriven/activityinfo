package org.activityinfo.shared.command;

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
