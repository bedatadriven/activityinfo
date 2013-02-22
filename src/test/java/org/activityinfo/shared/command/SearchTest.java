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
import static org.junit.Assert.fail;

import java.util.logging.Level;
import java.util.logging.Logger;

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
		
		Logger.getLogger("com.bedatadriven.rebar").setLevel(Level.ALL);
		
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

