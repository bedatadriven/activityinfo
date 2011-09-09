package org.sigmah.shared.search;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.sigmah.server.dao.OnDataSet;
import org.sigmah.server.endpoint.gwtrpc.CommandTestCase;
import org.sigmah.shared.command.Search;
import org.sigmah.shared.command.result.SearchResult;
import org.sigmah.shared.exception.CommandException;
import org.sigmah.shared.report.model.DimensionType;
import org.sigmah.test.InjectionSupport;

import com.bedatadriven.rebar.sql.client.SqlDatabase;
import com.bedatadriven.rebar.sql.client.SqlException;
import com.bedatadriven.rebar.sql.client.SqlTransaction;
import com.bedatadriven.rebar.sql.client.SqlTransactionCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;

@RunWith(InjectionSupport.class)
@OnDataSet("/dbunit/sites-simple1.db.xml")
public class SearchTest extends CommandTestCase {

	@Inject private SqlDatabase db;
	
	@Test
	public void testAttributeGroup() {
		db.transaction(new SqlTransactionCallback() {
			@Override
			public void begin(SqlTransaction tx) {
				new AttributeGroupSearcher().search(Arrays.asList("cause"), tx, new AsyncCallback<List<Integer>>() {
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
	public void testSearchAll() throws CommandException {
		SearchResult result = execute(new Search("kivu"));

		assertTrue("Expected all searchers to succeed", result.getFailedSearchers().isEmpty());
		
		for (Searcher<?> searcher : AllSearcher.supportedSearchers()) {
			assertHasDimension(searcher.getDimensionType(), result);
		}
		
		assertHasRestrictionWithIds(DimensionType.AdminLevel, result, 2, 3);
		assertHasRestrictionWithIds(DimensionType.Partner, result, 3);
		assertHasRestrictionWithIds(DimensionType.Project, result, 3);
		assertHasRestrictionWithIds(DimensionType.AttributeGroup, result, 3);
		assertHasRestrictionWithIds(DimensionType.Indicator, result, 675);
		assertHasRestrictionWithIds(DimensionType.Location, result, 1);
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
				
				fail(String.format("Expected restriction with id=%s for DimensionType %s", 
						Integer.toString(id), type.toString()));
			}
		}
	}
	
}

