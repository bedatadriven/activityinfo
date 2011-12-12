package org.sigmah.server.database.hibernate.entity;

import javax.persistence.Column;

public class IndicatorLinkId implements java.io.Serializable {

	private int sourceIndicatorId;
	private int destinationIndicatorId;

	public IndicatorLinkId() {

	}

	public IndicatorLinkId(int sourceIndicatorId, int destinationIndicatorId) {
		this.sourceIndicatorId = sourceIndicatorId;
		this.destinationIndicatorId = destinationIndicatorId;
	}

	@Column(name = "SourceIndicatorId", nullable = false)
	public int getSourceIndicatorId() {
		return sourceIndicatorId;
	}

	public void setSourceIndicatorId(int sourceIndicatorId) {
		this.sourceIndicatorId = sourceIndicatorId;
	}

	@Column(name = "DestinationIndicatorId", nullable = false)
	public int getDestinationIndicatorId() {
		return destinationIndicatorId;
	}

	public void setDestinationIndicatorId(int destinationIndicatorId) {
		this.destinationIndicatorId = destinationIndicatorId;
	}

}
