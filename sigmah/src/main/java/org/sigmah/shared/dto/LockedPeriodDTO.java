package org.sigmah.shared.dto;

import java.util.Date;

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

	public LockedPeriodDTO(int id, Date startDate, Date endDate) {
		setId(id);
		setFromDate(startDate);
		setToDate(endDate);
		setEnabled(true);
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
		set("toDate", toDate);
	}
	
	public Date getToDate() {
		return (Date) get("toDate");
	}
	
	public void setFromDate(Date fromDate) {
		set("fromDate", fromDate);
	}
	
	public Date getFromDate() {
		return (Date) get("fromDate");
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
		this.activity = activity;
	}

	public ActivityDTO getActivity() {
		return activity;
	}
	
	public UserDatabaseDTO getUserDatabase() {
		return userDatabase;
	}

	public void setUserDatabase(UserDatabaseDTO userDatabase) {
		this.userDatabase = userDatabase;
	}

	public ProjectDTO getProject() {
		return project;
	}

	public void setProject(ProjectDTO project) {
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
		if (getProject() != null) {
			return getProject().getName();
		}
		if (getActivity() != null) {
			return getActivity().getName();
		}
		if (getUserDatabase() != null) {
			return getUserDatabase().getName();
		}
		
		return null;
	}
	
	public boolean fallsWithinPeriod(Date date) {
		Date from = getFromDate();
		Date to = getToDate();
		
		boolean isSameAsFrom = from.compareTo(date) == 0;
		boolean isSameAsTo = to.compareTo(date) == 0;
		boolean isBetween = from.before(date) && to.after(date);
		
		return isBetween || isSameAsFrom || isSameAsTo;
	}
}
