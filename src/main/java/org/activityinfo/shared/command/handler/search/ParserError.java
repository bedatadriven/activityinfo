package org.activityinfo.shared.command.handler.search;

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