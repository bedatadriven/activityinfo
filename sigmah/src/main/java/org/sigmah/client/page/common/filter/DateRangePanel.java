/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.page.common.filter;

import java.util.Date;

import org.sigmah.client.i18n.I18N;
import org.sigmah.client.icon.IconImageBundle;
import org.sigmah.client.page.common.filter.FilterToolBar.ApplyFilterEvent;
import org.sigmah.client.page.common.filter.FilterToolBar.ApplyFilterHandler;
import org.sigmah.client.page.common.filter.FilterToolBar.RemoveFilterEvent;
import org.sigmah.client.page.common.filter.FilterToolBar.RemoveFilterHandler;
import org.sigmah.shared.command.Filter;

import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.FieldEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.form.DateField;
import com.extjs.gxt.ui.client.widget.form.LabelField;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.HasValue;

/**
 * UI Component for selecting a range of dates to be used with
 * a {@link org.sigmah.shared.command.Filter}
 *
 * @author Alex Bertram
 */
public class DateRangePanel extends ContentPanel implements HasValue<Filter>, FilterPanel {
    private DateField datefieldMinDate;
    private DateField datefieldMaxDate;
    
    private FilterToolBar filterToolBar;

    public DateRangePanel() {
        super();
        
        initializeComponent();

        createFilterToolbar();
        createFromDateField();
        createToDateField();
    }

	private void createFilterToolbar() {
		filterToolBar = new FilterToolBar();
		filterToolBar.addApplyFilterHandler(new ApplyFilterHandler() {
			@Override
			public void onApplyFilter(ApplyFilterEvent deleteEvent) {
				applyFilter();
			}
		});

		filterToolBar.addRemoveFilterHandler(new RemoveFilterHandler() {
			@Override
			public void onRemoveFilter(RemoveFilterEvent deleteEvent) {
				removeFilter();
			}
		});
		filterToolBar.setApplyFilterEnabled(true);
		setTopComponent((Component) filterToolBar);
	}

	protected void applyFilter() {
		Filter value = getValue();
		ValueChangeEvent.fire(this, value);
		filterToolBar.setRemoveFilterEnabled(value.isDateRestricted());
	}

	protected void removeFilter() {
		datefieldMinDate.setValue(null);
		datefieldMaxDate.setValue(null);
		filterToolBar.setRemoveFilterEnabled(false);
		ValueChangeEvent.fire(this, getValue());
	}

	private void createToDateField() {
		add(new LabelField(I18N.CONSTANTS.toDate()));

        datefieldMaxDate = new DateField();
        add(datefieldMaxDate);
	}

	private void createFromDateField() {
		add(new LabelField(I18N.CONSTANTS.fromDate()));

        datefieldMinDate = new DateField();
        add(datefieldMinDate);
	}

	private void initializeComponent() {
		setHeading(I18N.CONSTANTS.filterByDate());
        setIcon(IconImageBundle.ICONS.filter());
	}

    /**
     * Updates the given filter with the user's choice.
     *
     * @param filter  the filter to update
     */
    public void updateFilter(Filter filter) {
    }

	@Override
	public HandlerRegistration addValueChangeHandler(
			ValueChangeHandler<Filter> handler) {
		return addHandler(handler, ValueChangeEvent.getType());
	}

	@Override
	public Filter getValue() {
		Filter filter = new Filter();
        filter.setMinDate(datefieldMinDate.getValue());
        filter.setMaxDate(datefieldMaxDate.getValue());
        
        return filter;
	}

	@Override
	public void setValue(Filter value) {
		setValue(value, false);
	}

	@Override
	public void setValue(Filter value, boolean fireEvents) {
		datefieldMinDate.setValue(value.getMinDate());
		datefieldMaxDate.setValue(value.getMaxDate());
		
		filterToolBar.setRemoveFilterEnabled(value.isDateRestricted());
		
		if(fireEvents) {
			ValueChangeEvent.fire(this, getValue());
		}
	}

	public Date getMinDate() {
		return datefieldMinDate.getValue();
	}

	public Date getMaxDate() {
		return datefieldMaxDate.getValue();
	}

	public void setMinDate(Date date){
		datefieldMinDate.setValue(date);
	}
	
	public void setMaxDate(Date date){
		datefieldMaxDate.setValue(date);
	}
	
	@Override
	public void applyBaseFilter(Filter filter) {
		
	}
}
