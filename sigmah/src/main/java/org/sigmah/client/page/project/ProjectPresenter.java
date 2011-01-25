/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */
package org.sigmah.client.page.project;

import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import org.sigmah.client.CountriesList;
import org.sigmah.client.EventBus;
import org.sigmah.client.UserInfo;
import org.sigmah.client.dispatch.AsyncMonitor;
import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.dispatch.remote.Authentication;
import org.sigmah.client.event.NavigationEvent;
import org.sigmah.client.i18n.I18N;
import org.sigmah.client.page.Frame;
import org.sigmah.client.page.NavigationCallback;
import org.sigmah.client.page.NavigationHandler;
import org.sigmah.client.page.Page;
import org.sigmah.client.page.PageId;
import org.sigmah.client.page.PageState;
import org.sigmah.client.page.TabPage;
import org.sigmah.client.page.project.calendar.ProjectCalendarPresenter;
import org.sigmah.client.page.project.dashboard.ProjectDashboardPresenter;
import org.sigmah.client.page.project.dashboard.funding.FundingIconProvider;
import org.sigmah.client.page.project.details.ProjectDetailsPresenter;
import org.sigmah.client.page.project.logframe.ProjectLogFramePresenter;
import org.sigmah.client.page.project.reports.ProjectReportsPresenter;
import org.sigmah.client.ui.ToggleAnchor;
import org.sigmah.shared.command.GetProject;
import org.sigmah.shared.domain.profile.GlobalPermissionEnum;
import org.sigmah.shared.dto.PhaseDTO;
import org.sigmah.shared.dto.ProjectBannerDTO;
import org.sigmah.shared.dto.ProjectDTO;
import org.sigmah.shared.dto.element.DefaultFlexibleElementDTO;
import org.sigmah.shared.dto.element.FlexibleElementDTO;
import org.sigmah.shared.dto.layout.LayoutConstraintDTO;
import org.sigmah.shared.dto.layout.LayoutDTO;
import org.sigmah.shared.dto.layout.LayoutGroupDTO;
import org.sigmah.shared.dto.profile.ProfileUtils;

import com.allen_sauer.gwt.log.client.Log;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.LabelField;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.extjs.gxt.ui.client.widget.layout.HBoxLayout;
import com.extjs.gxt.ui.client.widget.layout.HBoxLayoutData;
import com.extjs.gxt.ui.client.widget.layout.VBoxLayoutData;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.ImplementedBy;
import com.google.inject.Inject;
import java.util.EnumMap;
import java.util.Map;
import org.sigmah.shared.command.AmendmentAction;
import org.sigmah.shared.domain.Amendment;
import org.sigmah.shared.dto.AmendmentDTO;

/**
 * Project presenter which manages the {@link ProjectView}.
 * 
 * @author Denis Colliot (dcolliot@ideia.fr)
 */
public class ProjectPresenter implements Frame, TabPage {

    public static final PageId PAGE_ID = new PageId("project");

    public static final int REPORT_TAB_INDEX = 5;

    /**
     * Description of the view managed by this presenter.
     */
    @ImplementedBy(ProjectView.class)
    public interface View {

        public ContentPanel getPanelProjectBanner();

        public ContentPanel getTabPanel();

        public ContentPanel getAmendmentBox();

        public void setMainPanel(Widget widget);

        public void insufficient();

        public void sufficient();
    }

    private final View view;
    private final Dispatcher dispatcher;
    private final Authentication authentication;
    private final CountriesList countriesList;
    private Page activePage;

    private ProjectState currentState;
    private ToggleAnchor currentTab;
    /**
     * The current displayed project.
     */
    private ProjectDTO currentProjectDTO;
    /**
     * The current displayed phase.
     */
    private PhaseDTO currentDisplayedPhaseDTO;

    private final static Map<Amendment.Action, String> amendmentActionDisplayNames;

