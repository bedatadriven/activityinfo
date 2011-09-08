package org.sigmah.shared.search;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.sigmah.shared.dao.Filter;
import org.sigmah.shared.report.model.DimensionType;

/** Transforms a search query string into a Filter instance.
 * See related test class for OK/non OK queries
 */
public class QueryParser {
	private static final String colon = ":";
	private static final String quote = "\"";
	private static final String space = " ";
	private static final String comma = ",";
	private boolean hasEntities;
	private Filter filter = new Filter();
	private List<Integer> colonPositions = new ArrayList<Integer>();
	private List<Dimension> dimensions = new ArrayList<Dimension>();
	private Map<String, List<String>> uniqueDimensions = new HashMap<String, List<String>>();
	private String query;
	private boolean hasFailed=false;
	
	public void parse(String query) {
		this.query=query;
		
		try {
			determineColonPositions();
			createDimensions();
			parseSearchTerms();
			makeDimensionsUnique();
		} catch (Exception e) {
			hasFailed=true;
		}
	}

	private void makeDimensionsUnique() {
		for (Dimension d : dimensions) {
			if (uniqueDimensions.containsKey(d.getName())) {
				uniqueDimensions.get(d.getName()).addAll(d.getSearchTerms());
			} else {
				uniqueDimensions.put(d.name, d.getSearchTerms());
			}
		}
	}

	public List<Dimension> getDimensions() {
		return dimensions;
	}
	
	public Map<String, List<String>> getUniqueDimensions() {
		return uniqueDimensions;
	}

	public boolean hasFailed() {
		return hasFailed;
	}

	private void parseSearchTerms() {
		int dimensionCount= dimensions.size();
		for (int dimensionIndex =0; dimensionIndex < dimensionCount; dimensionIndex++) {
			Dimension dimension = dimensions.get(dimensionIndex);
			int endPosition = 
				dimensionIndex == dimensionCount - 1 ? 
						query.length() : 
						dimensions.get(dimensionIndex+1).getStartPosition();

			int startPosSearchTerm = dimension.getEndPosition() + 1; // exclude the colon position
			int startPosCurrentSearchTerm=startPosSearchTerm;
			//boolean isQuoted = query.substring(startPosSearchTerm, startPosSearchTerm + 1) == quote;
			StringBuilder sb = new StringBuilder();
			boolean isQuoted = false;
			// Parse text between the colon and the starting position of the next dimension
			for (int pos = startPosSearchTerm; pos < endPosition; pos++) {
				String character = query.substring(pos, pos + 1);
				if (character.equals(comma)) { // add 
					dimension.addSearchTerm(sb.toString());
					sb = new StringBuilder();
					startPosCurrentSearchTerm = pos;
					continue;
				} else if (character.equals(quote)) {
					if (pos==startPosCurrentSearchTerm) { // opening quote
						isQuoted = true;
						startPosCurrentSearchTerm++;
						continue;
					} else if (isQuoted) { //closing quote
						dimension.addSearchTerm(sb.toString());
						sb = new StringBuilder();
						isQuoted=false; //reset for next term
						continue;
					}
				} else {
					sb.append(character);
				}
				if (!isQuoted && pos == (endPosition - 1)) {
					dimension.addSearchTerm(sb.toString());
				}
			}
		}
	}

	/**
	 * Using the list of positions of the colons, it parses dimension names and
	 * creates a dimension and adds it to a list of dimensions
	 */
	private void createDimensions() {
		for (Integer colonPosition: colonPositions) {
			Dimension dimension = new Dimension();
			dimension.setEndPosition(colonPosition);
			boolean startNotFound = true;
			while (startNotFound) {
				for (int pos = colonPosition-1; pos > -1; pos--) {
					String lookBehind = query.substring(pos, pos + 1);
					// Quote/space/start of string means detection of begincharacter dimensiontype
					if (lookBehind.equals(quote) || lookBehind.equals(space) || pos == 0) {
						dimension.startPosition = pos;
						startNotFound = false;
						break;
					}
				}
			}
			dimension.setName(query.substring(dimension.getStartPosition(), dimension.getEndPosition()));
//			dimension.setDimensionType(getDimensionType(dimension.getName()));
			dimensions.add(dimension);
		}
	}

	private DimensionType getDimensionType(String name) {
		return Enum.valueOf(DimensionType.class, name);
	}

	/**
	 * creates a list of positions in the searchterm where a colon occurs
	 */
	private void determineColonPositions() {
		int position=0;
		while (query.indexOf(colon, position) != -1) {
			colonPositions.add(query.indexOf(colon, position));
			position=query.indexOf(colon, position) + 1;
		}
	}

	/** Simple struct to keep tome info together */
	public static class Dimension {
		private int startPosition; // of the dimension name
		private int endPosition;   // of the disbmension name
		private String name;
		private DimensionType dimensionType;
		private List<String> searchTerms = new ArrayList<String>();
		public int getStartPosition() {
			return startPosition;
		}
		public void setStartPosition(int startPosition) {
			this.startPosition = startPosition;
		}
		public int getEndPosition() {
			return endPosition;
		}
		public void setEndPosition(int endPosition) {
			this.endPosition = endPosition;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name.trim().toLowerCase();
		}
		public DimensionType getDimensionType() {
			return dimensionType;
		}
		public void setDimensionType(DimensionType dimensionType) {
			this.dimensionType = dimensionType;
		}
		public List<String> getSearchTerms() {
			return searchTerms;
		}
		public void setSearchTerms(List<String> searchTerms) {
			this.searchTerms = searchTerms;
		}
		public void addSearchTerm(String dimensionName) {
			searchTerms.add(dimensionName.trim().toLowerCase());
		}
	}
}
