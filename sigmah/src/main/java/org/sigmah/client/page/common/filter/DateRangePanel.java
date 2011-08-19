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
import org.sigmah.shared.dao.Filter;

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
 * a {@link org.sigmah.shared.dao.Filter}
 *
 * @author Alex Bertram
 */
public class DateRangePanel extends ContentPanel implements HasValue<Filter>, FilterPanel {
    private DateField datefieldMinDate;
    private DateField datefieldMaxDate;
    private Filter filter = new Filter();
    
    private FilterToolBar filterToolBar;

    public DateRangePanel() {
        super();
        
        initializeComponent();

        createFilterToolbar();
        createFromDateField();
        createToDateField();
    }

	private void createFilterToolbar() {
		filterToolBar = new FilterToolBarImpl();
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
		setTopComponent((Component) filterToolBar);
	}

	protected void applyFilter() {
		ValueChangeEvent.fire(this, filter);
		filterToolBar.setRemoveFilterEnabled(true);
	}

	protected void removeFilter() {
		
	}

	private void createToDateField() {
		add(new LabelField(I18N.CONSTANTS.toDate()));

        datefieldMaxDate = new DateField();
        datefieldMaxDate.addListener(Events.Change, new Listener<FieldEvent>() {
			@Override
			public void handleEvent(FieldEvent be) {
				filter.setMaxDate(datefieldMaxDate.getValue());
				filterToolBar.setApplyFilterEnabled(filter.getMinDate() != null);
			}
		});
        add(datefieldMaxDate);
	}

	private void createFromDateField() {
		add(new LabelField(I18N.CONSTANTS.fromDate()));

        datefieldMinDate = new DateField();
        datefieldMinDate.addListener(Events.Change, new Listener<FieldEvent>() {
			@Override
			public void handleEvent(FieldEvent be) {
				filter.setMinDate(datefieldMinDate.getValue());
				filterToolBar.setApplyFilterEnabled(filter.getMaxDate() != null);
			}
		});
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
        filter.setMinDate(datefieldMinDate.getValue());
        filter.setMaxDate(datefieldMaxDate.getValue());
    }

	@Override
	public HandlerRegistration addValueChangeHandler(
			ValueChangeHandler<Filter> handler) {
		return addHandler(handler, ValueChangeEvent.getType());
	}

	@Override
	public Filter getValue() {
		return filter;
	}

	@Override
	public void setValue(Filter value) {
		this.filter=value;
	}

	@Override
	public void setValue(Filter value, boolean fireEvents) {
		// TODO Auto-generated method stub
		
	}

	public Date getMinDate() {
		return datefieldMinDate.getValue();
	}

	public Date getMaxDate() {
		return datefieldMaxDate.getValue();
	}

	@Override
	public void applyBaseFilter(Filter filter) {
		filter.setMaxDate(getMaxDate());
		filter.setMinDate(getMinDate());
	}
}
