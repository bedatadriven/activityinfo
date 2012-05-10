package org.activityinfo.client.page.entry;

import org.activityinfo.client.util.date.DateUtilGWTImpl;
import org.activityinfo.shared.report.content.YearCategory;
import org.activityinfo.shared.report.model.DateRange;

import com.extjs.gxt.ui.client.data.BaseModelData;

final class YearModel extends BaseModelData {

	private YearCategory category;
	

	public YearModel(YearCategory category) {
		this.category = category;
		set("name", category.getLabel());
	}
	
	
	/**
	 * Further restricts an existing filter to the year range defined by this 
	 * model.
	 */
	public DateRange getDateRange() {
		return DateUtilGWTImpl.INSTANCE.yearRange(category.getYear());
	}


	public String getKey() {
		return "Y" + category.getYear();
	}
	
	
}
