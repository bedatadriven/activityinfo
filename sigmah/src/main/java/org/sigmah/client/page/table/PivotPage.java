package org.sigmah.client.page.table;

import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.dnd.DND;
import com.extjs.gxt.ui.client.dnd.ListViewDragSource;
import com.extjs.gxt.ui.client.dnd.ListViewDropTarget;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.store.StoreEvent;
import com.extjs.gxt.ui.client.store.StoreListener;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.util.Padding;
import com.extjs.gxt.ui.client.widget.*;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.layout.*;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.inject.Inject;
import org.sigmah.client.AppEvents;
import org.sigmah.client.Application;
import org.sigmah.client.EventBus;
import org.sigmah.client.dispatch.AsyncMonitor;
import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.dispatch.monitor.MaskingAsyncMonitor;
import org.sigmah.client.event.PivotCellEvent;
import org.sigmah.client.page.common.filter.AdminFilterPanel;
import org.sigmah.client.page.common.filter.DateRangePanel;
import org.sigmah.client.page.common.filter.IndicatorTreePanel;
import org.sigmah.client.page.common.toolbar.UIActions;
import org.sigmah.client.page.table.drilldown.DrillDownEditor;
import org.sigmah.client.page.table.drilldown.DrillDownGrid;
import org.sigmah.client.util.DateUtilGWTImpl;
import org.sigmah.client.util.state.IStateManager;
import org.sigmah.shared.dto.AdminEntityDTO;
import org.sigmah.shared.dto.IndicatorDTO;
import org.sigmah.shared.dto.SchemaDTO;
import org.sigmah.shared.report.model.Dimension;
import org.sigmah.shared.report.model.PivotTableElement;

import java.util.Date;
import java.util.List;

/**
 * @author Alex Bertram (akbertram@gmail.com)
 */
public class PivotPage extends LayoutContainer implements PivotPresenter.View {

    protected EventBus eventBus;
    protected Dispatcher service;
    protected IStateManager stateMgr;

    protected PivotPresenter presenter;

    protected ListStore<Dimension> unusedDims;
    protected ListStore<Dimension> rowDims;
    protected ListStore<Dimension> colDims;

    protected ContentPanel filterPane;
    protected IndicatorTreePanel indicatorPanel;
    protected AdminFilterPanel adminPanel;
    protected DateRangePanel datePanel;
    protected LayoutContainer center;
    protected PivotGridPanel gridContainer;
    protected ToolBar gridToolBar;
    protected DrillDownGrid drilldownPanel;
    private Listener<PivotCellEvent> initialDrillDownListener;

    @Inject
    public PivotPage(EventBus eventBus, Dispatcher service, IStateManager stateMgr) {

        this.eventBus = eventBus;
        this.service = service;
        this.stateMgr = stateMgr;

        BorderLayout borderLayout = new BorderLayout();
        borderLayout.setEnableState(true);
        setStateId("pivotPage");
        setLayout(borderLayout);

        createPane();
        createFilterPane();
        createIndicatorPanel();
        createAdminFilter();
        createDateFilter();
        createGridContainer();

        initialDrillDownListener = new Listener<PivotCellEvent>() {
            public void handleEvent(PivotCellEvent be) {
                createDrilldownPanel(be);
            }
        };
        eventBus.addListener(AppEvents.Drilldown, initialDrillDownListener);
    }

    public void createPane() {

        ContentPanel pane = new ContentPanel();
        pane.setHeading(Application.CONSTANTS.dimensions());

        VBoxLayout layout = new VBoxLayout();
        layout.setPadding(new Padding(5));
        layout.setVBoxLayoutAlign(VBoxLayout.VBoxLayoutAlign.STRETCH);

        pane.setLayout(layout);

        VBoxLayoutData labelLayout = new VBoxLayoutData();

        VBoxLayoutData listLayout = new VBoxLayoutData();
        listLayout.setFlex(1.0);

        unusedDims = createStore();
        pane.add(createList(unusedDims), listLayout);

        pane.add(new Text(Application.CONSTANTS.rows()), labelLayout);

        rowDims = createStore();
        pane.add(createList(rowDims), listLayout);

        pane.add(new Text(Application.CONSTANTS.columns()), labelLayout);

        colDims = createStore();
        pane.add(createList(colDims), listLayout);

        BorderLayoutData east = new BorderLayoutData(Style.LayoutRegion.EAST);
        east.setCollapsible(true);
        east.setSplit(true);
        east.setMargins(new Margins(0, 5, 0, 0));

        add(pane, east);
    }

    private ListStore<Dimension> createStore() {
        ListStore<Dimension> store = new ListStore<Dimension>();
        store.addStoreListener(new StoreListener<Dimension>() {
            @Override
            public void storeDataChanged(StoreEvent<Dimension> se) {
                if (presenter != null) {
                    presenter.onDimensionsChanged();
                }
            }
        });
        return store;
    }

