package org.sigmah.shared.search;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.sigmah.server.dao.OnDataSet;
import org.sigmah.test.InjectionSupport;
import org.sigmah.test.MockHibernateModule;
import org.sigmah.test.Modules;

import com.bedatadriven.rebar.sql.server.jdbc.JdbcDatabase;
import com.google.gwt.user.client.rpc.AsyncCallback;

@RunWith(InjectionSupport.class)
@Modules({MockHibernateModule.class})
@OnDataSet("/dbunit/sites-simple1.db.xml")
public class SearchTest {

	private String testQuery = "kivu";
	private String dbFile = getClass().getResource("/dbunit/sites-simple.sqlite").getFile();
	private JdbcDatabase db = new JdbcDatabase(dbFile);

	
	@Test
	public void testString() {
		
//		
//		Filter filter = searcher.search(testQuery);
//		
//		assertTrue(filter.getRestrictions(DimensionType.AdminLevel).contains(3)); 
	}
	
	@Test
	public void testStringAsync() {
		AdminEntitySearcher sa = new AdminEntitySearcher(db);
		sa.search(testQuery, new AsyncCallback<List<Integer>>() {
			
			@Override
			public void onSuccess(List<Integer> result) {
				assertTrue("Expected adminlevel with id=3",result.contains(3)); 
			}
			
			@Override
			public void onFailure(Throwable caught) {
				throw new AssertionError(caught);
			}
		}, null);
	}
	
	@Test
	public void testSearchAll() {
//		AllSearcher allSearcher = new AllSearcher(db);
//		allSearcher.searchAll("kivu");
//		
//		assertTrue("expected 1 partner, 2 adminlevels", 
//				allSearcher.getFilter().getRestrictedDimensions()
//					.contains(DimensionType.AdminLevel));
//		
//		assertTrue("expected adminlevels with id=2 and id=3", 
//				allSearcher.getFilter().getRestrictions(DimensionType.AdminLevel)
//					.contains(2) &&
//				allSearcher.getFilter().getRestrictions(DimensionType.AdminLevel)
//					.contains(3));
//					
//		assertTrue("expected 1 partner, 2 adminlevels", 
//				allSearcher.getFilter().getRestrictedDimensions()
//					.contains(DimensionType.Partner));
//		
//		assertTrue("expected partner with id=3", 
//				allSearcher.getFilter().getRestrictions(DimensionType.Partner)
//					.contains(3));
		
	}
}
