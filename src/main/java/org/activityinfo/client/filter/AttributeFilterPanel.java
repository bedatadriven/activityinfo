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

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.activityinfo.client.dispatch.Dispatcher;
import org.activityinfo.client.filter.FilterToolBar.ApplyFilterEvent;
import org.activityinfo.client.filter.FilterToolBar.ApplyFilterHandler;
import org.activityinfo.client.filter.FilterToolBar.RemoveFilterEvent;
import org.activityinfo.client.filter.FilterToolBar.RemoveFilterHandler;
import org.activityinfo.client.i18n.I18N;
import org.activityinfo.client.icon.IconImageBundle;
import org.activityinfo.shared.command.Filter;
import org.activityinfo.shared.command.GetAttributeGroupsWithSites;
import org.activityinfo.shared.command.GetSchema;
import org.activityinfo.shared.command.result.AttributeGroupResult;
import org.activityinfo.shared.dto.AttributeGroupDTO;
import org.activityinfo.shared.dto.SchemaDTO;
import org.activityinfo.shared.report.model.DimensionType;
import org.activityinfo.shared.util.CollectionUtil;

import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.Style.Orientation;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;

public class AttributeFilterPanel extends ContentPanel implements FilterPanel {
    private final Dispatcher service;
    private FilterToolBar filterToolBar;

    private Filter value = new Filter();
    private List<AttributeGroupDTO> groups = new ArrayList<AttributeGroupDTO>();
    private List<AttributeGroupFilterWidget> widgets = new ArrayList<AttributeGroupFilterWidget>();

    @Inject
    public AttributeFilterPanel(Dispatcher service) {
        this.service = service;

        setLayout(new RowLayout(Orientation.VERTICAL));
        setScrollMode(Style.Scroll.AUTO);
        setHeading(I18N.CONSTANTS.filterByAttribute());
        setIcon(IconImageBundle.ICONS.filter());

        createFilterToolBar();
    }

    private void createFilterToolBar() {
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
        setTopComponent(filterToolBar);
    }

    FilterToolBar getFilterToolBar() {
        return filterToolBar;
    }

    @Override
    public void applyBaseFilter(Filter rawFilter) {
        System.out.println("*** APPLYING BASEFILTER: " + rawFilter);
        final Filter filter = new Filter(rawFilter);
        filter.clearRestrictions(DimensionType.Attribute);

        service.execute(new GetSchema(), new AsyncCallback<SchemaDTO>() {
            @Override
            public void onFailure(Throwable caught) {
                GWT.log("Failed to load attributes", caught);
            }

            @Override
            public void onSuccess(final SchemaDTO schema) {
                // retrieve all attributegroups for the current filter
                service.execute(new GetAttributeGroupsWithSites(filter), new AsyncCallback<AttributeGroupResult>() {
                    @Override
                    public void onFailure(Throwable caught) {
                    }

                    @Override
                    public void onSuccess(AttributeGroupResult result) {
                        // clean up old widgets
                        for (AttributeGroupFilterWidget widget : widgets) {
                            remove(widget);
                        }

                        // decorate resultlist from schema
                        List<AttributeGroupDTO> pivotData = result.getData();
                        groups = new ArrayList<AttributeGroupDTO>();
                        if (CollectionUtil.isNotEmpty(pivotData)) {
                            for (AttributeGroupDTO pivotGroup : pivotData) {
                                AttributeGroupDTO schemaGroup = schema.getAttributeGroupById(pivotGroup.getId());
                                if (schemaGroup != null) {
                                    groups.add(schemaGroup);
                                }
                            }
                        }

                        // create new widgets, one for each attributegroup
                        List<Integer> selection = getSelectedIds();

                        widgets = new ArrayList<AttributeGroupFilterWidget>();
                        for (AttributeGroupDTO group : groups) {
                            // create
                            AttributeGroupFilterWidget widget =
                                new AttributeGroupFilterWidget(AttributeFilterPanel.this, group);

                            // set old selection
                            widget.setSelection(selection);

                            // add widget to panel
                            widgets.add(widget);
                            add(widget);
                        }
                    }
                });
            }
        });
    }

    protected void removeFilter() {
        value = new Filter();
        for (AttributeGroupFilterWidget widget : widgets) {
            widget.clear();
        }

        ValueChangeEvent.fire(this, value);

        filterToolBar.setApplyFilterEnabled(false);
        filterToolBar.setRemoveFilterEnabled(false);
    }

    private void applyFilter() {
        value = new Filter();
        if (isRendered()) {
            List<Integer> selectedIds = getSelectedIds();
            if (selectedIds.size() > 0) {
                value.addRestriction(DimensionType.Attribute, getSelectedIds());
            }
        }

        ValueChangeEvent.fire(this, value);

        filterToolBar.setApplyFilterEnabled(false);
        filterToolBar.setRemoveFilterEnabled(true);
    }

    private List<Integer> getSelectedIds() {
        List<Integer> list = new ArrayList<Integer>();
        for (AttributeGroupFilterWidget widget : widgets) {
            Set<Integer> selection = widget.getValue().getRestrictions(DimensionType.Attribute);
            if (CollectionUtil.isNotEmpty(selection)) {
                list.addAll(selection);
            }
        }
        return list;
    }

    @Override
    public Filter getValue() {
        return value;
    }

    @Override
    public void setValue(Filter value) {
        setValue(value, false);
    }

    @Override
    public void setValue(Filter value, boolean fireEvents) {
        this.value = new Filter();
        this.value.addRestriction(DimensionType.Attribute, value.getRestrictions(DimensionType.Attribute));
        applyInternalValue();
        if (fireEvents) {
            ValueChangeEvent.fire(this, value);
        }
    }

    private void applyInternalValue() {
        if (getValue().isRestricted(DimensionType.Attribute)) {
            for (AttributeGroupFilterWidget widget : widgets) {
                widget.setSelection(getValue().getRestrictions(DimensionType.Attribute));
            }
        }
        filterToolBar.setApplyFilterEnabled(false);
        filterToolBar.setRemoveFilterEnabled(getValue().isRestricted(DimensionType.Attribute));
    }

    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<Filter> handler) {
        return addHandler(handler, ValueChangeEvent.getType());
    }
}
