/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.domain;

// Generated Apr 9, 2009 7:58:20 AM by Hibernate Tools 3.2.2.GA

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

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