    static {
        final EnumMap<Amendment.Action, String> map = new EnumMap<Amendment.Action, String>(Amendment.Action.class);
        map.put(Amendment.Action.CREATE, I18N.CONSTANTS.amendmentCreate());
        map.put(Amendment.Action.LOCK, I18N.CONSTANTS.amendmentLock());
        map.put(Amendment.Action.UNLOCK, I18N.CONSTANTS.amendmentUnlock());
        map.put(Amendment.Action.REJECT, I18N.CONSTANTS.amendmentReject());
        map.put(Amendment.Action.VALIDATE, I18N.CONSTANTS.amendmentValidate());

        amendmentActionDisplayNames = map;
    }

    private final static String[] MAIN_TABS = { I18N.CONSTANTS.projectTabDashboard(), I18N.CONSTANTS.projectDetails(),
            I18N.CONSTANTS.projectTabLogFrame(), I18N.CONSTANTS.projectTabIndicators(),
            I18N.CONSTANTS.projectTabCalendar(), I18N.CONSTANTS.projectTabReports(),
            I18N.CONSTANTS.projectTabSecurityIncident() };
    private final SubPresenter[] presenters;

    @Inject
    public ProjectPresenter(final Dispatcher dispatcher, View view, Authentication authentication,
            final EventBus eventBus, final UserInfo info, final CountriesList countriesList) {
        this.dispatcher = dispatcher;
        this.view = view;
        this.authentication = authentication;
        this.countriesList = countriesList;

        final DummyPresenter dummyPresenter = new DummyPresenter(); // For
                                                                    // development

        this.presenters = new SubPresenter[] {
                new ProjectDashboardPresenter(dispatcher, eventBus, authentication, this, info, countriesList), // Dashboard
                new ProjectDetailsPresenter(dispatcher, authentication, this, countriesList), // Details,
                new ProjectLogFramePresenter(dispatcher, authentication, this), // Logical
                // Framework
                dummyPresenter, // Indicators
                new ProjectCalendarPresenter(dispatcher, authentication, this), // Calendar
                new ProjectReportsPresenter(authentication, dispatcher, eventBus, this), // Reports
                dummyPresenter // Security incidents
        };

        for (int i = 0; i < MAIN_TABS.length; i++) {
            final int index = i;

            String tabTitle = MAIN_TABS[i];

            final HBoxLayoutData layoutData = new HBoxLayoutData();
            layoutData.setMargins(new Margins(0, 10, 0, 0));

            final ToggleAnchor anchor = new ToggleAnchor(tabTitle);
            anchor.setAnchorMode(true);

            anchor.addClickHandler(new ClickHandler() {

                @Override
                public void onClick(ClickEvent event) {
                    eventBus.fireEvent(new NavigationEvent(NavigationHandler.NavigationRequested, currentState
                            .deriveTo(index)));
                }
            });

            this.view.getTabPanel().add(anchor, layoutData);
        }
    }

    private void selectTab(int index, boolean force) {
        final ToggleAnchor anchor = (ToggleAnchor) this.view.getTabPanel().getWidget(index);

        if (currentTab != anchor) {
            if (currentTab != null)
                currentTab.toggleAnchorMode();

            anchor.toggleAnchorMode();
            currentTab = anchor;

            ProjectPresenter.this.view.setMainPanel(presenters[index].getView());
            presenters[index].viewDidAppear();
        } else if (force) {
            ProjectPresenter.this.view.setMainPanel(presenters[index].getView());
            presenters[index].viewDidAppear();
        }
    }

