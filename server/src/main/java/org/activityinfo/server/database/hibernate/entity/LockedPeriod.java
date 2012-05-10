package org.activityinfo.server.database.hibernate.entity;

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

import com.bedatadriven.rebar.time.calendar.LocalDate;


@Entity
public class LockedPeriod implements Serializable, ReallyDeleteable {
	private Date fromDate;
	private Date toDate;
	private String name;
	private int id;
	private UserDatabase userDatabase;
	private Project project;
	private Activity activity;
	private boolean enabled;
	
	@Column(nullable = false)
	@Temporal(TemporalType.DATE)
	public Date getFromDate() {
		return fromDate;
	}
	
	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}
	
	public void setFromDate(LocalDate fromDate) {
		this.fromDate = fromDate.atMidnightInMyTimezone();
	}
	
	@Column(nullable = false)
	@Temporal(TemporalType.DATE)
	public Date getToDate() {
		return toDate;
	}
	
	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}
	
	public void setToDate(LocalDate toDate) {
		this.toDate = toDate.atMidnightInMyTimezone();
	}
	
	@Column(nullable = false)
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

	@ManyToOne(fetch = FetchType.EAGER )
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
	
	@ManyToOne(fetch = FetchType.EAGER )
	@JoinColumn(name = "ProjectId", nullable = true)
	public Project getProject() {
		return project;
	}

	public void setActivity(Activity activity) {
		this.activity = activity;
	}
	
	@ManyToOne(fetch = FetchType.EAGER )
	@JoinColumn(name = "ActivityId", nullable = true)
	public Activity getActivity() {
		return activity;
	}

	public void setEnabled(boolean isEnabled) {
		this.enabled = isEnabled;
	}

	public boolean isEnabled() {
		return enabled;
	}
	
	@Transient
	public UserDatabase getParentDatabase() {
		if (userDatabase != null) {
			return userDatabase;
		} else if (activity != null) {
			return activity.getDatabase();
		} else if (project != null) {
			return project.getUserDatabase();
		}
		
		return null;
	}

	@Override
	public void deleteReferences() {
		if (activity != null) {
			activity.getLockedPeriods().remove(this);
		}
		if (userDatabase != null) {
			userDatabase.getLockedPeriods().remove(this);
		}
		if (project!=null) {
			project.getLockedPeriods().remove(this);
		}
		this.activity=null;
		this.userDatabase=null;
		this.project=null;
	}

}
