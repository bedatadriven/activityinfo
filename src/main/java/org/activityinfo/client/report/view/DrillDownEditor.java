

package org.activityinfo.client.report.view;

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

import org.activityinfo.client.AppEvents;
import org.activityinfo.client.EventBus;
import org.activityinfo.client.dispatch.Dispatcher;
import org.activityinfo.client.event.PivotCellEvent;
import org.activityinfo.client.page.common.Shutdownable;
import org.activityinfo.client.page.entry.SiteGridPanel;
import org.activityinfo.client.page.entry.grouping.NullGroupingModel;
import org.activityinfo.client.util.state.StateProvider;
import org.activityinfo.shared.command.Filter;
import org.activityinfo.shared.report.content.EntityCategory;
import org.activityinfo.shared.report.content.PivotTableData;
import org.activityinfo.shared.report.model.DimensionType;
import org.activityinfo.shared.util.date.DateUtil;

import com.extjs.gxt.ui.client.event.Listener;

public class DrillDownEditor implements Shutdownable {
  
    private final EventBus eventBus;
    private final DateUtil dateUtil;
    private Listener<PivotCellEvent> eventListener;
    private SiteGridPanel gridPanel;

    public DrillDownEditor(EventBus eventBus, Dispatcher service, StateProvider stateMgr, DateUtil dateUtil) {

        this.eventBus = eventBus;
        this.dateUtil = dateUtil;
        this.gridPanel = new SiteGridPanel(service);

        eventListener = new Listener<PivotCellEvent>() {
            @Override
			public void handleEvent(PivotCellEvent be) {
                onDrillDown(be);
            }
        };
        eventBus.addListener(AppEvents.DRILL_DOWN, eventListener);
    }

    @Override
    public void shutdown() {
        eventBus.removeListener(AppEvents.DRILL_DOWN, eventListener);
    } 

    public void onDrillDown(PivotCellEvent event) {

        // construct our filter from the intersection of rows and columns
        Filter filter = new Filter(filterFromAxis(event.getRow()), filterFromAxis(event.getColumn()));

        // apply the effective filter
        final Filter effectiveFilter = new Filter(filter, event.getElement().getContent().getEffectiveFilter());

        // determine the indicator
        final int indicatorId = effectiveFilter.getRestrictions(DimensionType.Indicator).iterator().next();
        effectiveFilter.clearRestrictions(DimensionType.Indicator);

        gridPanel.load(NullGroupingModel.INSTANCE, effectiveFilter);
    }

    private Filter filterFromAxis(PivotTableData.Axis axis) {

        Filter filter = new Filter();
        while (axis != null) {
            if (axis.getDimension() != null) {
                if (axis.getDimension().getType() == DimensionType.Date) {
                    filter.setDateRange(dateUtil.rangeFromCategory(axis.getCategory()));
                } else if (axis.getCategory() instanceof EntityCategory) {
                    filter.addRestriction(axis.getDimension().getType(), ((EntityCategory) axis.getCategory()).getId());
                }
            }
            axis = axis.getParent();
        }
        return filter;
    }

	public SiteGridPanel getGridPanel() {
		return gridPanel;
	}
}


