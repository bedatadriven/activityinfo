package org.activityinfo.client.page.config;

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

import org.activityinfo.client.EventBus;
import org.activityinfo.client.dispatch.AsyncMonitor;
import org.activityinfo.client.dispatch.Dispatcher;
import org.activityinfo.client.dispatch.monitor.MaskingAsyncMonitor;
import org.activityinfo.client.i18n.I18N;
import org.activityinfo.client.icon.IconImageBundle;
import org.activityinfo.client.page.NavigationCallback;
import org.activityinfo.client.page.Page;
import org.activityinfo.client.page.PageId;
import org.activityinfo.client.page.PageState;
import org.activityinfo.client.page.common.toolbar.ActionToolBar;
import org.activityinfo.client.page.common.toolbar.UIActions;
import org.activityinfo.client.util.state.StateProvider;
import org.activityinfo.shared.dto.UserDatabaseDTO;

import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.GridEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.inject.Inject;

public class DbListPage extends ContentPanel implements DbListPresenter.View,
    Page {

    private Grid<UserDatabaseDTO> grid;
    private DbListPresenter presenter;
    private ActionToolBar toolBar;

    @Inject
    public DbListPage(EventBus eventBus, Dispatcher dispatcher,
        StateProvider stateMgr) {
        presenter = new DbListPresenter(eventBus, dispatcher, this);

        setLayout(new FitLayout());
        setHeading(I18N.CONSTANTS.databases());
        setIcon(IconImageBundle.ICONS.database());

        createGrid();
        createToolBar();

        presenter.onSelectionChanged(null);
    }

    private void createToolBar() {
        toolBar = new ActionToolBar();
        toolBar.addButton(UIActions.ADD, I18N.CONSTANTS.newDatabase(),
            IconImageBundle.ICONS.addDatabase());
        toolBar.addEditButton(IconImageBundle.ICONS.editDatabase());
        toolBar.addDeleteButton();
        toolBar.setListener(presenter);
        this.setTopComponent(toolBar);
    }

    private void createGrid() {
        grid = new Grid<UserDatabaseDTO>(presenter.getStore(),
            createColumnModel());
        grid.setAutoExpandColumn("fullName");
        grid.setLoadMask(true);

        grid.addListener(Events.RowDoubleClick, new Listener<GridEvent>() {
            @Override
            public void handleEvent(GridEvent be) {
                presenter.onUIAction(UIActions.EDIT);
            }
        });
        grid.getSelectionModel().addSelectionChangedListener(
            new SelectionChangedListener<UserDatabaseDTO>() {
                @Override
                public void selectionChanged(
                    SelectionChangedEvent<UserDatabaseDTO> se) {
                    presenter.onSelectionChanged(se.getSelectedItem());
                }
            });

        add(grid);
    }

    private ColumnModel createColumnModel() {
        List<ColumnConfig> columns = new ArrayList<ColumnConfig>();
        columns.add(new ColumnConfig("name", I18N.CONSTANTS.name(), 100));
        columns
            .add(new ColumnConfig("fullName", I18N.CONSTANTS.fullName(), 150));
        columns.add(new ColumnConfig("ownerName", I18N.CONSTANTS.ownerName(),
            150));

        return new ColumnModel(columns);
    }

    @Override
    public void setActionEnabled(String id, boolean enabled) {
        toolBar.setActionEnabled(id, enabled);
    }

    @Override
    public AsyncMonitor getDeletingMonitor() {
        return new MaskingAsyncMonitor(this, I18N.CONSTANTS.deleting());
    }

    @Override
    public PageId getPageId() {
        return DbListPresenter.PAGE_ID;
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
        return false;
    }

    @Override
    public void shutdown() {

    }
}