    @Override
    public boolean navigate(final PageState place) {
        final ProjectState projectState = (ProjectState) place;
        final int projectId = projectState.getProjectId();

        if (ProfileUtils.isGranted(authentication, GlobalPermissionEnum.VIEW_PROJECT)) {

            view.sufficient();

            if (currentProjectDTO == null || projectId != currentProjectDTO.getId()) {
                if (Log.isDebugEnabled()) {
                    Log.debug("Loading project #" + projectId + "...");
                }

                dispatcher.execute(new GetProject(projectId), null, new AsyncCallback<ProjectDTO>() {

                    @Override
                    public void onFailure(Throwable throwable) {
                        Log.error("Error, project #" + projectId + " not loaded.");
                    }

                    @Override
                    public void onSuccess(ProjectDTO projectDTO) {

                        if (projectDTO == null) {
                            Log.error("Project not loaded : " + projectId);
                            view.insufficient();
                        } else {

                            if (Log.isDebugEnabled()) {
                                Log.debug("Project loaded : " + projectDTO.getName());
                            }

                            currentState = projectState;

                            boolean projectChanged = !projectDTO.equals(currentProjectDTO);

                            projectState.setTabTitle(projectDTO.getName());
                            loadProjectOnView(projectDTO);

                            selectTab(projectState.getCurrentSection(), projectChanged);
                        }
                    }
                });
            } else {
                boolean change = false;

                if (!currentState.equals(projectState)) {
                    change = true;
                    currentState = projectState;
                }

                selectTab(projectState.getCurrentSection(), change);
            }
        } else {
            view.insufficient();
        }

        return true;
    }

    /**
     * Loads a {@link ProjectDTO} object on the view.
     * 
     * @param projectDTO
     *            the {@link ProjectDTO} object loaded on the view
     */
    private void loadProjectOnView(ProjectDTO projectDTO) {
        currentProjectDTO = projectDTO;
        currentDisplayedPhaseDTO = projectDTO.getCurrentPhaseDTO();

        refreshBanner();
        refreshAmendment();

        // TODO: Call the sub-presenter
    }

    public ProjectDTO getCurrentProjectDTO() {
        return currentProjectDTO;
    }

    public void setCurrentProjectDTO(ProjectDTO currentProjectDTO) {
        this.currentProjectDTO = currentProjectDTO;
    }

    public PhaseDTO getCurrentDisplayedPhaseDTO() {
        return currentDisplayedPhaseDTO;
    }

    public void setCurrentDisplayedPhaseDTO(PhaseDTO currentPhaseDTO) {
        this.currentDisplayedPhaseDTO = currentPhaseDTO;
    }

