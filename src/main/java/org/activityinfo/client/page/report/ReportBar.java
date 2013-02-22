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

import org.activityinfo.client.i18n.I18N;
import org.activityinfo.client.icon.IconImageBundle;
import org.activityinfo.client.page.common.toolbar.ExportMenuButton;
import org.activityinfo.client.page.report.resources.ReportResources;

import com.extjs.gxt.ui.client.event.EditorEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.util.Padding;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.button.ToggleButton;
import com.extjs.gxt.ui.client.widget.layout.HBoxLayout;
import com.extjs.gxt.ui.client.widget.layout.HBoxLayout.HBoxLayoutAlign;
import com.extjs.gxt.ui.client.widget.layout.HBoxLayoutData;

public class ReportBar extends LayoutContainer {

    private ReportTitleWidget titleWidget;
    private Button saveButton;
    private ToggleButton dashboardButton;
    private ExportMenuButton exportButton;
    private Button shareButton;
    private Button switchViewButton;

    public ReportBar() {
        setStyleName(ReportResources.INSTANCE.style().bar());

        HBoxLayout layout = new HBoxLayout();
        layout.setHBoxLayoutAlign(HBoxLayoutAlign.MIDDLE);
        layout.setPadding(new Padding(5));

        setLayout(layout);
        setMonitorWindowResize(true);

        addTitle();

        switchViewButton = new Button("Switch to page view",
            IconImageBundle.ICONS.page());
        add(switchViewButton);

        dashboardButton = new ToggleButton("Pin to dashboard",
            IconImageBundle.ICONS.star());
        add(dashboardButton);

        shareButton = new Button(I18N.CONSTANTS.share(),
            IconImageBundle.ICONS.group());
        add(shareButton);

        exportButton = new ExportMenuButton();
        add(exportButton);

        saveButton = new Button(I18N.CONSTANTS.save(),
            IconImageBundle.ICONS.save());
        add(saveButton);

    }

    private void addTitle() {

        titleWidget = new ReportTitleWidget();

        HBoxLayoutData titleLayout = new HBoxLayoutData(0, 0, 0, 7);
        titleLayout.setFlex(1);

        add(titleWidget, titleLayout);
    }

    public void addTitleEditCompleteListener(Listener<EditorEvent> listener) {
        titleWidget.addEditCompleteListener(listener);
    }

    public void setReportTitle(String value) {
        titleWidget.setText(value);
    }

    public Button getSaveButton() {
        return saveButton;
    }

    public ToggleButton getDashboardButton() {
        return dashboardButton;
    }

    public ExportMenuButton getExportButton() {
        return exportButton;
    }

    public Button getShareButton() {
        return shareButton;
    }

    public Button getSwitchViewButton() {
        return switchViewButton;
    }
}
