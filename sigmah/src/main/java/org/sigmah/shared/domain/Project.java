package org.sigmah.shared.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Project implements Serializable {
	private int id;
	private String name;
	private String description;
	private Date dateDeleted;
	private UserDatabase database;
    private Set<LockedPeriod> lockedPeriods = new HashSet<LockedPeriod>();

	public Project() {
		super();
	}

	public Project(int id, String name) {
		super();
		this.id = id;
		this.name = name;
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

	public void setDatabase(UserDatabase database) {
		this.database = database;
	}
	
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DatabaseId", nullable = false)
	public UserDatabase getDatabase() {
		return database;
	}

	public void setLockedPeriods(Set<LockedPeriod> lockedPeriods) {
		this.lockedPeriods = lockedPeriods;
	}

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "project")
    @JoinColumn(name = "ProjectId", nullable = false)
	public Set<LockedPeriod> getLockedPeriods() {
		return lockedPeriods;
	}

}