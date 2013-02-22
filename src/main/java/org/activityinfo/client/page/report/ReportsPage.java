package org.activityinfo.client.page.report;

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
import org.activityinfo.client.page.NavigationCallback;
import org.activityinfo.client.page.Page;
import org.activityinfo.client.page.PageId;
import org.activityinfo.client.page.PageState;
import org.activityinfo.client.page.report.json.ReportSerializer;

import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.google.inject.Inject;

/**
 * Page which presents the list of reports visible to the user
 * 
 * @author Alex Bertram
 */
public class ReportsPage extends LayoutContainer implements Page {
    public static final PageId PAGE_ID = new PageId("reports");

    @Inject
    public ReportsPage(EventBus eventBus, Dispatcher dispatcher,
        ReportSerializer reportSerializer) {

        setLayout(new BorderLayout());

        BorderLayoutData newLayout = new BorderLayoutData(LayoutRegion.EAST);
        newLayout.setSize(0.40f);
        newLayout.setMargins(new Margins(0, 5, 0, 0));
        newLayout.setSplit(true);
        newLayout.setCollapsible(true);
        add(new NewReportPanel(eventBus, dispatcher, reportSerializer),
            newLayout);

        add(new ReportGridPanel(eventBus, dispatcher), new BorderLayoutData(
            LayoutRegion.CENTER));

    }

    @Override
    public void shutdown() {

    }

    @Override
    public PageId getPageId() {
        return PAGE_ID;
    }

    @Override
    public Object getWidget() {
        return this;
    }

    @Override
    public void requestToNavigateAway(PageState place,
        NavigationCallback callback) {
        callback.onDecided(true);
    }

    @Override
    public String beforeWindowCloses() {
        return null;
    }

    @Override
    public boolean navigate(PageState place) {
        return true;
    }

}
