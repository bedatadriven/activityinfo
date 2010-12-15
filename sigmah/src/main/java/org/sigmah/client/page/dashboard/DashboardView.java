/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.page.dashboard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import org.sigmah.client.EventBus;
import org.sigmah.client.UserInfo;
import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.dispatch.monitor.MaskingAsyncMonitor;
import org.sigmah.client.dispatch.remote.Authentication;
import org.sigmah.client.event.NavigationEvent;
import org.sigmah.client.i18n.I18N;
import org.sigmah.client.icon.IconImageBundle;
import org.sigmah.client.page.NavigationHandler;
import org.sigmah.client.page.PageState;
import org.sigmah.client.page.charts.ChartPageState;
import org.sigmah.client.page.config.DbListPageState;
import org.sigmah.client.page.dashboard.CreateProjectWindow.CreateProjectListener;
import org.sigmah.client.page.entry.SiteGridPageState;
import org.sigmah.client.page.map.MapPageState;
import org.sigmah.client.page.project.ProjectPresenter;
import org.sigmah.client.page.project.dashboard.funding.FundingIconProvider;
import org.sigmah.client.page.project.dashboard.funding.FundingIconProvider.IconSize;
import org.sigmah.client.page.report.ReportListPageState;
import org.sigmah.client.page.table.PivotPageState;
import org.sigmah.client.ui.RatioBar;
import org.sigmah.client.ui.StylableVBoxLayout;
import org.sigmah.client.util.Notification;
import org.sigmah.client.util.NumberUtils;
import org.sigmah.shared.command.UpdateProject;
import org.sigmah.shared.command.result.VoidResult;
import org.sigmah.shared.domain.ProjectModelType;
import org.sigmah.shared.dto.OrgUnitDTOLight;
import org.sigmah.shared.dto.ProjectDTOLight;
import org.sigmah.shared.dto.element.DefaultFlexibleElementDTO;
import org.sigmah.shared.dto.element.handler.ValueEvent;

import com.allen_sauer.gwt.log.client.Log;
import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.store.Store;
import com.extjs.gxt.ui.client.store.StoreSorter;
import com.extjs.gxt.ui.client.store.TreeStore;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.util.Padding;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.Text;
import com.extjs.gxt.ui.client.widget.WidgetComponent;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.Radio;
import com.extjs.gxt.ui.client.widget.form.RadioGroup;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.VBoxLayout;
import com.extjs.gxt.ui.client.widget.layout.VBoxLayoutData;
import com.extjs.gxt.ui.client.widget.toolbar.SeparatorToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.extjs.gxt.ui.client.widget.treegrid.TreeGrid;
import com.extjs.gxt.ui.client.widget.treegrid.WidgetTreeGridCellRenderer;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

/**
 * Displays the dashboard.
 * 
 * @author Raphaël Calabro (rcalabro@ideia.fr)
 */
public class DashboardView extends ContentPanel implements DashboardPresenter.View {

    private final static int BORDER = 8;
    private final static String STYLE_MAIN_BACKGROUND = "main-background";

    /**
     * The service.
     */
    private final Dispatcher dispatcher;
    private final EventBus eventBus;
    private final Authentication authentication;
    private final UserInfo info;

    /**
     * Model containing the displayed projects
     */
    private final DashboardPresenter.ProjectStore projectStore;

    private ContentPanel projectsPanel;
    private OrgUnitTreeGrid orgUnitsTreeGrid;
    private ContentPanel orgUnitsPanel;

    private Radio ngoRadio;
    private Radio fundingRadio;
    private Radio partnerRadio;

    private Button filterButton;