    /**
     * Refreshes the project banner for the current project.
     */
    public void refreshBanner() {

        // Panel.
        final ContentPanel panel = view.getPanelProjectBanner();
        panel.setHeading(I18N.CONSTANTS.projectMainTabTitle() + ' ' + currentProjectDTO.getName() + " ("
                + currentProjectDTO.getFullName() + ") : " + I18N.CONSTANTS.projectInfos());
        panel.removeAll();

        final Grid gridPanel = new Grid(1, 2);
        gridPanel.addStyleName("banner");
        gridPanel.setCellPadding(0);
        gridPanel.setCellSpacing(0);
        gridPanel.setWidth("100%");
        gridPanel.setHeight("100%");

        // Logo.
        final Image logo = FundingIconProvider.getProjectTypeIcon(
                currentProjectDTO.getProjectModelDTO().getVisibility(authentication.getOrganizationId()),
                FundingIconProvider.IconSize.LARGE).createImage();
        gridPanel.setWidget(0, 0, logo);
        gridPanel.getCellFormatter().addStyleName(0, 0, "banner-logo");

        // Banner.
        final ProjectBannerDTO banner = currentProjectDTO.getProjectModelDTO().getProjectBannerDTO();
        final LayoutDTO layout = banner.getLayoutDTO();

        // Executes layout.
        if (banner != null && layout != null && layout.getLayoutGroupsDTO() != null
                && !layout.getLayoutGroupsDTO().isEmpty()) {

            // For visibility constraints, the banner accept a maximum of 2 rows
            // and 4 columns.
            final int rows = layout.getRowsCount() > 2 ? 2 : layout.getRowsCount();
            final int cols = layout.getColumnsCount() > 4 ? 4 : layout.getColumnsCount();

            final Grid gridLayout = new Grid(rows, cols);
            gridLayout.addStyleName("banner-flex");
            gridLayout.setCellPadding(0);
            gridLayout.setCellSpacing(0);
            gridLayout.setWidth("100%");
            gridLayout.setHeight("100%");

            for (int i = 0; i < gridLayout.getColumnCount() - 1; i++) {
                gridLayout.getColumnFormatter().setWidth(i, "325px");
            }

            for (final LayoutGroupDTO groupLayout : layout.getLayoutGroupsDTO()) {

                // Checks group bounds.
                if (groupLayout.getRow() + 1 > rows || groupLayout.getColumn() + 1 > cols) {
                    continue;
                }

                final ContentPanel groupPanel = new ContentPanel();
                groupPanel.setLayout(new FormLayout());
                groupPanel.setTopComponent(null);
                groupPanel.setHeaderVisible(false);

                gridLayout.setWidget(groupLayout.getRow(), groupLayout.getColumn(), groupPanel);

                if (groupLayout.getLayoutConstraintsDTO() != null) {
                    for (final LayoutConstraintDTO constraint : groupLayout.getLayoutConstraintsDTO()) {

                        final FlexibleElementDTO element = constraint.getFlexibleElementDTO();

                        // Only default elements are allowed.
                        if (!(element instanceof DefaultFlexibleElementDTO)) {
                            continue;
                        }

                        // Builds the graphic component
                        final DefaultFlexibleElementDTO defaultElement = (DefaultFlexibleElementDTO) element;
                        defaultElement.setService(dispatcher);
                        defaultElement.setAuthentication(authentication);
                        defaultElement.setCountries(countriesList);
                        defaultElement.setCurrentContainerDTO(currentProjectDTO);

                        final Component component = defaultElement.getElementComponent(null, false);

                        if (component != null) {
                            groupPanel.add(component);
                        }

                        // Only one element per cell.
                        break;
                    }
                }
            }

            gridPanel.setWidget(0, 1, gridLayout);
        }
        // Default banner.
        else {

            panel.setLayout(new FormLayout());

            final LabelField codeField = new LabelField();
            codeField.setReadOnly(true);
            codeField.setFieldLabel(I18N.CONSTANTS.projectName());
            codeField.setLabelSeparator(":");
            codeField.setValue(currentProjectDTO.getName());

            gridPanel.setWidget(0, 1, codeField);
        }

        panel.add(gridPanel);
        panel.layout();
    }

