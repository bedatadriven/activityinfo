package org.activityinfo.client.page.config;

import com.extjs.gxt.ui.client.event.*;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.inject.Inject;
import org.activityinfo.client.Application;
import org.activityinfo.client.EventBus;
import org.activityinfo.client.Place;
import org.activityinfo.client.dispatch.AsyncMonitor;
import org.activityinfo.client.dispatch.Dispatcher;
import org.activityinfo.client.dispatch.monitor.MaskingAsyncMonitor;
import org.activityinfo.client.page.NavigationCallback;
import org.activityinfo.client.page.PageId;
import org.activityinfo.client.page.PagePresenter;
import org.activityinfo.client.page.Pages;
import org.activityinfo.client.page.common.toolbar.ActionToolBar;
import org.activityinfo.client.page.common.toolbar.UIActions;
import org.activityinfo.client.util.state.IStateManager;
import org.activityinfo.shared.dto.UserDatabaseDTO;

import java.util.ArrayList;
import java.util.List;

public class DbListPage extends ContentPanel implements DbListPresenter.View, PagePresenter {

    private Grid<UserDatabaseDTO> grid;
    private DbListPresenter presenter;
    private ActionToolBar toolBar;

    @Inject
    public DbListPage(EventBus eventBus, Dispatcher dispatcher, IStateManager stateMgr) {
        presenter = new DbListPresenter(eventBus, dispatcher, this);

        setLayout(new FitLayout());
        setHeading(Application.CONSTANTS.databases());
        setIcon(Application.ICONS.database());

        createGrid();
        createToolBar();

        presenter.onSelectionChanged(null);
    }

    private void createToolBar() {
        toolBar = new ActionToolBar();
        toolBar.addButton(UIActions.add, Application.CONSTANTS.newDatabase(), Application.ICONS.addDatabase());
        toolBar.addEditButton(Application.ICONS.editDatabase());
        toolBar.addDeleteButton();
        toolBar.setListener(presenter);
        this.setTopComponent(toolBar);
	}

	private void createGrid() {
		grid = new Grid<UserDatabaseDTO>(presenter.getStore(), createColumnModel());
		grid.setAutoExpandColumn("fullName");
        grid.setLoadMask(true);

        grid.addListener(Events.RowDoubleClick, new Listener<GridEvent>() {
            @Override
            public void handleEvent(GridEvent be) {
                presenter.onUIAction(UIActions.edit);
            }
        });
        grid.getSelectionModel().addSelectionChangedListener(new SelectionChangedListener<UserDatabaseDTO>() {
            @Override
            public void selectionChanged(SelectionChangedEvent<UserDatabaseDTO> se) {
                presenter.onSelectionChanged(se.getSelectedItem());
            }
        });
	
		add(grid);
	}

	private ColumnModel createColumnModel() {
		List<ColumnConfig> columns = new ArrayList<ColumnConfig>();
		columns.add(new ColumnConfig("name", Application.CONSTANTS.name(), 100));
		columns.add(new ColumnConfig("fullName", Application.CONSTANTS.fullName(), 150));
		columns.add(new ColumnConfig("ownerName", Application.CONSTANTS.ownerName(), 150));

		return new ColumnModel(columns);
	}

    @Override
    public void setActionEnabled(String id, boolean enabled) {
        toolBar.setActionEnabled(id, enabled);
    }

    @Override
    public AsyncMonitor getDeletingMonitor() {
        return new MaskingAsyncMonitor(this, Application.CONSTANTS.deleting());
    }

    @Override
    public PageId getPageId() {
        return Pages.DatabaseList;
    }

    @Override
    public Object getWidget() {
        return this;
    }

    @Override
    public void requestToNavigateAway(Place place, NavigationCallback callback) {
        callback.onDecided(true);
    }

    @Override
    public String beforeWindowCloses() {
        return null;
    }

    @Override
    public boolean navigate(Place place) {
        return false;
    }

    @Override
    public void shutdown() {

    }
}