    @Inject
    public DashboardView(final EventBus eventBus, final Dispatcher dispatcher, final Authentication authentication,
            final UserInfo info) {

        this.dispatcher = dispatcher;
        this.eventBus = eventBus;
        this.authentication = authentication;
        this.info = info;

        // Initialization of the models
        projectStore = new DashboardPresenter.ProjectStore();
        projectStore.setMonitorChanges(true);

        // The dashboard itself
        final BorderLayout borderLayout = new BorderLayout();
        borderLayout.setContainerStyle("x-border-layout-ct " + STYLE_MAIN_BACKGROUND);
        setLayout(borderLayout);
        setHeaderVisible(false);
        setBorders(false);

        // Left bar
        final ContentPanel leftPanel = new ContentPanel();
        final VBoxLayout leftPanelLayout = new StylableVBoxLayout(STYLE_MAIN_BACKGROUND);
        leftPanelLayout.setVBoxLayoutAlign(VBoxLayout.VBoxLayoutAlign.STRETCH);
        leftPanelLayout.setPadding(new Padding(0));
        leftPanel.setLayout(leftPanelLayout);
        leftPanel.setHeaderVisible(false);
        leftPanel.setBorders(false);
        leftPanel.setBodyBorder(false);

        // Left bar content
        final VBoxLayoutData vBoxLayoutData = new VBoxLayoutData();
        vBoxLayoutData.setFlex(1.0);
        vBoxLayoutData.setMargins(new Margins(0, 0, BORDER, 0));

        final ContentPanel remindersPanel = new ContentPanel(new FitLayout());
        remindersPanel.setHeading(I18N.CONSTANTS.reminderPoints());
        leftPanel.add(remindersPanel, vBoxLayoutData);

        final ContentPanel importantPointsPanel = new ContentPanel(new FitLayout());
        importantPointsPanel.setHeading(I18N.CONSTANTS.monitoredPoints());
        leftPanel.add(importantPointsPanel, vBoxLayoutData);

        final ContentPanel menuPanel = new ContentPanel();
        final VBoxLayout menuPanelLayout = new VBoxLayout();
        menuPanelLayout.setVBoxLayoutAlign(VBoxLayout.VBoxLayoutAlign.STRETCH);
        menuPanel.setLayout(menuPanelLayout);
        menuPanel.setHeading(I18N.CONSTANTS.menu());

        buildNavLinks(menuPanel);

        final VBoxLayoutData bottomVBoxLayoutData = new VBoxLayoutData();
        bottomVBoxLayoutData.setFlex(1.0);
        bottomVBoxLayoutData.setMargins(new Margins(0, 0, 0, 0));
        leftPanel.add(menuPanel, bottomVBoxLayoutData);

        final BorderLayoutData leftLayoutData = new BorderLayoutData(LayoutRegion.WEST, 250);
        leftLayoutData.setMargins(new Margins(0, BORDER / 2, 0, 0));
        add(leftPanel, leftLayoutData);

        // Main panel
        final ContentPanel mainPanel = new ContentPanel();
        final VBoxLayout mainPanelLayout = new StylableVBoxLayout(STYLE_MAIN_BACKGROUND);
        mainPanelLayout.setVBoxLayoutAlign(VBoxLayout.VBoxLayoutAlign.STRETCH);
        mainPanel.setLayout(mainPanelLayout);
        mainPanel.setHeaderVisible(false);
        mainPanel.setBorders(false);
        mainPanel.setBodyBorder(false);

        // Org units panel
        final VBoxLayoutData smallVBoxLayoutData = new VBoxLayoutData();
        smallVBoxLayoutData.setFlex(1.0);
        smallVBoxLayoutData.setMargins(new Margins(0, 0, BORDER, 0));
        mainPanel.add(buildOrgUnitsPanel(), smallVBoxLayoutData);

        // Country list panel
        // mainPanel.add(buildCountriesPanel(), smallVBoxLayoutData);

        // Project tree panel
        final VBoxLayoutData largeVBoxLayoutData = new VBoxLayoutData();
        largeVBoxLayoutData.setFlex(2.0);
        mainPanel.add(buildProjectPanel(), largeVBoxLayoutData);

        final BorderLayoutData mainLayoutData = new BorderLayoutData(LayoutRegion.CENTER);
        mainLayoutData.setMargins(new Margins(0, 0, 0, BORDER / 2));
        add(mainPanel, mainLayoutData);
    }

