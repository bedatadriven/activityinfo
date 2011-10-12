package org.sigmah.client.page.map.layerOptions;

import org.sigmah.client.i18n.I18N;
import org.sigmah.client.icon.IconImageBundle;
import org.sigmah.shared.report.model.DateRange;

import com.extjs.gxt.ui.client.event.MenuEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.util.DateWrapper;
import com.extjs.gxt.ui.client.widget.menu.Menu;
import com.extjs.gxt.ui.client.widget.menu.MenuItem;
import com.extjs.gxt.ui.client.widget.menu.SeparatorMenuItem;

public class DateFilterMenu extends Menu {
	
	private SelectionCallback<DateRange> callback;
	private DateRangeDialog dialog;
	
	public DateFilterMenu() {

		addLastFourQuarters();
		add(new SeparatorMenuItem());
	
		addYearRange(0);
		addYearRange(1);
		add(new SeparatorMenuItem());
				
		add(new MenuItem(I18N.CONSTANTS.customDateRange(), new SelectionListener<MenuEvent>() {
			@Override
			public void componentSelected(MenuEvent ce) {
				showDateRangeDialog();
			}
		}));
		add(new SeparatorMenuItem());
		
		addRemoveDateFilter();
	}
	
	private void addRemoveDateFilter() {
		add(new MenuItem(I18N.CONSTANTS.remove(), IconImageBundle.ICONS.delete(), new SelectionListener<MenuEvent>() {
			@Override
			public void componentSelected(MenuEvent ce) {
				if(callback!=null) {
					callback.onSelected(null);
				}
			}
		}));
	}

	public void showAt(int x, int y, SelectionCallback<DateRange> callback) {
		this.callback = callback;
		showAt(x,y);
	}
	
	private void addYearRange(int yearsAgo) {
		int year = new DateWrapper().getFullYear() - yearsAgo;
		DateWrapper from = new DateWrapper(year, 0, 1);
		DateWrapper to = new DateWrapper(year, 11, 31);
		
		addFixedRange(Integer.toString(year), 
				new DateRange(from.asDate(), to.asDate()));
	}
	
	private void addLastFourQuarters() {
		DateWrapper today = new DateWrapper();
		int year = today.getFullYear();
		int quarter = today.getMonth() / 3;
		
		for(int i=0;i<4;++i) {
			quarter = quarter - 1;
			if(quarter < 0) {
				year = year - 1;
				quarter = 3;
			}
			addQuarterRange(year, quarter);
		}
		
	}
	
	private void addQuarterRange(int year, int quarter) {
		DateWrapper from = new DateWrapper(year, quarter*3, 1);
		DateWrapper to = from.addMonths(3).addDays(-1);
				
		addFixedRange(year + "Q" + (quarter+1), 
				new DateRange(from.asDate(), to.asDate()));
	}

	
	private void addFixedRange(String label, final DateRange dateRange) {
		add(new MenuItem(label, new SelectionListener<MenuEvent>() {

			@Override
			public void componentSelected(MenuEvent ce) {
				if(callback!=null) {
					callback.onSelected(dateRange);
				}
			}
		}));
	}

	protected void showDateRangeDialog() {
		if(dialog == null) {
			dialog = new DateRangeDialog();
		}
		dialog.show(callback);
	}

}
