package org.sigmah.shared.command.handler.search;

import java.io.Serializable;

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