    /**
     * Builds the nevigation links.
     * 
     * @param menuPanel
     *            The menu panel.
     */
    private void buildNavLinks(final ContentPanel menuPanel) {

        // Menu
        addNavLink(eventBus, menuPanel, I18N.CONSTANTS.createProjectNewProject(), IconImageBundle.ICONS.add(),
                new Listener<ButtonEvent>() {

                    private final CreateProjectWindow window = CreateProjectWindow.getInstance(dispatcher,
                            authentication, info);

                    {
                        window.addListener(new CreateProjectListener() {

                            @Override
                            public void projectCreated(ProjectDTOLight project) {

                                projectStore.clearFilters();
                                projectStore.add(project, false);
                                projectStore.applyFilters(null);
                                
                                // Show notification.
                                Notification.show(I18N.CONSTANTS.createProjectSucceeded(),
                                        I18N.CONSTANTS.createProjectSucceededDetails());
                            }

                            @Override
                            public void projectCreatedAsFunded(ProjectDTOLight project, double percentage) {
                                // nothing to do (must not be called).
                            }

                            @Override
                            public void projectCreatedAsFunding(ProjectDTOLight project, double percentage) {
                                // nothing to do (must not be called).
                            }
                        });
                    }

                    @Override
                    public void handleEvent(ButtonEvent be) {
                        window.show();
                    }
                });

        // Temporary code to hide/show activityInfo menus
        if (authentication.isShowMenus()) {
            addNavLink(eventBus, menuPanel, I18N.CONSTANTS.dataEntry(), IconImageBundle.ICONS.dataEntry(),
                    new SiteGridPageState());
            addNavLink(eventBus, menuPanel, I18N.CONSTANTS.reports(), IconImageBundle.ICONS.report(),
                    new ReportListPageState());
            addNavLink(eventBus, menuPanel, I18N.CONSTANTS.charts(), IconImageBundle.ICONS.barChart(),
                    new ChartPageState());
            addNavLink(eventBus, menuPanel, I18N.CONSTANTS.maps(), IconImageBundle.ICONS.map(), new MapPageState());
            addNavLink(eventBus, menuPanel, I18N.CONSTANTS.tables(), IconImageBundle.ICONS.table(),
                    new PivotPageState());
            addNavLink(eventBus, menuPanel, I18N.CONSTANTS.setup(), IconImageBundle.ICONS.setup(),
                    new DbListPageState());
        }
    }

    /**
     * Creates a navigation button in the given panel.
     * 
     * @param eventBus
     *            Event bus of the application
     * @param panel
     *            Placeholder of the button
     * @param text
     *            Label of the button
     * @param icon
     *            Icon displayed next to the label
     * @param place
     *            The user will be redirected there when the button is clicked
     */
    private void addNavLink(final EventBus eventBus, final ContentPanel panel, final String text,
            final AbstractImagePrototype icon, final PageState place) {
        final Button button = new Button(text, icon, new SelectionListener<ButtonEvent>() {
            @Override
            public void componentSelected(ButtonEvent ce) {
                eventBus.fireEvent(new NavigationEvent(NavigationHandler.NavigationRequested, place));
            }
        });

        final VBoxLayoutData vBoxLayoutData = new VBoxLayoutData();
        vBoxLayoutData.setFlex(1.0);
        panel.add(button, vBoxLayoutData);
    }

    /**
     * Creates a navigation button in the given panel.
     * 
     * @param eventBus
     *            Event bus of the application
     * @param panel
     *            Placeholder of the button
     * @param text
     *            Label of the button
     * @param icon
     *            Icon displayed next to the label
     * @param clickHandler
     *            The action executed when the button is clicked
     */
    private void addNavLink(final EventBus eventBus, final ContentPanel panel, final String text,
            final AbstractImagePrototype icon, final Listener<ButtonEvent> clickHandler) {

        final Button button = new Button(text, icon);
        button.addListener(Events.OnClick, clickHandler);

        final VBoxLayoutData vBoxLayoutData = new VBoxLayoutData();
        vBoxLayoutData.setFlex(1.0);
        panel.add(button, vBoxLayoutData);
    }

