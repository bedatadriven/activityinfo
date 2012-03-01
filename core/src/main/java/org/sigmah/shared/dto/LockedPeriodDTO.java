package org.sigmah.shared.dto;

import java.util.Date;
import java.util.Set;

import com.bedatadriven.rebar.time.calendar.LocalDate;
import com.extjs.gxt.ui.client.data.BaseModelData;

/** A period where 'normal users' cannot add, update or remove data */
public class LockedPeriodDTO extends BaseModelData implements EntityDTO {
	
	/** An object supporting locks */
	public interface HasLockedPeriod extends EntityDTO {
		public Set<LockedPeriodDTO> getLockedPeriods();
		
		// This should be an instance method of LockedPeriodsList
		public Set<LockedPeriodDTO> getEnabledLockedPeriods();
	}
	
	private HasLockedPeriod parent;
	private Integer parentId;
	
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

	/** Returns true when startDate is before end date, and both startDate and EndDate  are non-null. */
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
	
	public HasLockedPeriod getParent() {
		return parent;
	}
	public void setParent(HasLockedPeriod hasLock) {
		this.parent = hasLock;
		this.parentId = hasLock.getId();
		set("parentName", hasLock.getName());
		set("parentType", hasLock.getEntityName());
	}
	
	public boolean hasParent() {
		return parent != null;
	}
	
	public boolean hasParentId() {
		return parentId != 0;
	}
	
	public void setParentId(int id) {
		this.parentId=id;
	}
	
	public String getParentName() {
		return (String)get("parentName");
	}
	
	public int getParentId() {
		return parentId;
	}
	
	public String getParentType() {
		return (String)get("parentType");
	}

	/** give meaning to the parentId by specifying the type of the parent. */
	public void setParentType(String type) {
		set("parentType", type);
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
