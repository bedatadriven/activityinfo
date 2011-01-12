package org.sigmah.client.page.dashboard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.dispatch.monitor.MaskingAsyncMonitor;
import org.sigmah.client.dispatch.remote.Authentication;
import org.sigmah.client.i18n.I18N;
import org.sigmah.client.icon.IconImageBundle;
import org.sigmah.client.page.project.ProjectPresenter;
import org.sigmah.client.page.project.category.CategoryIconProvider;
import org.sigmah.client.page.project.dashboard.funding.FundingIconProvider;
import org.sigmah.client.page.project.dashboard.funding.FundingIconProvider.IconSize;
import org.sigmah.client.ui.RatioBar;
import org.sigmah.client.util.Notification;
import org.sigmah.client.util.NumberUtils;
import org.sigmah.shared.command.GetProjects;
import org.sigmah.shared.command.UpdateProject;
import org.sigmah.shared.command.result.ProjectListResult;
import org.sigmah.shared.command.result.VoidResult;
import org.sigmah.shared.domain.ProjectModelType;
import org.sigmah.shared.domain.profile.GlobalPermissionEnum;
import org.sigmah.shared.dto.ProjectDTOLight;
import org.sigmah.shared.dto.category.CategoryElementDTO;
import org.sigmah.shared.dto.element.DefaultFlexibleElementDTO;
import org.sigmah.shared.dto.element.handler.ValueEvent;
import org.sigmah.shared.dto.profile.ProfileUtils;

import com.allen_sauer.gwt.log.client.Log;
import com.extjs.gxt.ui.client.Style.SortDir;
import com.extjs.gxt.ui.client.data.SortInfo;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.FieldEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.store.Store;
import com.extjs.gxt.ui.client.store.StoreEvent;
import com.extjs.gxt.ui.client.store.StoreFilter;
import com.extjs.gxt.ui.client.store.StoreSorter;
import com.extjs.gxt.ui.client.store.TreeStore;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
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
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.FlowData;
import com.extjs.gxt.ui.client.widget.layout.FlowLayout;
import com.extjs.gxt.ui.client.widget.toolbar.SeparatorToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.extjs.gxt.ui.client.widget.treegrid.TreeGrid;
import com.extjs.gxt.ui.client.widget.treegrid.WidgetTreeGridCellRenderer;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

/**
 * A widget which display a list of projects.
 * 
 * @author tmi
 * 
 */
public class ProjectsListPanel {

    /**
     * A tree store with some useful dedicated methods.
     * 
     * @author tmi
     * 
     */
    public static class ProjectStore extends TreeStore<ProjectDTOLight> {
    }

    private final Dispatcher dispatcher;
    private final Authentication authentication;
    private final ContentPanel projectTreePanel;
    private final TreeGrid<ProjectDTOLight> projectTreeGrid;
    private final Radio ngoRadio;
    private final Radio fundingRadio;
    private final Radio partnerRadio;
    private final Button filterButton;

    // Current projects grid parameters.
    private ProjectModelType currentModelType;
    private final ArrayList<Integer> orgUnitsIds;

