package org.sigmah.server.endpoint.gwtrpc;

import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.sigmah.server.dao.OnDataSet;
import org.sigmah.server.dao.PivotDAO;
import org.sigmah.server.database.TestDatabaseModule;
import org.sigmah.server.util.BeanMappingModule;
import org.sigmah.server.util.logging.LoggingModule;
import org.sigmah.shared.dao.Filter;
import org.sigmah.shared.report.model.DimensionType;
import org.sigmah.test.InjectionSupport;
import org.sigmah.test.MockHibernateModule;
import org.sigmah.test.Modules;

import com.bedatadriven.rebar.sql.server.jdbc.JdbcScheduler;
import com.google.inject.Inject;

@RunWith(InjectionSupport.class)
@Modules({
	        MockHibernateModule.class,
	        TestDatabaseModule.class,
	        BeanMappingModule.class,
	        GwtRpcModule.class,
	        LoggingModule.class
})
@OnDataSet("/dbunit/sites-simple1.db.xml")
public class PivotTest {
	
	@Inject
	PivotDAO pivotdao;
	
	public PivotTest() {
	}

	/*
	 * Check if the generic query on name works in the getFilterLabels works
	 */
	@Test
	public void labelsTest() {
		Filter filter = new Filter();
		filter.addRestriction(DimensionType.Activity, Arrays.asList(1,2,3,4,5));
		filter.addRestriction(DimensionType.AdminLevel, Arrays.asList(1,2,3,4,5));
		filter.addRestriction(DimensionType.Partner, Arrays.asList(1,2,3,4,5));
		filter.addRestriction(DimensionType.Project, Arrays.asList(1,2,3,4,5));
		filter.addRestriction(DimensionType.Database, Arrays.asList(1,2,3,4,5));
		filter.addRestriction(DimensionType.Indicator, Arrays.asList(1,2,3,4,5));
		
		for (DimensionType dimension : filter.getRestrictedDimensions()) {
			Map<Integer, String> labels = pivotdao.getFilterLabels(dimension, filter.getRestrictions(dimension));
			 
			JdbcScheduler.get().assertAllFinished();
			
			// We don't make assumptions about the data, only about that there should be data (working query)
			assertTrue("Expected at least one label for entity " + dimension.toString(), labels.size() != 0);
		}
	}
}
