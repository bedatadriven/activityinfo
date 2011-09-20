package org.sigmah.shared.dto;

import java.util.Date;

import com.bedatadriven.rebar.time.calendar.LocalDate;
import com.extjs.gxt.ui.client.data.BaseModelData;

/*
 * A period where normal users cannot add, update or remove data 
 */
public class LockedPeriodDTO extends BaseModelData implements EntityDTO {
	private ActivityDTO activity;
	private UserDatabaseDTO userDatabase;
	private ProjectDTO project;
	
	public LockedPeriodDTO() {
		super();
	}
	
	public void setName(String name) {
		set("name", name);
	}
	
	public String getName() {
		return (String)get("name");
	}
	
	public void setId(int id) {
		set("id", id);
	}
	
	public int getId() {
		return (Integer)get("id");
	}
	
	public void setToDate(Date toDate) {
		set("toDate", new LocalDate(toDate));
	}
	
	public void setToDate(LocalDate toDate) {
		if(toDate == null) {
			set("toDate", null);
		} else {
			set("toDate", toDate);
		}
	}
	
	public LocalDate getToDate() {
		return get("toDate");
	}
	
	public void setFromDate(Date fromDate) {
		if(fromDate == null) {
			set("fromDate", null);
		} else {
			set("fromDate", new LocalDate(fromDate));
		}
	}
	
	public void setFromDate(LocalDate fromDate) {
		set("fromDate", fromDate);
	}
	
	public LocalDate getFromDate() {
		return get("fromDate");
	}

	/*
	 * Returns true when startDate is before end date, and both startDate and EndDate 
	 * are non-null.
	 */
	public boolean isValid() {
		return getFromDate() != null &&
		getToDate() != null &&
		getFromDate().before(getToDate());
	}

	public void setEnabled(boolean enabled) {
		set("enabled", enabled);
	}

	public boolean isEnabled() {
		return (Boolean)get("enabled");
	}

	@Override
	public String getEntityName() {
		return "LockedPeriod";
	}

	
	public void setActivity(ActivityDTO activity) {
		set("parentName", activity.getName());
		this.activity = activity;
	}

	public ActivityDTO getActivity() {
		return activity;
	}
	
	public UserDatabaseDTO getUserDatabase() {
		return userDatabase;
	}

	public void setUserDatabase(UserDatabaseDTO userDatabase) {
		set("parentName", userDatabase.getName());
		this.userDatabase = userDatabase;
	}

	public ProjectDTO getProject() {
		return project;
	}

	public void setProject(ProjectDTO project) {
		set("parentName", project.getName());
		this.project = project;
	}
	
	public int getParentId() {
		if (getActivity() != null) {
			return getActivity().getId();		
		}
		if (getProject() != null) {
			return getProject().getId();		
		}
		if (getUserDatabase() != null) {
			return getUserDatabase().getId();		
		}
		
		throw new RuntimeException("Expected a parent instance");
	}
	
	public String getParentName() {
		return (String)get("parentName");
	}
	
	public boolean fallsWithinPeriod(Date date) {
		return fallsWithinPeriod(new LocalDate(date));
	}
	
	public boolean fallsWithinPeriod(LocalDate date) {
		LocalDate from = getFromDate();
		LocalDate to = getToDate();
		
		boolean isSameAsFrom = from.compareTo(date) == 0;
		boolean isSameAsTo = to.compareTo(date) == 0;
		boolean isBetween = from.before(date) && to.after(date);
		
		return isBetween || isSameAsFrom || isSameAsTo;
	}
}