    public ProjectsListPanel(Dispatcher dispatcher, Authentication authentication) {

        this.dispatcher = dispatcher;
        this.authentication = authentication;

        // Default filters parameters.
        orgUnitsIds = new ArrayList<Integer>();
        currentModelType = ProjectModelType.NGO;

        // Store.
        final ProjectStore projectStore = new ProjectStore();
        projectStore.setMonitorChanges(true);

        // Default sort order of the projects grid.
        projectStore.setSortInfo(new SortInfo("name", SortDir.ASC));

        // Grid.
        projectTreeGrid = new TreeGrid<ProjectDTOLight>(projectStore, getProjectGridColumnModel());
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
                    return 0;
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
        projectTreePanel = new ContentPanel(new FitLayout());
        projectTreePanel.setHeading(I18N.CONSTANTS.projects());

        projectTreePanel.setTopComponent(toolbar);

        if (ProfileUtils.isGranted(authentication, GlobalPermissionEnum.VIEW_PROJECT)) {
            projectTreePanel.add(projectTreeGrid);
        } else {
            final HTML insufficient = new HTML(I18N.CONSTANTS.permViewProjectInsufficient());
            insufficient.addStyleName("important-label");
            projectTreePanel.add(insufficient);
        }

        addListeners();
        addFilters();
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
                                projectTreePanel, I18N.CONSTANTS.loading()), new AsyncCallback<VoidResult>() {

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
        categoryColumn.setSortable(false);
        categoryColumn.setRenderer(new GridCellRenderer<ProjectDTOLight>() {

            @Override
            public Object render(ProjectDTOLight model, String property, ColumnData config, int rowIndex, int colIndex,
                    ListStore<ProjectDTOLight> store, Grid<ProjectDTOLight> grid) {

                final List<CategoryElementDTO> elements = model.getCategoryElements();
                final LayoutContainer panel = new LayoutContainer();
                panel.setLayout(new FlowLayout());
                final FlowData data = new FlowData(new Margins(0, 5, 0, 0));

                if (elements != null) {
                    for (final CategoryElementDTO element : elements) {
                        panel.add(CategoryIconProvider.getIcon(element), data);
                    }
                }

                return panel;
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

    private void addListeners() {

        // Updates the projects grid heading when the store is filtered.
        projectTreeGrid.getTreeStore().addListener(Store.Filter, new Listener<StoreEvent<ProjectDTOLight>>() {

            @Override
            public void handleEvent(StoreEvent<ProjectDTOLight> be) {
                projectTreePanel.setHeading(I18N.CONSTANTS.projects() + " ("
                        + projectTreeGrid.getTreeStore().getChildCount() + ')');
            }
        });

        // Adds actions on filter by model type.
        for (final ProjectModelType type : ProjectModelType.values()) {
            getRadioFilter(type).addListener(Events.Change, new Listener<FieldEvent>() {

                @Override
                public void handleEvent(FieldEvent be) {
                    if (Boolean.TRUE.equals(be.getValue())) {
                        currentModelType = type;
                        applyProjectFilters();
                    }
                }
            });
        }
    }

    private void addFilters() {

        // The filter by model type.
        final StoreFilter<ProjectDTOLight> typeFilter = new StoreFilter<ProjectDTOLight>() {

            @Override
            public boolean select(Store<ProjectDTOLight> store, ProjectDTOLight parent, ProjectDTOLight item,
                    String property) {

                boolean selected = false;

                // Root item.
                if (item.getParent() == null) {
                    // A root item is filtered if its type doesn't match the
                    // current type.
                    selected = item.getVisibility(authentication.getOrganizationId()) == currentModelType;
                }
                // Child item
                else {
                    // A child item is filtered if its parent is filtered.
                    selected = ((ProjectDTOLight) item.getParent()).getVisibility(authentication.getOrganizationId()) == currentModelType;
                }

                return selected;
            }
        };

        getProjectsStore().addFilter(typeFilter);

        // Filters aren't used for the moment.
        filterButton.setVisible(false);
    }

    private void applyProjectFilters() {
        getProjectsStore().applyFilters(null);
    }

    private Radio getRadioFilter(ProjectModelType type) {

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

    /**
     * Refreshes the projects grid with the current parameters.
     */
    private void refreshProjectGrid() {

        // Checks that the user can view projects.
        if (!ProfileUtils.isGranted(authentication, GlobalPermissionEnum.VIEW_PROJECT)) {
            return;
        }

        // Retrieves all the projects in the org units. The filters on type,
        // etc. are applied locally.
        final GetProjects cmd = new GetProjects();
        cmd.setOrgUnitsIds(orgUnitsIds);

        dispatcher.execute(cmd, new MaskingAsyncMonitor(projectTreePanel, I18N.CONSTANTS.loading()),
                new AsyncCallback<ProjectListResult>() {

                    @Override
                    public void onFailure(Throwable e) {
                        Log.error("[GetProjects command] Error while getting projects.", e);
                        // nothing
                    }

                    @Override
                    public void onSuccess(ProjectListResult result) {

                        getProjectsStore().removeAll();
                        getProjectsStore().clearFilters();

                        if (result != null) {
                            final List<ProjectDTOLight> resultList = result.getList();
                            getProjectsStore().add(resultList, true);
                        }

                        applyProjectFilters();
                    }
                });
    }

    public ContentPanel getProjectsPanel() {
        return projectTreePanel;
    }

    public TreeGrid<ProjectDTOLight> getProjectsTreeGrid() {
        return projectTreeGrid;
    }

    public ProjectStore getProjectsStore() {
        return (ProjectStore) projectTreeGrid.getTreeStore();
    }

    public void refresh(Integer... orgUnitsIds) {
        refresh(Arrays.asList(orgUnitsIds));
    }

    public void refresh(List<Integer> orgUnitsIds) {
        this.orgUnitsIds.clear();
        this.orgUnitsIds.addAll(orgUnitsIds);
        refreshProjectGrid();
    }
}
