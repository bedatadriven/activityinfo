package org.sigmah.server.database.hibernate.entity;

import java.io.Serializable;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class IndicatorLink implements Serializable, ReallyDeleteable {

	private IndicatorLinkId id;
	private Indicator sourceIndicator;
	private Indicator destinationIndicator;

	public IndicatorLink() {
		super();
	}

	@EmbeddedId
	@AttributeOverrides( {
		@AttributeOverride(name = "sourceIndicator", column = @Column(name = "sourceIndicator", nullable = false)),
		@AttributeOverride(name = "destinationIndicator", column = @Column(name = "destinationIndicator", nullable = false)) })
	
	public IndicatorLinkId getId() {
		return id;
	}

	public void setId(IndicatorLinkId id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "IndicatorId", nullable = false, insertable = false, updatable = false)
	public Indicator getSourceIndicator() {
		return sourceIndicator;
	}

	public void setSourceIndicator(Indicator sourceIndicator) {
		this.sourceIndicator = sourceIndicator;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "IndicatorId", nullable = false, insertable = false, updatable = false)
	public Indicator getDestinationIndicator() {
		return destinationIndicator;
	}

	public void setDestinationIndicator(Indicator destinationIndicator) {
		this.destinationIndicator = destinationIndicator;
	}

	@Override
	public void deleteReferences() {
		// TODO Auto-generated method stub
	}

}
