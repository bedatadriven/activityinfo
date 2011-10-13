package org.sigmah.client.page.map.layerOptions;

import org.sigmah.client.i18n.I18N;
import org.sigmah.shared.dao.Filter;
import org.sigmah.shared.report.model.DateRange;

import com.google.gwt.user.client.Event;

public class DateFilterWidget extends FilterWidget {

	private DateFilterMenu menu;
	
	public DateFilterWidget() {
		dimensionSpan.setInnerHTML(I18N.CONSTANTS.dates());
		stateSpan.setInnerText(I18N.CONSTANTS.allDates());
	}
	
	public void updateView() {
		if(value.getMinDate() == null && value.getMaxDate() == null) {
			setState(I18N.CONSTANTS.allDates());
		} else if(value.getMinDate()==null) {
			setState(FilterResources.MESSAGES.beforeDate(value.getMaxDate()));
		} else if(value.getMaxDate()==null) {
			setState(FilterResources.MESSAGES.afterDate(value.getMinDate()));
		} else {
			setState(FilterResources.MESSAGES.betweenDates(value.getMinDate(), value.getMaxDate()));
		}
	}

	@Override
	public void choose(Event event) {
		if(menu == null) {
			menu = new DateFilterMenu();
		}
		menu.showAt(event.getClientX(), event.getClientY(), new SelectionCallback<DateRange>() {
			
			@Override
			public void onSelected(DateRange selection) {
				Filter filter = new Filter(value);
				filter.setDateRange(selection);
				
				setValue(filter);
			}
		});
	}


}
