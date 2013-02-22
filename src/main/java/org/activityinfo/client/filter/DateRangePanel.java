package org.activityinfo.client.filter;

/*
 * #%L
 * ActivityInfo Server
 * %%
 * Copyright (C) 2009 - 2013 UNICEF
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import java.util.Date;

import org.activityinfo.client.filter.FilterToolBar.ApplyFilterEvent;
import org.activityinfo.client.filter.FilterToolBar.ApplyFilterHandler;
import org.activityinfo.client.filter.FilterToolBar.RemoveFilterEvent;
import org.activityinfo.client.filter.FilterToolBar.RemoveFilterHandler;
import org.activityinfo.client.i18n.I18N;
import org.activityinfo.client.icon.IconImageBundle;
import org.activityinfo.shared.command.Filter;

import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.form.DateField;
import com.extjs.gxt.ui.client.widget.form.LabelField;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.HasValue;

/**
 * UI Component for selecting a range of dates to be used with a
 * {@link org.activityinfo.shared.command.Filter}
 * 
 * @author Alex Bertram
 */
public class DateRangePanel extends ContentPanel implements HasValue<Filter>,
    FilterPanel {
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
        setTopComponent(filterToolBar);
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
     * @param filter
     *            the filter to update
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

        if (fireEvents) {
            ValueChangeEvent.fire(this, getValue());
        }
    }

    public Date getMinDate() {
        return datefieldMinDate.getValue();
    }

    public Date getMaxDate() {
        return datefieldMaxDate.getValue();
    }

    public void setMinDate(Date date) {
        datefieldMinDate.setValue(date);
    }

    public void setMaxDate(Date date) {
        datefieldMaxDate.setValue(date);
    }

    @Override
    public void applyBaseFilter(Filter filter) {

    }
}
