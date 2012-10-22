package org.activityinfo.shared.command;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.activityinfo.server.command.CommandTestCase2;
import org.activityinfo.server.database.OnDataSet;
import org.activityinfo.shared.command.handler.search.AllSearcher;
import org.activityinfo.shared.command.handler.search.Searcher;
import org.activityinfo.shared.command.result.SearchResult;
import org.activityinfo.shared.exception.CommandException;
import org.activityinfo.shared.report.model.DimensionType;
import org.activityinfo.test.InjectionSupport;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(InjectionSupport.class)
@OnDataSet("/dbunit/sites-simple1.db.xml")
public class SearchTest extends CommandTestCase2 {

	
	@Test
	@Ignore
	public void testSearchAll() throws CommandException {
		SearchResult result = execute(new Search("kivu"));

		assertTrue("Expected all searchers to succeed", result.getFailedSearchers().isEmpty());
		
		for (Searcher searcher : AllSearcher.supportedSearchers()) {
			assertHasDimension(searcher.getDimensionType(), result);
		}
		
		assertHasRestrictionWithIds(DimensionType.AdminLevel, result, 2, 3);
		assertHasRestrictionWithIds(DimensionType.Partner, result, 3);
		assertHasRestrictionWithIds(DimensionType.Project, result, 4);
		assertHasRestrictionWithIds(DimensionType.AttributeGroup, result, 3);
		assertHasRestrictionWithIds(DimensionType.Indicator, result, 675);
		assertHasRestrictionWithIds(DimensionType.Location, result, 1);
	}
	
	@Test
	@Ignore("still not working")
	public void testBadSyntax() {
		SearchResult result = execute(new Search("y:y"));
		
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

