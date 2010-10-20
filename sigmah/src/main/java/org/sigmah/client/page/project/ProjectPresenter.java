/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */
package org.sigmah.client.page.project;

import org.sigmah.client.dispatch.AsyncMonitor;
import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.dispatch.remote.Authentication;
import org.sigmah.client.i18n.I18N;
import org.sigmah.client.page.Frame;
import org.sigmah.client.page.NavigationCallback;
import org.sigmah.client.page.Page;
import org.sigmah.client.page.PageId;
import org.sigmah.client.page.PageState;
import org.sigmah.client.page.TabPage;
import org.sigmah.shared.command.GetProject;
import org.sigmah.shared.dto.PhaseDTO;
import org.sigmah.shared.dto.ProjectDTO;

import com.allen_sauer.gwt.log.client.Log;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Label;
import com.extjs.gxt.ui.client.widget.layout.HBoxLayoutData;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.ImplementedBy;
import com.google.inject.Inject;
import org.sigmah.client.EventBus;
import org.sigmah.client.event.NavigationEvent;
import org.sigmah.client.page.NavigationHandler;
import org.sigmah.client.page.project.calendar.ProjectCalendarPresenter;
import org.sigmah.client.page.project.dashboard.ProjectDashboardPresenter;
import org.sigmah.client.page.project.logframe.ProjectLogFramePresenter;
import org.sigmah.client.ui.ToggleAnchor;

/**
 * Project presenter which manages the {@link ProjectView}.
 * 
 * @author Denis Colliot (dcolliot@ideia.fr)
 */
public class ProjectPresenter implements Frame, TabPage {

    public static final PageId PAGE_ID = new PageId("project");

    /**
     * Description of the view managed by this presenter.
     */
    @ImplementedBy(ProjectView.class)
    public interface View {

        public ContentPanel getPanelProjectBanner();

        public ContentPanel getTabPanel();

        public void setMainPanel(Widget widget);
    }
    private final View view;
    private final Dispatcher dispatcher;
    private final Authentication authentication;
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
    private PhaseDTO currentPhaseDTO;
    private final static String[] MAIN_TABS = {
        I18N.CONSTANTS.projectTabDashboard(),
        I18N.CONSTANTS.projectTabLogFrame(), I18N.CONSTANTS.projectTabIndicators(),
        I18N.CONSTANTS.projectTabCalendar(), I18N.CONSTANTS.projectTabReports(),
        I18N.CONSTANTS.projectTabSecurityIncident()
    };
    private final Presenter[] presenters;

    @Inject
    public ProjectPresenter(final Dispatcher dispatcher, View view, Authentication authentication, final EventBus eventBus) {
        this.dispatcher = dispatcher;
        this.view = view;
        this.authentication = authentication;

        this.presenters = new Presenter[]{
                    new ProjectDashboardPresenter(dispatcher, authentication, this), // Dashboard
                    new ProjectLogFramePresenter(dispatcher, this), // Logical Framework
                    null, // Indicators
                    new ProjectCalendarPresenter(dispatcher, this), // Calendar
                    null, // Reports
                    null // Security incidents
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
                    eventBus.fireEvent(new NavigationEvent(NavigationHandler.NavigationRequested, currentState.deriveTo(index)));
                }
            });

            this.view.getTabPanel().add(anchor, layoutData);
        }
    }

    private void selectTab(int index, boolean force) {
        final ToggleAnchor anchor = (ToggleAnchor) this.view.getTabPanel().getWidget(index);

        if (currentTab != anchor) {
            if(currentTab != null)
                currentTab.toggleAnchorMode();
            
            anchor.toggleAnchorMode();
            currentTab = anchor;

            ProjectPresenter.this.view.setMainPanel(presenters[index].getView());
            presenters[index].viewDidAppear();
        }
        else if(force) {
            ProjectPresenter.this.view.setMainPanel(presenters[index].getView());
            presenters[index].viewDidAppear();
        }
    }

    @Override
    public boolean navigate(final PageState place) {
        final ProjectState projectState = (ProjectState) place;
        final int projectId = projectState.getProjectId();

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
                if (Log.isDebugEnabled()) {
                    Log.debug("Project loaded : " + projectDTO.getName());
                }
                currentState = projectState;

                boolean projectChanged = !projectDTO.equals(currentProjectDTO);

                if(projectChanged) {
                    projectState.setTabTitle(projectDTO.getName());
                    loadProjectOnView(projectDTO);
                }

                selectTab(projectState.getCurrentSection(), projectChanged);
            }
        });

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
        currentPhaseDTO = projectDTO.getCurrentPhaseDTO();

        refreshBanner();

        // TODO: Call the sub-presenter
    }

    public ProjectDTO getCurrentProjectDTO() {
        return currentProjectDTO;
    }

    public void setCurrentProjectDTO(ProjectDTO currentProjectDTO) {
        this.currentProjectDTO = currentProjectDTO;
    }

    public PhaseDTO getCurrentPhaseDTO() {
        return currentPhaseDTO;
    }

    public void setCurrentPhaseDTO(PhaseDTO currentPhaseDTO) {
        this.currentPhaseDTO = currentPhaseDTO;
    }

    /**
     * Refreshes the project banner for the current project.
     */
    public void refreshBanner() {

        view.getPanelProjectBanner().removeAll();

        // Refreshes labels.
        if (currentProjectDTO.getProjectModelDTO().getProjectBannerDTO() != null) {
            view.getPanelProjectBanner().add(
                    new Label(I18N.CONSTANTS.projectName() + ": " + currentProjectDTO.getName()));
            view.getPanelProjectBanner().add(
                    new Label(I18N.CONSTANTS.projectManager() + ": " + currentProjectDTO.getOwnerName()));
            view.getPanelProjectBanner().add(
                    new Label(I18N.CONSTANTS.projectActivePhase() + ": "
                    + currentProjectDTO.getCurrentPhaseDTO().getPhaseModelDTO().getName()));

            view.getPanelProjectBanner().layout();
        }
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

    @Override
    public AsyncMonitor showLoadingPlaceHolder(PageId pageId, PageState loadingPlace) {
        return null;
    }
}