    private void refreshAmendment() {
        Log.debug("Loading amendments for project '" + currentProjectDTO.getName() + "'...");

        final ContentPanel amendmentBox = view.getAmendmentBox();
        amendmentBox.removeAll();

        // Prepare the amendment store
        final ListStore<AmendmentDTO> store = new ListStore<AmendmentDTO>();

        for (final AmendmentDTO amendmentDTO : currentProjectDTO.getAmendments()) {
            amendmentDTO.prepareName();
            store.add(amendmentDTO);
        }

        // Adding the current amendment
        final AmendmentDTO currentAmendment = new AmendmentDTO(currentProjectDTO);
        store.add(currentAmendment);

        // Creating the amendment list
        final ComboBox<AmendmentDTO> versionList = new ComboBox<AmendmentDTO>();
        versionList.setStore(store);
        versionList.setTriggerAction(ComboBox.TriggerAction.ALL);

        versionList.setValue(currentAmendment); // Selecting the
                                                // currentAmendment

        Log.debug(store.getCount() + " amendment(s).");

        final Button displayAmendmentButton = new Button(I18N.CONSTANTS.amendmentDisplay());
        displayAmendmentButton.setEnabled(false);

        versionList.addSelectionChangedListener(new SelectionChangedListener<AmendmentDTO>() {
            @Override
            public void selectionChanged(SelectionChangedEvent<AmendmentDTO> se) {
                int currentAmendmentId = 0;
                if(currentProjectDTO.getCurrentAmendment() != null)
                    currentAmendmentId = currentProjectDTO.getCurrentAmendment().getId();

                Log.debug("Current "+currentAmendmentId+" / Selected "+se.getSelectedItem().getId());
                
                displayAmendmentButton.setEnabled(se.getSelectedItem().getId() != currentAmendmentId);
            }
        });

        displayAmendmentButton.addSelectionListener(new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                AmendmentDTO amendmentDTO = versionList.getSelection().get(0);
                if(amendmentDTO.getId() == 0)
                    amendmentDTO = null;
                else
                    Log.debug("Back to "+amendmentDTO.getId());

                currentProjectDTO.setCurrentAmendment(amendmentDTO);

                
                // Refreshing the whole view
                discardAllViews();
                selectTab(currentState.getCurrentSection(), true);
                displayAmendmentButton.setEnabled(false);
            }
        });

        final LayoutContainer amendmentListContainer = new LayoutContainer(new HBoxLayout());
        amendmentListContainer.add(versionList);
        amendmentListContainer.add(displayAmendmentButton, new HBoxLayoutData(0, 0, 0, 4));

        amendmentBox.add(amendmentListContainer, new VBoxLayoutData(0, 0, 3, 0));

        // Displaying the available actions
        final Amendment.Action[] actions;
        if(currentProjectDTO.getAmendmentState() != null)
            actions = currentProjectDTO.getAmendmentState().getActions();
        else
            actions = new Amendment.Action[0];
        final Anchor[] anchors = new Anchor[actions.length];

        for (int index = 0; index < actions.length; index++) {
            final Amendment.Action action = actions[index];
            Log.debug("Adding the " + action + " amendment action.");

            final Anchor actionAnchor = new Anchor(amendmentActionDisplayNames.get(action));
            actionAnchor.addStyleName("amendment-action");

            actionAnchor.addClickHandler(new ClickHandler() {
                @Override
                public void onClick(ClickEvent event) {
                    // Disabling every actions before sending the request
                    amendmentBox.mask(I18N.CONSTANTS.loading());

                    for (final Anchor anchor : anchors)
                        anchor.setEnabled(false);

                    final AmendmentAction amendmentAction = new AmendmentAction(currentProjectDTO.getId(), action);
                    dispatcher.execute(amendmentAction, null, new AsyncCallback<ProjectDTO>() {

                        @Override
                        public void onFailure(Throwable caught) {
                            // Failures may happen if an other user changes the
                            // amendment state.
                            // TODO: we should maybe refresh the project or tell
                            // the user to refresh the page.
                            MessageBox.alert(amendmentActionDisplayNames.get(action),
                                    I18N.CONSTANTS.amendmentActionError(), null);
                            for (final Anchor anchor : anchors)
                                anchor.setEnabled(true);
                            amendmentBox.unmask();
                        }

                        @Override
                        public void onSuccess(ProjectDTO result) {
                            for (final Anchor anchor : anchors)
                                anchor.setEnabled(true);

                            // Updating the current project
                            currentProjectDTO = result;

                            // Refreshing the whole view
                            discardAllViews();
                            selectTab(currentState.getCurrentSection(), true);
                            refreshAmendment();

                            amendmentBox.unmask();
                        }

                    });
                }
            });

            // TOM: This is were the amendment actions are added to the view
            amendmentBox.add(actionAnchor, new VBoxLayoutData());
            anchors[index] = actionAnchor;
        }

        amendmentBox.layout();
    }

    private void discardAllViews() {
        for (final SubPresenter presenter : presenters)
            presenter.discardView();
    }

    @Override
    public String getTabTitle() {
        return I18N.CONSTANTS.projectMainTabTitle();
    }

    @Override
    public PageId getPageId() {
        return PAGE_ID;
    }

    @Override
    public Object getWidget() {
        return view;
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
    public void shutdown() {
    }

    @Override
    public void setActivePage(Page page) {
        this.activePage = page;
    }

    @Override
    public Page getActivePage() {
        return this.activePage;
    }

    public ProjectState getCurrentState() {
        return currentState;
    }

    @Override
    public AsyncMonitor showLoadingPlaceHolder(PageId pageId, PageState loadingPlace) {
        return null;
    }
}
