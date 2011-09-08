package org.sigmah.shared.search;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.sigmah.shared.dao.Filter;
import org.sigmah.shared.report.model.DimensionType;

/** Transforms a search query string into a Filter instance/ workable data structure
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
	private Map<String, List<String>> preciseDimensions = new HashMap<String, List<String>>();
	private List<ParserError> errors = new ArrayList<ParserError>();
	private String query;
	private boolean hasFailed=false;
	private boolean hasDimensions = false;
	
	public void parse(String query) {
		this.query=query;
		
		try {
			determineColonPositions();
			determineHaveDimensions();
			if (hasDimensions) {
				createDimensions();
				parseSearchTerms();
				makeDimensionsUnique();
				createPreciseDimensions();
				createFilter();
			}
		} catch (Exception e) {
			hasFailed=true;
		}
	}
	
	/** This can be done better */
	private boolean isSupportedDimension(DimensionType dimension) {
		return (dimension == DimensionType.Activity   ||
				dimension == DimensionType.AdminLevel ||
				dimension == DimensionType.Partner    ||
				dimension == DimensionType.Location   ||
				dimension == DimensionType.Project    ||
				dimension == DimensionType.Indicator  ||
				dimension == DimensionType.IndicatorCategory);
	}

	private void createFilter() {
		Filter filter = new Filter();
		
		for (String dimensionString : preciseDimensions.keySet()) {
			DimensionType dimension;
			try {
				
				dimension = fromString(dimensionString);
			} catch (Exception e) {
				continue; // Ruthlessly ignore nonparseable string
			}

			List<Integer> ids = new ArrayList<Integer>();
			for (String idString : preciseDimensions.get(dimensionString)) {
				try {
					int id = Integer.parseInt(idString);
					ids.add(id);
				} catch (Exception ex) {
					continue; // Ruthlessly ignore nonparseable string
				}
			}
			if (ids.size() > 0 && isSupportedDimension(dimension)) {
				filter.addRestriction(dimension, ids);
			}
		}
		
		this.filter=filter;
	}
	
	/** Transforms a localized dimension into a dimension we can use to parse using the DimensionType enum */
	// TODO: implement
	private String fromLocalizedDimension(String localizedDimension) {
		return localizedDimension;
	}

	private DimensionType fromString(String dimensionString) throws Exception {
		dimensionString = capitalizeFirstLetter(dimensionString.toLowerCase().trim());
		try {
			return Enum.valueOf(DimensionType.class, dimensionString);
		} catch (Exception ex) {
			throw ex;
		}
	}
	
	private String capitalizeFirstLetter(String string) {
		char[] stringArray = string.toCharArray();
		stringArray[0] = Character.toUpperCase(stringArray[0]);
		return new String(stringArray);
	}

	private void createPreciseDimensions() {
		for (String dimension : uniqueDimensions.keySet()) {
			if (dimension.toLowerCase().endsWith("id")) {
				preciseDimensions.put(dimension, uniqueDimensions.get(dimension));
			}
		}
	}

	public boolean hasDimensions() {
		return hasDimensions;
	}

	private void determineHaveDimensions() {
		hasDimensions = colonPositions.size() > 0;
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

	public Filter getFilterOfSpecifiedIds() {
		return filter;
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
	 * 
	 * Assumes every character before a colon is part of the name of a dimension,
	 * unless a quote, space is encountered or start of string is reached
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
	
	public interface ParserError extends Serializable {
		public String description();
		public String fixTip();
		public ParseErrorSeverity severity();
		
		public enum ParseErrorSeverity {
			Info,
			Warning,
			Error // --> We should not have errors to bother a user with
		}
		
		/** Can't transform a dimension string into a DimensionType enum instance */
		public class DimensionError implements ParserError {
			@Override public String description() {
				return null;
			}

			@Override
			public String fixTip() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public ParseErrorSeverity severity() {
				// TODO Auto-generated method stub
				return null;
			}
		}
		
		/** When i.e. "locationid:15" is specified, but "15" is not parseable as an integer */
		public class IdSearchTermError implements ParserError {
			private String actualSearchTerm; 
			
			public IdSearchTermError() { }
			public IdSearchTermError(String actualSearchTerm) {
				super();
				this.actualSearchTerm = actualSearchTerm;
			}
			@Override public String description() {
				return null;
			}
			public String getActualSearchTerm() {
				return actualSearchTerm;
			}
			@Override public String fixTip() {
				// TODO Auto-generated method stub
				return null;
			}
			@Override public ParseErrorSeverity severity() {
				// TODO Auto-generated method stub
				return null;
			}
		}
		
		public class UnsupportedDimension implements ParserError {
			@Override
			public String description() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public String fixTip() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public ParseErrorSeverity severity() {
				// TODO Auto-generated method stub
				return null;
			}
		}
		
	}
}
