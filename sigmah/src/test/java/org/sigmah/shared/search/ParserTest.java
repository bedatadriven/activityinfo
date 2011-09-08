package org.sigmah.shared.search;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.sigmah.server.auth.impl.PasswordHelper;
import org.sigmah.shared.search.QueryParser.Dimension;

public class ParserTest {
	private final List<String> okQueries = new ArrayList<String>() {{ // new fancy i-dont-like collection initer
			add("location:\"kivu1\" project:\"kivu2 kivu3\"");
			add("location:\"kivu1\" project:kivu2 kivu3");
			add("location:\"kivu1\" project:kivu2 kivu3 partner:kivu4 project:\"kivu5\"");
			add("location:kivu1");
			//"location:\":::\"",  <-- that one is nasty with the current parser impl setup
			add("partner:some");
	}};
	
	private final String[] evilQueries =  { 
			"location::kivu",
			"location:\"\"",
			"location",
			"location\"\"",
			"location:",
			"location:",
			"location:" 
	};
	
	@Test
	public void testParser() {
		assertAllOkQueriesDontFail();
		//assertAllEvilQueriesFail();
		
		ParserTester.test(okQueries.get(0))
						   .andAssertContainsDimensions("location", "project")
						   .andAssertDimension("location").hasSearchTerms("kivu1")
						   .andAssertDimension("project").hasSearchTerms("kivu2 kivu3");

		ParserTester.test(okQueries.get(1))
						   .andAssertContainsDimensions("location", "project")
						   .andAssertDimension("location").hasSearchTerms("kivu1")
						   .andAssertDimension("project").hasSearchTerms("kivu2 kivu3");

		ParserTester.test(okQueries.get(2))
						   .andAssertContainsDimensions("location", "project")
						   .andAssertDimension("location").hasSearchTerms("kivu1")
						   .andAssertDimension("project").hasSearchTerms("kivu2 kivu3", "kivu5")
						   .andAssertDimension("partner").hasSearchTerms("kivu4");

		ParserTester.test(okQueries.get(3))
						   .andAssertContainsDimensions("location")
						   .andAssertDimension("location").hasSearchTerms("kivu1");

		ParserTester.test(okQueries.get(4))
						   .andAssertContainsDimensions("partner")
						   .andAssertDimension("partner").hasSearchTerms("some");
		System.out.println(PasswordHelper.hashPassword("woei"));
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
		private String dimension;
		
		public ParserTester(QueryParser parser) {
			this.parser = parser;
		}
		public static ParserTester test(String query) {
			QueryParser parser = new QueryParser();
			parser.parse(query);
			return new ParserTester(parser);
		}
		public ParserTester andAssertContainsDimensions(String... dimensionsToCheck) {
			for (String dimension : dimensionsToCheck) {
				if (!parser.getUniqueDimensions().containsKey(dimension))
					fail();
			}
			return this;
		}
		public ParserTester andAssertDimension(String dimension) {
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
