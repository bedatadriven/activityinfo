package org.sigmah.shared.search;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.sigmah.server.dao.OnDataSet;
import org.sigmah.server.endpoint.gwtrpc.handler.GenerateElementHandler;
import org.sigmah.server.endpoint.gwtrpc.handler.SearchHandler;
import org.sigmah.shared.command.Search;
import org.sigmah.shared.command.handler.CommandContext;
import org.sigmah.shared.command.result.SearchResult;
import org.sigmah.shared.domain.User;
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

	private String testQuery = "kivu";
	private String dbFile = getClass().getResource("/dbunit/sites-simple.sqlite").getFile();
	private JdbcDatabase db = new JdbcDatabase(dbFile);
	private GenerateElementHandler getPivotData;
	private SearchHandler handler;
	private CommandContext context = new CommandContext() {
		User user = new User();
		
		@Override
		public User getUser() {
			user.setId(1);
			return user;
		}
	};
	private GenerateElementHandler genelhandler;

    @Inject
    public SearchTest(GenerateElementHandler genElHandler) {
    	this.genelhandler = genElHandler;
    }
	
	
	@Test
	public void testString() {
		
//		
//		Filter filter = searcher.search(testQuery);
//		
//		assertTrue(filter.getRestrictions(DimensionType.AdminLevel).contains(3)); 
	}
	
	@Test
	public void testStringAsync() {
		AdminEntitySearcher sa = new AdminEntitySearcher();
		sa.search(testQuery, null, new AsyncCallback<List<Integer>>() {
			
			@Override
			public void onSuccess(List<Integer> result) {
				assertTrue("Expected adminlevel with id=3",result.contains(3)); 
			}
			
			@Override
			public void onFailure(Throwable caught) {
				throw new AssertionError(caught);
			}
		});
	}
	
	@Test
	public void testSearchAll() {
		SearchHandler handler = new SearchHandler(db, null, genelhandler);
		
		handler.execute(new Search("kivu"), context, new AsyncCallback<SearchResult>() {
			
			@Override
			public void onSuccess(SearchResult result) {
				assertTrue("Expected all searchers to succeed", result.getFailedSearchers().isEmpty());
				
				assertTrue("2 adminlevels", 
						result.getPivotTabelData().getEffectiveFilter().getRestrictedDimensions()
							.contains(DimensionType.AdminLevel));
				
				assertTrue("expected adminlevels with id=2 and id=3", 
						result.getPivotTabelData().getEffectiveFilter().getRestrictions(DimensionType.AdminLevel)
							.contains(2) &&
							result.getPivotTabelData().getEffectiveFilter().getRestrictions(DimensionType.AdminLevel)
							.contains(3));
							
				assertTrue("expected 1 partner", 
						result.getPivotTabelData().getEffectiveFilter().getRestrictedDimensions()
							.contains(DimensionType.Partner));
				
				assertTrue("expected partner with id=3", 
						result.getPivotTabelData().getEffectiveFilter().getRestrictions(DimensionType.Partner)
							.contains(3));
			}
			
			@Override
			public void onFailure(Throwable caught) {
				assertTrue("Expected searchresult", false);
			}
		});
		

	}
}
