package org.activityinfo.client.report.editor.pivotTable;

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
import org.activityinfo.client.i18n.I18N;
import org.activityinfo.client.page.report.HasReportElement;
import org.activityinfo.client.report.editor.pivotTable.DimensionSelectionListView.Axis;
import org.activityinfo.shared.report.model.PivotTableReportElement;

import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.util.Padding;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Text;
import com.extjs.gxt.ui.client.widget.layout.VBoxLayout;
import com.extjs.gxt.ui.client.widget.layout.VBoxLayoutData;

/**
 * User interface for selecting row / column dimensions
 * 
 */
public class PivotTrayPanel extends ContentPanel implements
    HasReportElement<PivotTableReportElement> {

    private DimensionTree tree;
    private DimensionSelectionListView rowList;
    private DimensionSelectionListView colList;

    private PivotTableReportElement model;

    public PivotTrayPanel(EventBus eventBus, Dispatcher dispatcher) {

        this.tree = new DimensionTree(eventBus, dispatcher);
        this.rowList = new DimensionSelectionListView(eventBus, dispatcher,
            Axis.ROW);
        this.colList = new DimensionSelectionListView(eventBus, dispatcher,
            Axis.COLUMN);

        setHeading(I18N.CONSTANTS.dimensions());
        setScrollMode(Style.Scroll.NONE);
        setIcon(null);

        VBoxLayout layout = new VBoxLayout();
        layout.setPadding(new Padding(5));
        layout.setVBoxLayoutAlign(VBoxLayout.VBoxLayoutAlign.STRETCH);
        setLayout(layout);

        VBoxLayoutData labelLayout = new VBoxLayoutData();
        VBoxLayoutData listLayout = new VBoxLayoutData();
        listLayout.setFlex(1.0);

        tree = new DimensionTree(eventBus, dispatcher);

        add(tree.asComponent(), listLayout);

        add(new Text(I18N.CONSTANTS.rows()), labelLayout);
        add(rowList.asComponent(), listLayout);

        add(new Text(I18N.CONSTANTS.columns()), labelLayout);
        add(colList.asComponent(), listLayout);
    }

    @Override
    public void bind(PivotTableReportElement model) {
        this.model = model;
        tree.bind(model);
        rowList.bind(model);
        colList.bind(model);
    }

    @Override
    public PivotTableReportElement getModel() {
        return model;
    }

    @Override
    public void disconnect() {
        tree.disconnect();
        rowList.disconnect();
        colList.disconnect();
    }

}
