/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.page.dashboard;

import java.util.Arrays;

import org.sigmah.client.EventBus;
import org.sigmah.client.UserInfo;
import org.sigmah.client.dispatch.Dispatcher;
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
import org.sigmah.client.page.report.ReportListPageState;
import org.sigmah.client.page.table.PivotPageState;
import org.sigmah.client.ui.RatioBar;
import org.sigmah.client.ui.StylableVBoxLayout;
import org.sigmah.client.util.Notification;
import org.sigmah.client.util.NumberUtils;
import org.sigmah.shared.dto.OrgUnitDTOLight;
import org.sigmah.shared.dto.ProjectDTOLight;

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
import com.extjs.gxt.ui.client.widget.button.Button;
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
import com.extjs.gxt.ui.client.widget.treegrid.TreeGrid;
import com.extjs.gxt.ui.client.widget.treegrid.WidgetTreeGridCellRenderer;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Image;
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
    private final TreeStore<ProjectDTOLight> projectStore;

    private Button loadProjectsButton;
    private ContentPanel projectsPanel;
    private OrgUnitTreeGrid orgUnitsTreeGrid;
    private ContentPanel orgUnitsPanel;

    @Inject
    public DashboardView(final EventBus eventBus, final Dispatcher dispatcher, final Authentication authentication,
            final UserInfo info) {

        this.dispatcher = dispatcher;
        this.eventBus = eventBus;
        this.authentication = authentication;
        this.info = info;

        // Initialization of the models
        projectStore = new TreeStore<ProjectDTOLight>();
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
        remindersPanel.setHeading(I18N.CONSTANTS.reminders());
        leftPanel.add(remindersPanel, vBoxLayoutData);

        final ContentPanel importantPointsPanel = new ContentPanel(new FitLayout());
        importantPointsPanel.setHeading(I18N.CONSTANTS.importantPoints());
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

                    private final CreateProjectWindow window = new CreateProjectWindow(dispatcher, authentication, info);

                    {
                        window.addListener(new CreateProjectListener() {

                            @Override
                            public void projectCreated(ProjectDTOLight project) {

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

        orgUnitsTreeGrid = new OrgUnitTreeGrid(eventBus, true);

        // Refresh button.
        loadProjectsButton = new Button(I18N.CONSTANTS.refreshPreview(), IconImageBundle.ICONS.refresh());
        orgUnitsTreeGrid.addToolbarButton(loadProjectsButton);

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

        // Store.
        projectStore.setStoreSorter(new StoreSorter<ProjectDTOLight>() {
            @Override
            public int compare(Store<ProjectDTOLight> store, ProjectDTOLight m1, ProjectDTOLight m2, String property) {

                if ("name".equals(property)) {
                    return m1.getName().compareToIgnoreCase(m2.getName());
                } else if ("phase".equals(property)) {
                    return m1.getCurrentPhaseDTO().getPhaseModelDTO().getName()
                            .compareToIgnoreCase(m2.getCurrentPhaseDTO().getPhaseModelDTO().getName());
                } else {
                    return super.compare(store, m1, m2, property);
                }
            }
        });

        // Panel
        final ContentPanel projectTreePanel = new ContentPanel(new FitLayout());
        projectTreePanel.setHeading(I18N.CONSTANTS.projects());

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

        // Starred icon
        final ColumnConfig starredIconColumn = new ColumnConfig("favorite", "", 24);
        starredIconColumn.setSortable(false);
        starredIconColumn.setRenderer(new GridCellRenderer<ProjectDTOLight>() {
            private final DashboardImageBundle imageBundle = GWT.create(DashboardImageBundle.class);

            @Override
            public Object render(final ProjectDTOLight model, String property, ColumnData config, int rowIndex,
                    int colIndex, final ListStore<ProjectDTOLight> store, final Grid<ProjectDTOLight> grid) {
                final Image icon;

                if (model.isFavorite())
                    icon = imageBundle.star().createImage();
                else
                    icon = imageBundle.emptyStar().createImage();

                icon.addClickHandler(new ClickHandler() {
                    @Override
                    public void onClick(ClickEvent event) {
                        model.setFavorite(!model.isFavorite());
                        // TODO: Save the changes
                    }
                });

                return icon;
            }
        });

        // Code
        final ColumnConfig codeColumn = new ColumnConfig("name", I18N.CONSTANTS.projectName(), 200);
        codeColumn.setRenderer(new WidgetTreeGridCellRenderer<ProjectDTOLight>() {
            @Override
            public Widget getWidget(ProjectDTOLight model, String property, ColumnData config, int rowIndex,
                    int colIndex, ListStore<ProjectDTOLight> store, Grid<ProjectDTOLight> grid) {
                return new Hyperlink((String) model.get(property), true, ProjectPresenter.PAGE_ID.toString() + '!'
                        + model.get("id").toString());
            }
        });

        // Title
        final ColumnConfig titleColumn = new ColumnConfig("fullName", I18N.CONSTANTS.projectFullName(), 200);

        // Current phase
        final ColumnConfig currentPhaseName = new ColumnConfig("phase", I18N.CONSTANTS.projectActivePhase(), 100);
        currentPhaseName.setRenderer(new GridCellRenderer<ProjectDTOLight>() {
            @Override
            public Object render(ProjectDTOLight model, String property, ColumnData config, int rowIndex, int colIndex,
                    ListStore<ProjectDTOLight> store, Grid<ProjectDTOLight> grid) {
                return model.getCurrentPhaseDTO().getPhaseModelDTO().getName();
            }
        });

        // Org Unit
        final ColumnConfig orgUnitColumn = new ColumnConfig("orgUnitName", I18N.CONSTANTS.orgunit(), 200);

        // Spent budget
        final ColumnConfig spentBudgetColumn = new ColumnConfig("spentBudget", I18N.CONSTANTS.projectSpendBudget(), 100);
        spentBudgetColumn.setRenderer(new GridCellRenderer<ProjectDTOLight>() {

            @Override
            public Object render(ProjectDTOLight model, String property, ColumnData config, int rowIndex, int colIndex,
                    ListStore<ProjectDTOLight> store, Grid<ProjectDTOLight> grid) {
                return new RatioBar(NumberUtils.ratio(model.getSpendBudget(), model.getPlannedBudget()));
            }
        });

        return new ColumnModel(Arrays.asList(starredIconColumn, codeColumn, titleColumn, currentPhaseName,
                orgUnitColumn, spentBudgetColumn));
    }

    @Override
    public TreeStore<ProjectDTOLight> getProjectsStore() {
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
    public Button getLoadProjectsButton() {
        return loadProjectsButton;
    }

    @Override
    public ContentPanel getProjectsPanel() {
        return projectsPanel;
    }

    @Override
    public ContentPanel getOrgUnitsPanel() {
        return orgUnitsPanel;
    }
}
