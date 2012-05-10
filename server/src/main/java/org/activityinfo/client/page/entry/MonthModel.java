package org.activityinfo.client.page.entry;

import org.activityinfo.client.util.date.DateUtilGWTImpl;
import org.activityinfo.shared.report.content.MonthCategory;
import org.activityinfo.shared.report.model.DateRange;

import com.extjs.gxt.ui.client.data.BaseModelData;

final class MonthModel extends BaseModelData {

	private final MonthCategory category;

	public MonthModel(MonthCategory category) {
		this.category = category;
		set("name", category.getLabel());
	}

	public DateRange getDateRange() {
		return DateUtilGWTImpl.INSTANCE.monthRange(category.getYear(), category.getMonth());
	}

	public String getKey() {
		return "Y" + category.getYear() + "M" + category.getMonth();
	}
	
}
