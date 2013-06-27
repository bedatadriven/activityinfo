package org.activityinfo.client.report.editor.chart;

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

import org.activityinfo.client.EventBus;
import org.activityinfo.client.dispatch.Dispatcher;
import org.activityinfo.client.filter.AdminFilterPanel;
import org.activityinfo.client.filter.AttributeFilterPanel;
import org.activityinfo.client.filter.DateRangePanel;
import org.activityinfo.client.filter.FilterPanelSet;
import org.activityinfo.client.filter.IndicatorFilterPanel;
import org.activityinfo.client.filter.PartnerFilterPanel;
import org.activityinfo.client.i18n.I18N;
import org.activityinfo.client.page.report.HasReportElement;
import org.activityinfo.client.page.report.ReportChangeHandler;
import org.activityinfo.client.page.report.ReportEventHelper;
import org.activityinfo.shared.command.Filter;
import org.activityinfo.shared.report.model.PivotReportElement;

import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.layout.AccordionLayout;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;

public class PivotFilterPanel extends ContentPanel implements
    HasReportElement<PivotReportElement> {
    private final FilterPanelSet panelSet;
    private final ReportEventHelper events;

    private PivotReportElement model;

    public PivotFilterPanel(EventBus eventBus, Dispatcher dispatcher) {
        this.events = new ReportEventHelper(eventBus, this);

        setLayout(new AccordionLayout());
        setHeading(I18N.CONSTANTS.filter());

        IndicatorFilterPanel indicatorPanel = new IndicatorFilterPanel(dispatcher, true);
        indicatorPanel.setHeaderVisible(true);
        add(indicatorPanel);

        AdminFilterPanel adminFilterPanel = new AdminFilterPanel(dispatcher);
        add(adminFilterPanel);

        DateRangePanel dateFilterPanel = new DateRangePanel();
        add(dateFilterPanel);

        PartnerFilterPanel partnerFilterPanel = new PartnerFilterPanel(dispatcher);
        add(partnerFilterPanel);

        AttributeFilterPanel attributePanel = new AttributeFilterPanel(dispatcher);
        add(attributePanel);

        panelSet = new FilterPanelSet(indicatorPanel, adminFilterPanel,
            dateFilterPanel, partnerFilterPanel, attributePanel);
        panelSet.addValueChangeHandler(new ValueChangeHandler<Filter>() {
            @Override
            public void onValueChange(ValueChangeEvent<Filter> event) {
                model.setFilter(event.getValue());
                events.fireChange();
            }
        });

        events.listen(new ReportChangeHandler() {
            @Override
            public void onChanged() {
                panelSet.setValue(model.getFilter());
            }
        });
    }

    @Override
    public void bind(PivotReportElement model) {
        this.model = model;
        panelSet.setValue(model.getFilter(), false);
    }

    @Override
    public PivotReportElement getModel() {
        return model;
    }

    public void applyBaseFilter(Filter filter) {
        panelSet.applyBaseFilter(filter);
    }

    @Override
    public void disconnect() {
        events.disconnect();
    }
}
