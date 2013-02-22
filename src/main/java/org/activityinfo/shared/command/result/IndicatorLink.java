package org.activityinfo.shared.command.result;

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

public class IndicatorLink implements Serializable {
	private int sourceDatabaseId;
	private int sourceIndicatorId;
	private int destinationDatabaseId;
	private int destinationIndicatorId;
	
	public IndicatorLink() {
		super();
	}

	public int getSourceDatabaseId() {
		return sourceDatabaseId;
	}

	public void setSourceDatabaseId(int sourceDatabaseId) {
		this.sourceDatabaseId = sourceDatabaseId;
	}

	public int getSourceIndicatorId() {
		return sourceIndicatorId;
	}

	public void setSourceIndicatorId(int sourceIndicatorId) {
		this.sourceIndicatorId = sourceIndicatorId;
	}

	public int getDestinationDatabaseId() {
		return destinationDatabaseId;
	}

	public void setDestinationDatabaseId(int destinationDatabaseId) {
		this.destinationDatabaseId = destinationDatabaseId;
	}

	public int getDestinationIndicatorId() {
		return destinationIndicatorId;
	}

	public void setDestinationIndicatorId(int destinationIndicatorId) {
		this.destinationIndicatorId = destinationIndicatorId;
	}
	
	
	
}
