package org.activityinfo.shared.command.result;

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
