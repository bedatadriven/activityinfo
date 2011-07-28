package org.sigmah.shared.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Project2 {
	private int id;
	private String name;
	private int siteId;
	private String description;
	private Date dateDeleted;
	
	public Project2() {
		super();
	}

	public Project2(int id, String name, int siteId) {
		super();
		this.id = id;
		this.name = name;
		this.siteId = siteId;
	}

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name = "ProjectId", unique = true, nullable = false)
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	@Column(nullable=false, length=30)
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public int getSiteId() {
		return siteId;
	}
	
	public void setSiteId(int siteId) {
		this.siteId = siteId;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(length=250, nullable=true)
	public String getDescription() {
		return description;
	}

	public void setDateDeleted(Date dateDeleted) {
		this.dateDeleted = dateDeleted;
	}

	public Date getDateDeleted() {
		return dateDeleted;
	}
}