package org.sigmah.shared.search;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.sigmah.server.dao.OnDataSet;
import org.sigmah.shared.dao.Filter;
import org.sigmah.shared.report.model.DimensionType;
import org.sigmah.test.InjectionSupport;
import org.sigmah.test.MockHibernateModule;
import org.sigmah.test.Modules;

import com.bedatadriven.rebar.sql.server.jdbc.JdbcDatabase;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;

@RunWith(InjectionSupport.class)
@Modules({MockHibernateModule.class})
@OnDataSet("/dbunit/sites-simple1.db.xml")
public class SearchTest {

	@Inject
	private SearcherImpl searcher;
	private String testQuery = "kivu";

	
	@Test
	public void testString() {
		
//		
//		Filter filter = searcher.search(testQuery);
//		
//		assertTrue(filter.getRestrictions(DimensionType.AdminLevel).contains(3)); 
	}
	
	@Test
	public void testStringAsync() {
		String dbFile = getClass().getResource("/dbunit/sites-simple.sqlite").getFile();
		JdbcDatabase db = new JdbcDatabase(dbFile);
		
		SearcherAsync sa = new SearcherAsync(db);
		sa.search(testQuery, new AsyncCallback<Filter>() {
			
			@Override
			public void onSuccess(Filter result) {
				assertTrue("Expected adminlevel with id=3",result.getRestrictions(DimensionType.AdminLevel).contains(3)); 
			}
			
			@Override
			public void onFailure(Throwable caught) {
				throw new AssertionError(caught);
			}
		});
	}
}
