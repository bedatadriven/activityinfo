package org.sigmah.shared.search;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.sigmah.database.ClientDatabaseStubs;
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

import com.bedatadriven.rebar.sql.client.SqlException;
import com.bedatadriven.rebar.sql.client.SqlTransaction;
import com.bedatadriven.rebar.sql.client.SqlTransactionCallback;
import com.bedatadriven.rebar.sql.server.jdbc.JdbcDatabase;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;

@RunWith(InjectionSupport.class)
@Modules({MockHibernateModule.class})
@OnDataSet("/dbunit/sites-simple1.db.xml")
public class SearchTest {

	private String testQuery = "kivu";
	private JdbcDatabase db = ClientDatabaseStubs.sitesSimple();
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
	public void testAttributeGroup() {
		db.transaction(new SqlTransactionCallback() {
			@Override
			public void begin(SqlTransaction tx) {
				new AttributeGroupSearcher().search("cause", tx, new AsyncCallback<List<Integer>>() {
					@Override
					public void onFailure(Throwable caught) {
						assertTrue("Did not expect error when searching attribute groups", false);
					}

					@Override
					public void onSuccess(List<Integer> result) {
						assertTrue("Expected one attribute group with name like 'cause'", result.size()==1);
					}
				});
			}

			@Override
			public void onError(SqlException e) {
				assertTrue("Did not expect error when having db transaction", false); 
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
				
				//assertHasDimension(DimensionType.Site, result);
				assertHasDimension(DimensionType.Partner, result);
				assertHasDimension(DimensionType.AdminLevel, result);
				assertHasDimension(DimensionType.Project, result);
				assertHasDimension(DimensionType.AttributeGroup, result);
				assertHasDimension(DimensionType.Indicator, result);
				
				assertHasRestrictionWithIds(DimensionType.AdminLevel, result, 2, 3);
				assertHasRestrictionWithIds(DimensionType.Partner, result, 3);
				assertHasRestrictionWithIds(DimensionType.Project, result, 3);
				//assertHasRestrictionWithIds(DimensionType.Site, result, 9);
				assertHasRestrictionWithIds(DimensionType.AttributeGroup, result, 3);
				assertHasRestrictionWithIds(DimensionType.Indicator, result, 675);
			}
			
			@Override
			public void onFailure(Throwable caught) { 
				assertTrue("Expected searchresult", false);
			}
		});
		
	}
	
	public static void assertHasDimension(DimensionType type, SearchResult result) {
		if (!result.getPivotTabelData().getEffectiveFilter().isRestricted(type)) {
			fail("expected DimensionType \"" + type.toString() + "\" in filter of searchresult");
		}
	}
	
	private void assertHasRestrictionWithIds(DimensionType type, SearchResult result, int... ids) {
		for (int id : ids) {
			if (!result.getPivotTabelData().getEffectiveFilter()
					.getRestrictions(type).contains(id)) {
				
				fail(String.format("Expected restriction with id={0} for DimensionType {1}", 
						Integer.toString(id), type.toString()));
			}
		}
	}
	
}





