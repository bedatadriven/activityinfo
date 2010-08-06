/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.sigmah.client.page.dashboard;


import org.sigmah.shared.command.result.ProjectListResult;

import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.store.TreeStore;
import com.extjs.gxt.ui.client.util.Padding;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.grid.CheckBoxSelectionModel;
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
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
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
import org.sigmah.client.page.charts.ChartPageState;
import org.sigmah.client.page.common.toolbar.ActionListener;
import org.sigmah.client.page.common.toolbar.ActionToolBar;
import org.sigmah.client.page.common.toolbar.UIActions;
import org.sigmah.client.page.config.DbListPageState;
import org.sigmah.client.page.entry.SiteGridPageState;
import org.sigmah.client.page.map.MapPageState;
import org.sigmah.client.page.project.ProjectPresenter;
import org.sigmah.client.page.report.ReportHomePageState;
import org.sigmah.client.page.table.PivotPageState;
import org.sigmah.shared.command.GetCountries;
import org.sigmah.shared.command.GetProjects;
import org.sigmah.shared.command.result.CountryResult;
import org.sigmah.shared.dto.CountryDTO;
import org.sigmah.shared.dto.ProjectDTO;

/**
 * Home screen of sigmah. Displays the main menu and a reminder of urgent tasks.
 * @author Raphaël Calabro (rcalabro@ideia.fr)
 */
public class DashboardPresenter implements Page {
    public static final PageId PAGE_ID = new PageId("welcome");
    
    private EventBus eventBus;
    private Dispatcher dispatcher;
    private Widget widget;
    
    /**
     * Model containing the displayed projects
     */
    private TreeStore<ProjectDTO> projectStore;
    
    /**
     * Model containing the countries available to the user.
     */
    private ListStore<CountryDTO> countryStore;

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
        
            // Country list panel
            final ContentPanel missionTreePanel = new ContentPanel(new FitLayout());
            missionTreePanel.setHeading(I18N.CONSTANTS.location());
            final VBoxLayoutData smallVBoxLayoutData = new VBoxLayoutData();
            smallVBoxLayoutData.setFlex(1.0);
            mainPanel.add(missionTreePanel, smallVBoxLayoutData);
            
                // Country list
                countryStore = new ListStore<CountryDTO>();
                
                final CheckBoxSelectionModel<CountryDTO> selectionModel = new CheckBoxSelectionModel<CountryDTO>();
                
                final ColumnConfig countryName = new ColumnConfig("name", I18N.CONSTANTS.name(), 200);
                final ColumnModel countryColumnModel = new ColumnModel(Arrays.asList(selectionModel.getColumn(), countryName));
                    
                final Grid countryGrid = new Grid(countryStore, countryColumnModel);
                countryGrid.setAutoExpandColumn("name");
                countryGrid.setSelectionModel(selectionModel);
                countryGrid.addPlugin(selectionModel);
                
                missionTreePanel.add(countryGrid);
                
                // Refresh button
                final ActionToolBar countryToolbar = new ActionToolBar(new ActionListener() {
                    @Override
                    public void onUIAction(String actionId) {
                        if(UIActions.refresh.equals(actionId)) {
                            dispatcher.execute(new GetProjects(selectionModel.getSelectedItems()), null, new AsyncCallback<ProjectListResult>() {
                                @Override
                                public void onFailure(Throwable throwable) {

                                }

                                @Override
                                public void onSuccess(ProjectListResult projectList) {
                                    projectStore.removeAll();
                                    projectStore.add(projectList.getList(), true);
                                }
                            });
                        }
                    }
                });
                
                countryToolbar.addRefreshButton(); 
                
                missionTreePanel.setTopComponent(countryToolbar);
            
            // Project tree panel
            final ContentPanel projectTreePanel = new ContentPanel(new FitLayout());
            projectTreePanel.setHeading(I18N.CONSTANTS.projects());
            final VBoxLayoutData largeVBoxLayoutData = new VBoxLayoutData();
            largeVBoxLayoutData.setFlex(2.0);
            mainPanel.add(projectTreePanel, largeVBoxLayoutData);
            
                // Project list
                projectStore = new TreeStore<ProjectDTO>();
                projectStore.setMonitorChanges(true);
                
                final ColumnConfig icon = new ColumnConfig("favorite", "-", 24);
                icon.setRenderer(new GridCellRenderer<ProjectDTO>() {
                    private final DashboardImageBundle imageBundle = GWT.create(DashboardImageBundle.class);
                
                    @Override
                    public Object render(final ProjectDTO model, String property, ColumnData config, int rowIndex, int colIndex, final ListStore<ProjectDTO> store, final Grid<ProjectDTO> grid) {
                        final Image icon;
                        
                        if(model.isFavorite())
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
                
                final ColumnConfig name = new ColumnConfig("name", I18N.CONSTANTS.name(), 200);
                name.setRenderer(new WidgetTreeGridCellRenderer() {
                    @Override
                    public Widget getWidget(ModelData model, String property, ColumnData config, int rowIndex, int colIndex, ListStore store, Grid grid) {
                        return new Hyperlink((String)model.get(property), true, ProjectPresenter.PAGE_ID.toString()+'!'+model.get("id").toString());
                    }
                });
        
                final ColumnConfig phase = new ColumnConfig("phase", "Phase", 100);
                final ColumnConfig topic = new ColumnConfig("topic", "Topic", 100);
                final ColumnModel columnModel = new ColumnModel(Arrays.asList(icon, name, phase, topic));
                    
                final TreeGrid projectTreeGrid = new TreeGrid(projectStore, columnModel);
                
                projectTreePanel.add(projectTreeGrid);
                
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
        dispatcher.execute(new GetCountries(true), null, new AsyncCallback<CountryResult>() {
            @Override
            public void onFailure(Throwable throwable) {

            }

            @Override
            public void onSuccess(CountryResult countryResult) {
                countryStore.add(countryResult.getData());
            }
        });
        
        return true;
    }

    @Override
    public void shutdown() {
    }
}
