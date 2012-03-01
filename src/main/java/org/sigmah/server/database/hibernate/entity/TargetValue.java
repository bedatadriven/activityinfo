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
public class TargetValue implements Serializable, ReallyDeleteable{

	private TargetValueId id;
	private Target target;
	private Indicator indicator;
	private Double value;
	
	
	public TargetValue() {
		super();
	}
		
	@EmbeddedId
	@AttributeOverrides( {
		@AttributeOverride(name = "targetId", column = @Column(name = "targetId", nullable = false)),
		@AttributeOverride(name = "IndicatorId", column = @Column(name = "IndicatorId", nullable = false)) })
	
	public TargetValueId getId() {
		return this.id;
	}

	public void setId(TargetValueId id) {
		this.id = id;
	}
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "targetId", nullable = false, insertable = false, updatable = false)
	public Target getTarget() {
		return target;
	}

	public void setTarget(Target target) {
		this.target = target;
	}

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "IndicatorId" , nullable = false, insertable = false, updatable = false)
	public Indicator getIndicator() {
		return indicator;
	}

	public void setIndicator(Indicator indicator) {
		this.indicator = indicator;
	}

	public Double getValue() {
		return value;
	}

	public void setValue(Double value) {
		this.value = value;
	}

	@Override
	public void deleteReferences() {
		// TODO delete referneces if any		
	}

}