    /**
     * Builds the org units panel.
     * 
     * @return The panel;
     */
    private Component buildOrgUnitsPanel() {

        orgUnitsTreeGrid = new OrgUnitTreeGrid(eventBus, false);

        final ContentPanel panel = new ContentPanel(new FitLayout());
        panel.setHeading(I18N.CONSTANTS.orgunitTree());

        panel.setTopComponent(orgUnitsTreeGrid.getToolbar());
        panel.add(orgUnitsTreeGrid.getTreeGrid());

        orgUnitsPanel = panel;

        return panel;
    }

    /**
     * Builds the projects panel.
     * 
     * @return The panel.
     */
    private Component buildProjectPanel() {

        // Grid.
        final TreeGrid<ProjectDTOLight> projectTreeGrid = new TreeGrid<ProjectDTOLight>(projectStore,
                getProjectGridColumnModel());
        projectTreeGrid.setBorders(true);
        projectTreeGrid.getStyle().setNodeOpenIcon(null);
        projectTreeGrid.getStyle().setNodeCloseIcon(null);
        projectTreeGrid.getStyle().setLeafIcon(null);
        projectTreeGrid.setAutoExpandColumn("fullName");
        projectTreeGrid.setTrackMouseOver(false);
        projectTreeGrid.setAutoExpand(true);

        // Store.
        projectStore.setStoreSorter(new StoreSorter<ProjectDTOLight>() {
            @Override
            public int compare(Store<ProjectDTOLight> store, ProjectDTOLight m1, ProjectDTOLight m2, String property) {

                if ("name".equals(property)) {
                    return m1.getName().compareToIgnoreCase(m2.getName());
                } else if ("fullName".equals(property)) {
                    return m1.getFullName().compareToIgnoreCase(m2.getFullName());
                } else if ("phase".equals(property)) {
                    return m1.getCurrentPhaseDTO().getPhaseModelDTO().getName()
                            .compareToIgnoreCase(m2.getCurrentPhaseDTO().getPhaseModelDTO().getName());
                } else if ("orgUnitName".equals(property)) {
                    return m1.getOrgUnitName().compareToIgnoreCase(m2.getOrgUnitName());
                } else if ("spentBudget".equals(property)) {
                    final Double d1 = NumberUtils.adjustRatio(NumberUtils.ratio(m1.getSpendBudget(),
                            m1.getPlannedBudget()));
                    final Double d2 = NumberUtils.adjustRatio(NumberUtils.ratio(m2.getSpendBudget(),
                            m2.getPlannedBudget()));
                    return d1.compareTo(d2);
                } else if ("time".equals(property)) {
                    final Double d1 = m1.getElapsedTime();
                    final Double d2 = m2.getElapsedTime();
                    return d1.compareTo(d2);
                } else if ("activity".equals(property)) {
                    return 0;
                } else if ("category".equals(property)) {
                    return m1.getCategoriesString().compareToIgnoreCase(m2.getCategoriesString());
                } else {
                    return super.compare(store, m1, m2, property);
                }
            }
        });

        // Top panel
        final RadioGroup group = new RadioGroup("projectTypeFilter");
        group.setFireChangeEventOnSetValue(true);

        ngoRadio = new Radio();
        ngoRadio.setFireChangeEventOnSetValue(true);
        ngoRadio.setValue(true);
        ngoRadio.setFieldLabel(ProjectModelType.getName(ProjectModelType.NGO));
        ngoRadio.addStyleName("toolbar-radio");

        final WidgetComponent ngoIcon = new WidgetComponent(FundingIconProvider.getProjectTypeIcon(
                ProjectModelType.NGO, IconSize.SMALL).createImage());
        ngoIcon.addStyleName("toolbar-icon");

        final Label ngoLabel = new Label(ProjectModelType.getName(ProjectModelType.NGO));
        ngoLabel.addStyleName("flexibility-element-label");
        ngoLabel.addStyleName("project-starred-icon");
        ngoLabel.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                ngoRadio.setValue(true);
                fundingRadio.setValue(false);
                partnerRadio.setValue(false);
            }
        });

        fundingRadio = new Radio();
        fundingRadio.setFireChangeEventOnSetValue(true);
        fundingRadio.setFieldLabel(ProjectModelType.getName(ProjectModelType.FUNDING));
        fundingRadio.addStyleName("toolbar-radio");

        final WidgetComponent fundingIcon = new WidgetComponent(FundingIconProvider.getProjectTypeIcon(
                ProjectModelType.FUNDING, IconSize.SMALL).createImage());
        fundingIcon.addStyleName("toolbar-icon");

        final Label fundingLabel = new Label(ProjectModelType.getName(ProjectModelType.FUNDING));
        fundingLabel.addStyleName("flexibility-element-label");
        fundingLabel.addStyleName("project-starred-icon");
        fundingLabel.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                ngoRadio.setValue(false);
                fundingRadio.setValue(true);
                partnerRadio.setValue(false);
            }
        });

        partnerRadio = new Radio();
        partnerRadio.setFireChangeEventOnSetValue(true);
        partnerRadio.setFieldLabel(ProjectModelType.getName(ProjectModelType.LOCAL_PARTNER));
        partnerRadio.addStyleName("toolbar-radio");

        final WidgetComponent partnerIcon = new WidgetComponent(FundingIconProvider.getProjectTypeIcon(
                ProjectModelType.LOCAL_PARTNER, IconSize.SMALL).createImage());
        partnerIcon.addStyleName("toolbar-icon");

        final Label partnerLabel = new Label(ProjectModelType.getName(ProjectModelType.LOCAL_PARTNER));
        partnerLabel.addStyleName("flexibility-element-label");
        partnerLabel.addStyleName("project-starred-icon");
        partnerLabel.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                ngoRadio.setValue(false);
                fundingRadio.setValue(false);
                partnerRadio.setValue(true);
            }
        });

        final HTML headLabel = new HTML("&nbsp;&nbsp;" + I18N.CONSTANTS.projectTypeFilter() + ": ");
        headLabel.addStyleName("flexibility-element-label");

        group.add(ngoRadio);
        group.add(fundingRadio);
        group.add(partnerRadio);

        // Expand all button.
        final Button expandButton = new Button(I18N.CONSTANTS.expandAll(), IconImageBundle.ICONS.expand(),
                new SelectionListener<ButtonEvent>() {

                    @Override
                    public void componentSelected(ButtonEvent ce) {
                        projectTreeGrid.expandAll();
                    }
                });

        // Collapse all button.
        final Button collapseButton = new Button(I18N.CONSTANTS.collapseAll(), IconImageBundle.ICONS.collapse(),
                new SelectionListener<ButtonEvent>() {

                    @Override
                    public void componentSelected(ButtonEvent ce) {
                        projectTreeGrid.collapseAll();
                    }
                });

        // Collapse all button.
        filterButton = new Button(I18N.CONSTANTS.filter(), IconImageBundle.ICONS.filter());

        final ToolBar toolbar = new ToolBar();
        toolbar.add(expandButton);
        toolbar.add(collapseButton);
        toolbar.add(new SeparatorToolItem());
        toolbar.add(filterButton);
        toolbar.add(new WidgetComponent(headLabel));
        toolbar.add(ngoRadio);
        toolbar.add(ngoIcon);
        toolbar.add(new WidgetComponent(ngoLabel));
        toolbar.add(fundingRadio);
        toolbar.add(fundingIcon);
        toolbar.add(new WidgetComponent(fundingLabel));
        toolbar.add(partnerRadio);
        toolbar.add(partnerIcon);
        toolbar.add(new WidgetComponent(partnerLabel));

        // Panel
        final ContentPanel projectTreePanel = new ContentPanel(new FitLayout());
        projectTreePanel.setHeading(I18N.CONSTANTS.projects());

        projectTreePanel.setTopComponent(toolbar);
        projectTreePanel.add(projectTreeGrid);
        projectsPanel = projectTreePanel;

        return projectTreePanel;
    }

    /**
     * Builds and returns the columns model for the projects tree grid.
     * 
     * @return The project tree grid columns model.
     */
    private ColumnModel getProjectGridColumnModel() {

        final DateTimeFormat format = DateTimeFormat.getFormat(I18N.CONSTANTS.flexibleElementDateFormat());

        // Starred icon
        final ColumnConfig starredIconColumn = new ColumnConfig("starred", "", 24);
        starredIconColumn.setRenderer(new GridCellRenderer<ProjectDTOLight>() {
            private final DashboardImageBundle imageBundle = GWT.create(DashboardImageBundle.class);

            @Override
            public Object render(final ProjectDTOLight model, String property, ColumnData config, int rowIndex,
                    int colIndex, final ListStore<ProjectDTOLight> store, final Grid<ProjectDTOLight> grid) {
                final Image icon;

                if (model.getStarred()) {
                    icon = imageBundle.star().createImage();
                } else {
                    icon = imageBundle.emptyStar().createImage();
                }

                icon.addClickHandler(new ClickHandler() {

                    @Override
                    public void onClick(ClickEvent event) {

                        final ArrayList<ValueEvent> events = new ArrayList<ValueEvent>();
                        final DefaultFlexibleElementDTO dto = new DefaultFlexibleElementDTO();
                        dto.setId(-1);
                        final ValueEvent starredEvent = new ValueEvent(dto, String.valueOf(!model.getStarred()));
                        events.add(starredEvent);

                        dispatcher.execute(new UpdateProject(model.getId(), events), new MaskingAsyncMonitor(
                                projectsPanel, I18N.CONSTANTS.loading()), new AsyncCallback<VoidResult>() {

                            @Override
                            public void onFailure(Throwable e) {
                                Log.error(
                                        "[execute] Error while setting the favorite status of the project #"
                                                + model.getId(), e);
                                MessageBox.alert(I18N.CONSTANTS.projectStarredError(),
                                        I18N.CONSTANTS.projectStarredErrorDetails(), null);
                            }

                            @Override
                            public void onSuccess(VoidResult result) {
                                model.setStarred(!model.getStarred());
                                store.update(model);
                                if (model.getStarred()) {
                                    Notification.show(I18N.CONSTANTS.infoConfirmation(),
                                            I18N.CONSTANTS.projectStarred());
                                }
                            }
                        });
                    }
                });

                icon.addStyleName("project-starred-icon");

                return icon;
            }
        });

        // Code
        final ColumnConfig codeColumn = new ColumnConfig("name", I18N.CONSTANTS.projectName(), 110);
        codeColumn.setRenderer(new WidgetTreeGridCellRenderer<ProjectDTOLight>() {
            @Override
            public Widget getWidget(ProjectDTOLight model, String property, ColumnData config, int rowIndex,
                    int colIndex, ListStore<ProjectDTOLight> store, Grid<ProjectDTOLight> grid) {

                final Hyperlink h = new Hyperlink((String) model.get(property), true, ProjectPresenter.PAGE_ID
                        .toString() + '!' + model.get("id").toString());
                if (!model.isLeaf()) {
                    h.addStyleName("project-grid-node");
                } else {
                    h.addStyleName("project-grid-leaf");
                }

                final com.google.gwt.user.client.ui.Grid panel = new com.google.gwt.user.client.ui.Grid(1, 2);
                panel.setCellPadding(0);
                panel.setCellSpacing(0);

                panel.setWidget(
                        0,
                        0,
                        FundingIconProvider.getProjectTypeIcon(
                                model.getProjectModelType(authentication.getOrganizationId()), IconSize.SMALL)
                                .createImage());
                panel.getCellFormatter().addStyleName(0, 0, "project-grid-code-icon");
                panel.setWidget(0, 1, h);
                panel.getCellFormatter().addStyleName(0, 1, "project-grid-code");

                return panel;
            }
        });

        // Title
        final ColumnConfig titleColumn = new ColumnConfig("fullName", I18N.CONSTANTS.projectFullName(), 230);
        titleColumn.setRenderer(new GridCellRenderer<ProjectDTOLight>() {

            @Override
            public Object render(ProjectDTOLight model, String property, ColumnData config, int rowIndex, int colIndex,
                    ListStore<ProjectDTOLight> store, Grid<ProjectDTOLight> grid) {
                String title = (String) model.get(property);
                if (model.getParent() != null) {
                    title = "&nbsp;&nbsp;&nbsp;&nbsp;" + title;
                }
                return createProjectGridText(model, title);
            }
        });

        // Current phase
        final ColumnConfig currentPhaseName = new ColumnConfig("phase", I18N.CONSTANTS.projectActivePhase(), 150);
        currentPhaseName.setRenderer(new GridCellRenderer<ProjectDTOLight>() {
            @Override
            public Object render(ProjectDTOLight model, String property, ColumnData config, int rowIndex, int colIndex,
                    ListStore<ProjectDTOLight> store, Grid<ProjectDTOLight> grid) {
                return createProjectGridText(model, model.getCurrentPhaseDTO().getPhaseModelDTO().getName());
            }
        });

        // Org Unit
        final ColumnConfig orgUnitColumn = new ColumnConfig("orgUnitName", I18N.CONSTANTS.orgunit(), 150);
        orgUnitColumn.setRenderer(new GridCellRenderer<ProjectDTOLight>() {

            @Override
            public Object render(ProjectDTOLight model, String property, ColumnData config, int rowIndex, int colIndex,
                    ListStore<ProjectDTOLight> store, Grid<ProjectDTOLight> grid) {
                return createProjectGridText(model, (String) model.get(property));
            }
        });

        // Spent budget
        final ColumnConfig spentBudgetColumn = new ColumnConfig("spentBudget", I18N.CONSTANTS.projectSpendBudget(), 100);
        spentBudgetColumn.setRenderer(new GridCellRenderer<ProjectDTOLight>() {

            @Override
            public Object render(ProjectDTOLight model, String property, ColumnData config, int rowIndex, int colIndex,
                    ListStore<ProjectDTOLight> store, Grid<ProjectDTOLight> grid) {
                return new RatioBar(NumberUtils.ratio(model.getSpendBudget(), model.getPlannedBudget()));
            }
        });

        // Time
        final ColumnConfig timeColumn = new ColumnConfig("time", I18N.CONSTANTS.projectTime(), 100);
        timeColumn.setRenderer(new GridCellRenderer<ProjectDTOLight>() {

            @Override
            public Object render(ProjectDTOLight model, String property, ColumnData config, int rowIndex, int colIndex,
                    ListStore<ProjectDTOLight> store, Grid<ProjectDTOLight> grid) {
                return new RatioBar(model.getElapsedTime());
            }
        });

        // Start date
        final ColumnConfig startDateColumn = new ColumnConfig("startDate", I18N.CONSTANTS.projectStartDate(), 75);
        startDateColumn.setDateTimeFormat(format);
        startDateColumn.setRenderer(new GridCellRenderer<ProjectDTOLight>() {

            @Override
            public Object render(ProjectDTOLight model, String property, ColumnData config, int rowIndex, int colIndex,
                    ListStore<ProjectDTOLight> store, Grid<ProjectDTOLight> grid) {
                final Date d = (Date) model.get(property);
                return createProjectGridText(model, d != null ? format.format(d) : "");
            }
        });

        // End date
        final ColumnConfig endDateColumn = new ColumnConfig("endDate", I18N.CONSTANTS.projectEndDate(), 75);
        endDateColumn.setDateTimeFormat(format);
        endDateColumn.setRenderer(new GridCellRenderer<ProjectDTOLight>() {

            @Override
            public Object render(ProjectDTOLight model, String property, ColumnData config, int rowIndex, int colIndex,
                    ListStore<ProjectDTOLight> store, Grid<ProjectDTOLight> grid) {
                final Date d = (Date) model.get(property);
                return createProjectGridText(model, d != null ? format.format(d) : "");
            }
        });

        // Close date
        final ColumnConfig closeDateColumn = new ColumnConfig("closeDate", I18N.CONSTANTS.projectClosedDate(), 75);
        closeDateColumn.setDateTimeFormat(format);
        closeDateColumn.setRenderer(new GridCellRenderer<ProjectDTOLight>() {

            @Override
            public Object render(ProjectDTOLight model, String property, ColumnData config, int rowIndex, int colIndex,
                    ListStore<ProjectDTOLight> store, Grid<ProjectDTOLight> grid) {
                final Date d = (Date) model.get(property);
                return createProjectGridText(model, d != null ? format.format(d) : "");
            }
        });

        // Activity
        final ColumnConfig activityColumn = new ColumnConfig("activity", I18N.CONSTANTS.logFrameActivity(), 100);
        activityColumn.setSortable(false);
        activityColumn.setRenderer(new GridCellRenderer<ProjectDTOLight>() {

            @Override
            public Object render(ProjectDTOLight model, String property, ColumnData config, int rowIndex, int colIndex,
                    ListStore<ProjectDTOLight> store, Grid<ProjectDTOLight> grid) {
                return new RatioBar(0);
            }
        });

        // Category
        final ColumnConfig categoryColumn = new ColumnConfig("category", I18N.CONSTANTS.category(), 150);
        categoryColumn.setRenderer(new GridCellRenderer<ProjectDTOLight>() {

            @Override
            public Object render(ProjectDTOLight model, String property, ColumnData config, int rowIndex, int colIndex,
                    ListStore<ProjectDTOLight> store, Grid<ProjectDTOLight> grid) {
                return createProjectGridText(model, model.getCategoriesString());
            }
        });

        return new ColumnModel(Arrays.asList(starredIconColumn, codeColumn, titleColumn, currentPhaseName,
                orgUnitColumn, spentBudgetColumn, startDateColumn, endDateColumn, closeDateColumn, timeColumn,
                activityColumn, categoryColumn));
    }

    private Object createProjectGridText(ProjectDTOLight model, String content) {
        final Text label = new Text(content);
        if (!model.isLeaf()) {
            label.addStyleName("project-grid-node");
        } else {
            label.addStyleName("project-grid-leaf");
        }
        return label;
    }

    @Override
    public DashboardPresenter.ProjectStore getProjectsStore() {
        return projectStore;
    }

    @Override
    public TreeStore<OrgUnitDTOLight> getOrgUnitsStore() {
        return orgUnitsTreeGrid.getStore();
    }

    @Override
    public TreeGrid<OrgUnitDTOLight> getOrgUnitsTree() {
        return orgUnitsTreeGrid.getTreeGrid();
    }

    @Override
    public ContentPanel getProjectsPanel() {
        return projectsPanel;
    }

    @Override
    public ContentPanel getOrgUnitsPanel() {
        return orgUnitsPanel;
    }

    @Override
    public Radio getRadioFilter(ProjectModelType type) {

        if (type != null) {
            switch (type) {
            case NGO:
                return ngoRadio;
            case FUNDING:
                return fundingRadio;
            case LOCAL_PARTNER:
                return partnerRadio;
            }
        }

        return null;
    }

    @Override
    public Button getFilterButton() {
        return filterButton;
    }
}
