/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.sigmah.client.page.dashboard;

import java.util.Arrays;

import org.sigmah.client.EventBus;
import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.event.NavigationEvent;
import org.sigmah.client.i18n.I18N;
import org.sigmah.client.icon.IconImageBundle;
import org.sigmah.client.page.NavigationCallback;
import org.sigmah.client.page.NavigationHandler;
import org.sigmah.client.page.Page;
import org.sigmah.client.page.PageId;
import org.sigmah.client.page.PageState;
import org.sigmah.client.page.TabPage;
import org.sigmah.client.page.charts.ChartPageState;
import org.sigmah.client.page.config.DbListPageState;
import org.sigmah.client.page.entry.SiteGridPageState;
import org.sigmah.client.page.map.MapPageState;
import org.sigmah.client.page.project.ProjectState;
import org.sigmah.client.page.report.ReportHomePageState;
import org.sigmah.client.page.table.PivotPageState;
import org.sigmah.shared.command.GetProjects;
import org.sigmah.shared.command.result.ProjectListResult;
import org.sigmah.shared.dto.ProjectDTO;

import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.GridEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.util.Padding;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.VBoxLayout;
import com.extjs.gxt.ui.client.widget.layout.VBoxLayoutData;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

/**
 * Home screen of sigmah. Displays the main menu and a reminder of urgent tasks.
 * @author Raphaël Calabro (rcalabro@ideia.fr)
 */
public class DashboardPresenter implements Page, TabPage {
    public static final PageId PAGE_ID = new PageId("welcome");
    
    private EventBus eventBus;
    private Dispatcher dispatcher;
    private Widget widget;
    private ListStore<ProjectDTO> projectStore;

    @Inject
    public DashboardPresenter(final EventBus eventBus, final Dispatcher dispatcher) {
        this.eventBus = eventBus;
        this.dispatcher = dispatcher;
        
        // The dashboard itself
        final ContentPanel dashboardPanel = new ContentPanel(new BorderLayout());
        dashboardPanel.setHeaderVisible(false);
        dashboardPanel.setBorders(false);
        
        // Left bar
        final ContentPanel leftPanel = new ContentPanel();
        final VBoxLayout leftPanelLayout = new VBoxLayout();
        leftPanelLayout.setVBoxLayoutAlign(VBoxLayout.VBoxLayoutAlign.STRETCH);
        leftPanelLayout.setPadding(new Padding(0));
        leftPanel.setLayout(leftPanelLayout);
        leftPanel.setHeaderVisible(false);
        leftPanel.setBorders(false);
        leftPanel.setBodyBorder(false);
        
            // Left bar content
            final VBoxLayoutData vBoxLayoutData = new VBoxLayoutData();
            vBoxLayoutData.setFlex(1.0);
        
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
            
                // Menu
                addNavLink(menuPanel, I18N.CONSTANTS.dataEntry(), IconImageBundle.ICONS.dataEntry(), new SiteGridPageState());
                addNavLink(menuPanel, I18N.CONSTANTS.reports(), IconImageBundle.ICONS.report(), new ReportHomePageState());
                addNavLink(menuPanel, I18N.CONSTANTS.charts(), IconImageBundle.ICONS.barChart(), new ChartPageState());
                addNavLink(menuPanel, I18N.CONSTANTS.maps(), IconImageBundle.ICONS.map(), new MapPageState());
                addNavLink(menuPanel, I18N.CONSTANTS.tables(), IconImageBundle.ICONS.table(), new PivotPageState());
                addNavLink(menuPanel, I18N.CONSTANTS.setup(), IconImageBundle.ICONS.setup(), new DbListPageState());
        
            leftPanel.add(menuPanel, vBoxLayoutData);
            
        final BorderLayoutData leftLayoutData = new BorderLayoutData(LayoutRegion.WEST, 250);
        leftLayoutData.setSplit(true);
        dashboardPanel.add(leftPanel, leftLayoutData);
            
        // Main panel
        final ContentPanel mainPanel = new ContentPanel(new VBoxLayout());
        final VBoxLayout mainPanelLayout = new VBoxLayout();
        mainPanelLayout.setVBoxLayoutAlign(VBoxLayout.VBoxLayoutAlign.STRETCH);
        mainPanel.setLayout(mainPanelLayout);
        mainPanel.setHeaderVisible(false);
        mainPanel.setBorders(false);
        mainPanel.setBodyBorder(false);
        
            // Mission tree panel
            final ContentPanel missionTreePanel = new ContentPanel();
            missionTreePanel.setHeading(I18N.CONSTANTS.location());
            final VBoxLayoutData smallVBoxLayoutData = new VBoxLayoutData();
            smallVBoxLayoutData.setFlex(1.0);
            mainPanel.add(missionTreePanel, smallVBoxLayoutData);
            
            // Project tree panel
            final ContentPanel projectTreePanel = new ContentPanel(new FitLayout());
            projectTreePanel.setHeading(I18N.CONSTANTS.projects());
            final VBoxLayoutData largeVBoxLayoutData = new VBoxLayoutData();
            largeVBoxLayoutData.setFlex(2.0);
            mainPanel.add(projectTreePanel, largeVBoxLayoutData);
            
                // Project list
                projectStore = new ListStore<ProjectDTO>();
                final ColumnConfig id = new ColumnConfig("id", "ID", 100);
                final ColumnConfig name = new ColumnConfig("name", "Name", 200);
                final ColumnConfig owner = new ColumnConfig("owner", "Owner", 100);
                final ColumnModel columnModel = new ColumnModel(Arrays.asList(id, name, owner));
                final Grid projectGrid = new Grid<ProjectDTO>(projectStore, columnModel);
                
                projectGrid.addListener(Events.CellDoubleClick, new Listener<GridEvent>() {
                    @Override
                    public void handleEvent(GridEvent be) {
                        final ProjectDTO project = projectStore.getAt(be.getRowIndex());
                        eventBus.fireEvent(new NavigationEvent(NavigationHandler.NavigationRequested, new ProjectState(project.getId())));
                    }
                });
                
                projectTreePanel.add(projectGrid);
                
        final BorderLayoutData mainLayoutData = new BorderLayoutData(LayoutRegion.CENTER);
        dashboardPanel.add(mainPanel, mainLayoutData);
        
        widget = dashboardPanel;
    }
    
    private void addNavLink(ContentPanel panel, String text, AbstractImagePrototype icon, final PageState place) {
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
    
    @Override
    public PageId getPageId() {
        return PAGE_ID;
    }

    @Override
    public Object getWidget() {
        return widget;
    }

    @Override
    public void requestToNavigateAway(PageState place, NavigationCallback callback) {
        callback.onDecided(true);
    }

    @Override
    public String beforeWindowCloses() {
        return null;
    }

    @Override
    public boolean navigate(PageState place) {
        dispatcher.execute(new GetProjects(), null, new AsyncCallback<ProjectListResult>() {
            @Override
            public void onFailure(Throwable throwable) {

            }

            @Override
            public void onSuccess(ProjectListResult projectList) {
                projectStore.add(projectList.getList());
            }
        });
        
        return true;
    }

    @Override
    public void shutdown() {
    }

    @Override
    public String getTabTitle() {
        return I18N.CONSTANTS.dashboard();
    }
}
