/*
 * 
 *  This file is part of ActivityInfo.
 *
 *  ActivityInfo is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  ActivityInfo is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with ActivityInfo.  If not, see <http://www.gnu.org/licenses/>.
 *  
 *  Copyright 2009 Alex Bertram
 *  
 */

package org.activityinfo.server.domain;

// Generated Apr 9, 2009 7:58:20 AM by Hibernate Tools 3.2.2.GA

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.Version;

/**
 * 
 * @author Alex Bertram
 *
 */
@Entity
public class Attribute implements Serializable, Deleteable, Orderable, SchemaElement {

	private int id;
	private AttributeGroup group;
	private String name;
	private int sortOrder;
	private int version;
	private Date dateDeleted;
	
	public Attribute() {
		
	}

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name = "AttributeId", unique = true, nullable = false)
	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	@Version
	@Column(nullable=false)
	public int getVersion() {
		return this.version;
	}
	
	protected void setVersion(int version) {
		this.version = version;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "AttributeGroupId", nullable = false)
	public AttributeGroup getGroup() {
		return this.group;
	}

	public void setGroup(AttributeGroup group) {
		this.group = group;
	}

	@Column(name = "Name", nullable = false, length = 50)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "SortOrder", nullable = false)
	public int getSortOrder() {
		return this.sortOrder;
	}

	public void setSortOrder(int sortOrder) {
		this.sortOrder = sortOrder;
	}
	
	@Column
	@Temporal(value = TemporalType.TIMESTAMP)
	public Date getDateDeleted() {
		return this.dateDeleted;
	}
	
	public void setDateDeleted(Date date) {
		this.dateDeleted = date;
	}
	
	public void delete() {
		setDateDeleted(new Date());
	}

	@Override
	@Transient
	public boolean isDeleted() {
		return getDateDeleted() == null;
	}

}
