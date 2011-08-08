package org.sigmah.shared.domain;

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

@Entity
public class LockedPeriod implements Serializable {
	private Date fromDate;
	private Date toDate;
	private String name;
	private int id;
	private UserDatabase userDatabase;
	private Project project;
	private Activity activity;
	
	@Column(name = "FromDate", nullable = false)
	public Date getFromDate() {
		return fromDate;
	}
	
	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}
	
	@Column(name = "ToDate", nullable = false)
	public Date getToDate() {
		return toDate;
	}
	
	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}
	
	@Column(name = "Name", nullable = false)
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name = "LockedPeriodId", unique = true, nullable = false)
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "UserDatabaseId", nullable = true)
	public UserDatabase getUserDatabase() {
		return userDatabase;
	}

	public void setUserDatabase(UserDatabase userDatabase) {
		this.userDatabase = userDatabase;
	}

	public void setProject(Project project) {
		this.project = project;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ProjectId", nullable = true)
	public Project getProject() {
		return project;
	}

	public void setActivity(Activity activity) {
		this.activity = activity;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ActivityId", nullable = true)
	public Activity getActivity() {
		return activity;
	}
	
}
