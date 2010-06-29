/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * 
 * @author Alex Bertram
 *
 */
@Entity
@org.hibernate.annotations.Filter(
		name="hideDeleted",
		condition="DateDeleted is null"
	)
public class AttributeGroup implements Serializable, Deleteable, Orderable, SchemaElement {

	private int id;

    private String name;
	private Set<Attribute> attributes = new HashSet<Attribute>(0);

	private Set<Activity> activities = new HashSet<Activity>(0);
	
	private int sortOrder;
	private boolean multipleAllowed;

	private String category;
	
	private Date dateDeleted;
	
	public AttributeGroup() {
		
	}
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="AttributeGroupId")
	public int getId() {
		return this.id;
	}
	
	private void setId(int id) {
		this.id = id;
	}

    @Column
	public String getName() {
		return this.name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	
	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinTable(name = "AttributeGroupInActivity", 
			joinColumns = { 
				@JoinColumn(name = "AttributeGroupId", nullable = false, updatable = false) },	
			inverseJoinColumns = { 
				@JoinColumn(name = "ActivityId", nullable = false, updatable = false) }) 
	public Set<Activity> getActivities() {
		return this.activities;
	}
	
	public void setActivities(Set<Activity> activities) {
		this.activities = activities;
	}
	
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "group")
	@org.hibernate.annotations.Fetch(org.hibernate.annotations.FetchMode.SUBSELECT)
	@org.hibernate.annotations.OrderBy(clause="SortOrder")	
	@org.hibernate.annotations.Filter(name="hideDeleted", condition="DateDeleted is null")
	public Set<Attribute> getAttributes() {
		return this.attributes;
	}
	
	public void setAttributes(Set<Attribute> attributes) {
		this.attributes = attributes;
	}
	
	@Column(nullable=false)
	public int getSortOrder() {
		return sortOrder;
	}
	
	public void setSortOrder(int order) {
		this.sortOrder = order;
	}

	@Column(nullable=false)
	public boolean isMultipleAllowed() {
		return multipleAllowed;
	}

	public void setMultipleAllowed(boolean allowed) {
		this.multipleAllowed = allowed;
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
		this.setDateDeleted(new Date());
	}

	@Column(name="category", length=50, nullable=true)
	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	@Override
	@Transient
	public boolean isDeleted() {
		return getDateDeleted() == null;
	}
}
