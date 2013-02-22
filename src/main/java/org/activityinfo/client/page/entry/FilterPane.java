package org.activityinfo.client.page.entry;

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

import org.activityinfo.client.dispatch.Dispatcher;
import org.activityinfo.client.filter.AdminFilterPanel;
import org.activityinfo.client.filter.DateRangePanel;
import org.activityinfo.client.filter.FilterPanelSet;
import org.activityinfo.client.filter.PartnerFilterPanel;
import org.activityinfo.client.i18n.I18N;

import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.layout.AccordionLayout;

public class FilterPane extends ContentPanel {

    private final FilterPanelSet filterPanelSet;

    public FilterPane(Dispatcher dispatcher) {
        setHeading(I18N.CONSTANTS.filter());
        setLayout(new AccordionLayout());

        ActivityFilterPanel activityFilterPanel = new ActivityFilterPanel(
            dispatcher);
        AdminFilterPanel adminFilterPanel = new AdminFilterPanel(dispatcher);
        DateRangePanel datePanel = new DateRangePanel();
        PartnerFilterPanel partnerPanel = new PartnerFilterPanel(dispatcher);

        add(activityFilterPanel);
        add(adminFilterPanel);
        add(datePanel);
        add(partnerPanel);

        filterPanelSet = new FilterPanelSet(activityFilterPanel,
            adminFilterPanel, datePanel, partnerPanel);

    }

    public FilterPanelSet getSet() {
        return filterPanelSet;
    }

}
