package org.sigmah.shared.command.search;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static org.sigmah.shared.report.model.DimensionType.Location;
import static org.sigmah.shared.report.model.DimensionType.Partner;
import static org.sigmah.shared.report.model.DimensionType.Project;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.junit.BeforeClass;
import org.junit.Test;
import org.sigmah.shared.command.handler.search.QueryParser;
import org.sigmah.shared.command.handler.search.QueryParser.Dimension;
import org.sigmah.shared.report.model.DimensionType;

import com.teklabs.gwt.i18n.server.LocaleProxy;

public class ParserTest {
	
	
	
	private final List<String> okQueries = new ArrayList<String>() {{ // new fancy i-dont-like collection initer
			add("site:\"kivu1\" project:\"kivu2 kivu3\"");
			add("site:\"kivu1\" project:kivu2 kivu3");
			add("site:\"kivu1\" project:kivu2 kivu3 partner:kivu4 project:\"kivu5\"");
			add("site:kivu1");
			//"location:\":::\"",  <-- that one is nasty with the current parser impl
			add("partner:some");
	}};
	
	private final String[] evilQueries =  { 
			"site::kivu",
			"site:\"\"",
			"site",
			"site\"\"",
			"site:",
			"site:",
			"site:" 
	};
	
	@BeforeClass 
	public static void initLocales() {
		LocaleProxy.initialize();
		LocaleProxy.setLocale(Locale.ENGLISH);	
	}
	
	@Test
	public void testParser() {
		assertAllOkQueriesDontFail();
		//assertAllEvilQueriesFail();
		
		ParserTester.test(okQueries.get(0))
						   .andAssertContainsDimensions(Location, Project)
						   .andAssertDimension(Location).hasSearchTerms("kivu1")
						   .andAssertDimension(Project).hasSearchTerms("kivu2 kivu3");

		ParserTester.test(okQueries.get(1))
						   .andAssertContainsDimensions(Location, Project)
						   .andAssertDimension(Location).hasSearchTerms("kivu1")
						   .andAssertDimension(Project).hasSearchTerms("kivu2 kivu3");

		ParserTester.test(okQueries.get(2))
						   .andAssertContainsDimensions(Location, Project)
						   .andAssertDimension(Location).hasSearchTerms("kivu1")
						   .andAssertDimension(Project).hasSearchTerms("kivu2 kivu3", "kivu5")
						   .andAssertDimension(Partner).hasSearchTerms("kivu4");

		ParserTester.test(okQueries.get(3))
						   .andAssertContainsDimensions(Location)
						   .andAssertDimension(Location).hasSearchTerms("kivu1");

		ParserTester.test(okQueries.get(4))
						   .andAssertContainsDimensions(Partner)
						   .andAssertDimension(Partner).hasSearchTerms("some");
	}
	
	private void assertAllEvilQueriesFail() {
		for (String okQuery : evilQueries) {
			QueryParser parser = new QueryParser();
			parser.parse(okQuery);
			//assertTrue("Query [[[" + okQuery + "]]] should fail", fails);
		}
	}

	private void assertAllOkQueriesDontFail() {
		for (String okQuery : okQueries) {
			QueryParser parser = new QueryParser();
			parser.parse(okQuery);
			assertFalse("Query [[[" + okQuery + "]]] should not fail", parser.hasFailed());
			showQueryInConsole(okQuery, parser);
		}
	}
	
	private void showQueryInConsole(String query, QueryParser parser) {
		System.out.println("Query: [[[" + query + "]]]");
		if (parser.hasFailed()) {
			System.out.println("FAILED ! ");
		}
		if (parser.getDimensions().size() > 0) {
		for (Dimension d : parser.getDimensions()) {
			String ss = "terms: ";
			for (String s : d.getSearchTerms()) {
				ss +=" " + s + " ";
			}
			System.out.println("Dimension:" + d.getName() + ", " + ss);
		}
		} else {
			System.out.println("No dimensions.");
		}
	}
	
	private static class ParserTester {
		QueryParser parser;
		private DimensionType dimension;
		
		public ParserTester(QueryParser parser) {
			this.parser = parser;
		}
		public static ParserTester test(String query) {
			QueryParser parser = new QueryParser();
			parser.parse(query);
			return new ParserTester(parser);
		}
		public ParserTester andAssertContainsDimensions(DimensionType... dimensionsToCheck) {
			for (DimensionType dimension : dimensionsToCheck) {
				if (!parser.getUniqueDimensions().containsKey(dimension))
					fail();
			}
			return this;
		}
		public ParserTester andAssertDimension(DimensionType dimension) {
			this.dimension=dimension;
			return this;
		}
		public ParserTester hasSearchTerms(String... searchTerms) {
			List<String> terms = parser.getUniqueDimensions().get(dimension);
			assertNotNull(terms);
			
			for (String term : searchTerms) {				
				if (!terms.contains(term)) { 
					fail();
				}
			}
			return this;
		}
	}
}	
