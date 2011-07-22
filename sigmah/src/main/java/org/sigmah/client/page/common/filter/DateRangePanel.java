/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.page.common.filter;

import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.FieldEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Html;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.form.DateField;
import com.extjs.gxt.ui.client.widget.form.LabelField;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.HasValue;

import org.sigmah.client.i18n.I18N;
import org.sigmah.client.icon.IconImageBundle;
import org.sigmah.shared.dao.Filter;

import java.util.Date;

/**
 * UI Component for selecting a range of dates to be used with
 * a {@link org.sigmah.shared.dao.Filter}
 *
 * @author Alex Bertram
 */
public class DateRangePanel extends ContentPanel implements HasValue<Filter>{
    private DateField datefieldMinDate;
    private DateField datefieldMaxDate;
    private Filter filter = new Filter();

    public DateRangePanel() {
        super();

//        setIcon(IconImageBundle.ICONS.filter());
//        setBodyStyle("padding:5px");

        add(new LabelField(I18N.CONSTANTS.fromDate()));

        datefieldMinDate = new DateField();
        add(datefieldMinDate);

        add(new LabelField(I18N.CONSTANTS.toDate()));

        datefieldMaxDate = new DateField();
        add(datefieldMaxDate);
        
        datefieldMinDate.addListener(Events.Change, new Listener<FieldEvent>() {
			@Override
			public void handleEvent(FieldEvent be) {
				filter.setMinDate(datefieldMinDate.getValue());
				
				// Only fire a ValueChangeEvent when there's a complete range (min+max both non-null)
				if (filter.getMaxDate() != null) {
					ValueChangeEvent.fire(DateRangePanel.this, filter);
				}
			}
		});
        datefieldMaxDate.addListener(Events.Change, new Listener<FieldEvent>() {
			@Override
			public void handleEvent(FieldEvent be) {
				filter.setMaxDate(datefieldMaxDate.getValue());
				
				// Only fire a ValueChangeEvent when there's a complete range (min+max both non-null)
				if (filter.getMinDate() != null) {
					ValueChangeEvent.fire(DateRangePanel.this, filter);
				}
			}
		});
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
}
