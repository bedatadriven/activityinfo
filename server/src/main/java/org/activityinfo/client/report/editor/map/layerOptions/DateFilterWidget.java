package org.activityinfo.client.report.editor.map.layerOptions;

import org.activityinfo.client.i18n.I18N;
import org.activityinfo.shared.command.Filter;
import org.activityinfo.shared.report.model.DateRange;

import com.google.gwt.user.client.Event;

public class DateFilterWidget extends FilterWidget {

	private DateFilterMenu menu;
	
	public DateFilterWidget() {
		dimensionSpan.setInnerHTML(I18N.CONSTANTS.dates());
		stateSpan.setInnerText(I18N.CONSTANTS.allDates());
	}
	
	public void updateView() {
		if(getValue().getMinDate() == null && getValue().getMaxDate() == null) {
			setState(I18N.CONSTANTS.allDates());
		} else if(getValue().getMinDate()==null) {
			setState(FilterResources.MESSAGES.beforeDate(getValue().getMaxDate()));
		} else if(getValue().getMaxDate()==null) {
			setState(FilterResources.MESSAGES.afterDate(getValue().getMinDate()));
		} else {
			setState(FilterResources.MESSAGES.betweenDates(getValue().getMinDate(), getValue().getMaxDate()));
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
				Filter filter = new Filter(getValue());
				filter.setDateRange(selection);
				
				setValue(filter);
			}
		});
	}


}
