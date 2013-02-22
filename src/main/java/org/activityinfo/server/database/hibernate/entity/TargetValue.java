package org.activityinfo.server.database.hibernate.entity;

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
