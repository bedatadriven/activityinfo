package org.sigmah.shared.dto;

import java.util.Date;

import com.extjs.gxt.ui.client.data.BaseModelData;

/*
 * A period where normal users cannot add, update or remove data 
 */
public class LockedPeriodDTO extends BaseModelData implements DTO {
	
	public LockedPeriodDTO() {
		super();
	}

	public LockedPeriodDTO(int id, Date startDate, Date endDate) {
		setId(id);
		setStartDate(startDate);
		setEndDate(endDate);
	}
	
	public void setId(int id) {
		set("id", id);
	}
	
	public int getId() {
		return get("id");
	}
	
	public void setEndDate(Date endDate) {
		set("endDate", endDate);
	}
	
	public Date getEndDate() {
		return (Date) get("endDate");
	}
	
	public void setStartDate(Date startDate) {
		set("startDate", startDate);
	}
	
	public Date getStartDate() {
		return (Date) get("startDate");
	}

	/*
	 * Returns true when startDate is before end date, and both startDate and EndDate 
	 * are non-null.
	 */
	public boolean isValid() {
		return getStartDate() != null &&
		getEndDate() != null &&
		getStartDate().before(getEndDate());
	}

}