    private ListView createList(ListStore<Dimension> store) {

        ListView<Dimension> list = new ListView<Dimension>(store);
        list.setDisplayProperty("caption");

        ListViewDragSource source = new ListViewDragSource(list);

        ListViewDropTarget target = new ListViewDropTarget(list);
        target.setFeedback(DND.Feedback.INSERT);
        target.setAllowSelfAsSource(true);

        return list;
    }

    private void createFilterPane() {
        filterPane = new ContentPanel();
        filterPane.setHeading(Application.CONSTANTS.filter());
        filterPane.setIcon(Application.ICONS.filter());
        filterPane.setLayout(new AccordionLayout());

        BorderLayoutData west = new BorderLayoutData(Style.LayoutRegion.WEST);
        west.setMinSize(250);
        west.setSize(250);
        west.setCollapsible(true);
        west.setSplit(true);
        west.setMargins(new Margins(0, 0, 0, 5));

        add(filterPane, west);
    }

    private void createIndicatorPanel() {
        indicatorPanel = new IndicatorTreePanel(service, true);
        indicatorPanel.setHeaderVisible(true);
        indicatorPanel.setHeading(Application.CONSTANTS.indicators());
        indicatorPanel.setIcon(Application.ICONS.indicator());

        filterPane.add(indicatorPanel);
    }


    private void createAdminFilter() {
        adminPanel = new AdminFilterPanel(service);
        adminPanel.setHeading(Application.CONSTANTS.filterByGeography());
        adminPanel.setIcon(Application.ICONS.filter());

        filterPane.add(adminPanel);
    }

    private void createDateFilter() {
        datePanel = new DateRangePanel();

        filterPane.add(datePanel);
    }

    private void createGridContainer() {

        center = new LayoutContainer();
        center.setLayout(new BorderLayout());
        add(center, new BorderLayoutData(Style.LayoutRegion.CENTER));

        gridContainer = new PivotGridPanel(eventBus);
        gridContainer.setHeaderVisible(true);
        gridContainer.setHeading(Application.CONSTANTS.preview());

        gridToolBar = new ToolBar();
        gridContainer.setTopComponent(gridToolBar);

        SelectionListener<ButtonEvent> listener = new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {
                if (presenter != null && ce.getButton().getItemId() != null) {
                    presenter.onUIAction(ce.getButton().getItemId());
                }
            }
        };

        Button refresh = new Button(Application.CONSTANTS.refreshPreview(),
                Application.ICONS.refresh(), listener);
        refresh.setItemId(UIActions.refresh);
        gridToolBar.add(refresh);

//        Button filter = new Button(Application.CONSTANTS.filter(),
//                Application.ICONS.filter(), listener);
//        filter.setItemId(UIActions.filter);
//        gridToolBar.add(filter);


        Button export = new Button(Application.CONSTANTS.export(),
                Application.ICONS.excel(), listener);
        export.setItemId(UIActions.export);
        gridToolBar.add(export);

        center.add(gridContainer, new BorderLayoutData(Style.LayoutRegion.CENTER));
    }

    protected void createDrilldownPanel(PivotCellEvent event) {

        BorderLayoutData layout = new BorderLayoutData(Style.LayoutRegion.SOUTH);
        layout.setSplit(true);
        layout.setCollapsible(true);

        drilldownPanel = new DrillDownGrid();
        DrillDownEditor drilldownEditor = new DrillDownEditor(eventBus, service, stateMgr, new DateUtilGWTImpl(), drilldownPanel);
        drilldownEditor.onDrillDown(event);

        center.add(drilldownPanel, layout);

        // disconnect our initial drilldown listener;
        // subsequent events will be handled by the DrillDownEditor's listener
        eventBus.removeListener(AppEvents.Drilldown, initialDrillDownListener);

        layout();

    }

    @Override
    public ListStore<Dimension> getRowStore() {
        return rowDims;
    }

    @Override
    public ListStore<Dimension> getColStore() {
        return colDims;
    }

    @Override
    public ListStore<Dimension> getUnsusedStore() {
        return unusedDims;
    }

    public void setSchema(SchemaDTO result) {
        //    indicatorPanel.setSchema(result);

    }

    public void bindPresenter(PivotPresenter presenter) {

        this.presenter = presenter;
    }

    public void enableUIAction(String actionId, boolean enabled) {
        Component button = gridToolBar.getItemByItemId(actionId);
        if (button != null) {
            button.setEnabled(enabled);
        }
    }

    public void setContent(PivotTableElement element) {
        gridContainer.setData(element);
    }

    public AsyncMonitor getMonitor() {
        return new MaskingAsyncMonitor(this, Application.CONSTANTS.loading());
    }

    public List<IndicatorDTO> getSelectedIndicators() {
        return indicatorPanel.getSelection();
    }

    public List<AdminEntityDTO> getAdminRestrictions() {
        return adminPanel.getSelection();
    }

    public Date getMinDate() {
        return datePanel.getMinDate();
    }

    public Date getMaxDate() {
        return datePanel.getMaxDate();
    }

}
