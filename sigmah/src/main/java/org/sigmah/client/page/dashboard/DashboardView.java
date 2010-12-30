/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.page.dashboard;

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
import org.sigmah.client.page.report.ReportListPageState;
import org.sigmah.client.page.table.PivotPageState;
import org.sigmah.client.ui.StylableVBoxLayout;
import org.sigmah.client.util.Notification;
import org.sigmah.shared.dto.OrgUnitDTOLight;
import org.sigmah.shared.dto.ProjectDTOLight;

import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.TreeStore;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.util.Padding;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.VBoxLayout;
import com.extjs.gxt.ui.client.widget.layout.VBoxLayoutData;
import com.extjs.gxt.ui.client.widget.treegrid.TreeGrid;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
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

    private ProjectsListPanel projectsListPanel;

    private OrgUnitTreeGrid orgUnitsTreeGrid;
    private ContentPanel orgUnitsPanel;

    @Inject
    public DashboardView(final EventBus eventBus, final Dispatcher dispatcher, final Authentication authentication,
            final UserInfo info) {

        this.dispatcher = dispatcher;
        this.eventBus = eventBus;
        this.authentication = authentication;
        this.info = info;

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

                                projectsListPanel.getProjectsStore().clearFilters();
                                projectsListPanel.getProjectsStore().add(project, false);
                                projectsListPanel.getProjectsStore().applyFilters(null);

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

        projectsListPanel = new ProjectsListPanel(dispatcher, authentication);
        return projectsListPanel.getProjectsPanel();
    }

    @Override
    public ProjectsListPanel getProjectsListPanel() {
        return projectsListPanel;
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
    public ContentPanel getOrgUnitsPanel() {
        return orgUnitsPanel;
    }
}
