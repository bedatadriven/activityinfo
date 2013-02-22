package org.activityinfo.client.page.search;

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

import java.util.List;

import org.activityinfo.client.icon.IconImageBundle;
import org.activityinfo.client.page.PageStateSerializer;
import org.activityinfo.client.page.entry.place.DataEntryPlace;
import org.activityinfo.shared.command.Filter;
import org.activityinfo.shared.report.content.EntityCategory;
import org.activityinfo.shared.report.content.PivotTableData.Axis;

import com.extjs.gxt.ui.client.widget.HorizontalPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.VerticalPanel;
import com.extjs.gxt.ui.client.widget.form.LabelField;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Image;

public class SearchResultItem extends LayoutContainer {
    private LabelField labelDatabaseName;
    private HorizontalPanel panelTop = new HorizontalPanel();
    private VerticalPanel panelChilds = new VerticalPanel();
    private int activityCount = 0;
    private int indicatorCount = 0;

    public SearchResultItem() {
        super();

        initializeComponent();
        SearchResources.INSTANCE.searchStyles().ensureInjected();

        addDatabaseIcon();
        addDatabaseLabel();

        add(panelTop);
        add(panelChilds);
    }

    public int getActivityCount() {
        return activityCount;
    }

    public int getIndicatorCount() {
        return indicatorCount;
    }

    private void addDatabaseIcon() {
        Image imageDatabase = IconImageBundle.ICONS.database().createImage();
        panelTop.add(imageDatabase);
    }

    private void initializeComponent() {
    }

    private void addDatabaseLabel() {
        labelDatabaseName = new LabelField();
        panelTop.add(labelDatabaseName);
    }

    public void setDabaseName(String databaseName) {
        labelDatabaseName.setValue(databaseName);
    }

    public void setChilds(List<Axis> childList) {
        for (Axis axis : childList) {
            VerticalPanel panelAll = new VerticalPanel();
            HorizontalPanel panelChild = new HorizontalPanel();

            HorizontalPanel spacer = new HorizontalPanel();
            spacer.setWidth(20);
            panelChild.add(spacer);
            Image image = IconImageBundle.ICONS.activity().createImage();
            panelChild.add(image);
            panelAll.add(panelChild);

            EntityCategory activity = (EntityCategory) axis.getCategory();
            Hyperlink link = new Hyperlink(axis.getLabel(),
                PageStateSerializer.serialize(new DataEntryPlace(Filter
                    .filter().onActivity(activity.getId()))));
            link.setStylePrimaryName("link");
            panelChild.add(link);

            for (Axis childAxis : axis.getChildren()) {
                HorizontalPanel panelIndicator = new HorizontalPanel();

                HorizontalPanel spacerIndicator = new HorizontalPanel();
                spacerIndicator.setWidth(40);
                panelIndicator.add(spacerIndicator);
                panelIndicator.add(IconImageBundle.ICONS.indicator()
                    .createImage());

                // Hyperlink linkIndicator = new Hyperlink(childAxis.getLabel(),
                // "site-grid/" +
                // ((EntityCategory)childAxis.getCategory()).getId());
                // linkIndicator.setStylePrimaryName("link");
                // panelIndicator.add(linkIndicator);

                LabelField labelIndicator = new LabelField(childAxis.getLabel());
                panelIndicator.add(labelIndicator);

                panelAll.add(panelIndicator);
                indicatorCount++;
            }

            activityCount++;
            panelChilds.add(panelAll);
        }
    }
}
