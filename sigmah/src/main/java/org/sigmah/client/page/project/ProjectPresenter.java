/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.page.project;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

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
import org.sigmah.shared.command.ChangePhase;
import org.sigmah.shared.command.GetProject;
import org.sigmah.shared.command.GetValue;
import org.sigmah.shared.command.result.ProjectListResult;
import org.sigmah.shared.command.result.ValueResult;
import org.sigmah.shared.dto.PhaseDTO;
import org.sigmah.shared.dto.PhaseModelDTO;
import org.sigmah.shared.dto.ProjectDTO;
import org.sigmah.shared.dto.element.FlexibleElementDTO;
import org.sigmah.shared.dto.element.handler.RequiredValueEvent;
import org.sigmah.shared.dto.element.handler.RequiredValueHandler;
import org.sigmah.shared.dto.element.handler.ValueEvent;
import org.sigmah.shared.dto.element.handler.ValueHandler;
import org.sigmah.shared.dto.layout.LayoutConstraintDTO;
import org.sigmah.shared.dto.layout.LayoutGroupDTO;

import com.allen_sauer.gwt.log.client.Log;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.Label;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.TabItem;
import com.extjs.gxt.ui.client.widget.TabPanel;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.FieldSet;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Grid;
import com.google.inject.ImplementedBy;
import com.google.inject.Inject;
import org.sigmah.shared.command.UpdateProject;
import org.sigmah.shared.command.result.VoidResult;
import org.sigmah.shared.dto.element.handler.ValueEventWrapper;

/**
 * Project presenter which manages the {@link ProjectView}.
 * 
 * @author Denis Colliot (dcolliot@ideia.fr)
 */
public class ProjectPresenter implements Frame, TabPage {

    public static final PageId PAGE_ID = new PageId("project");

    private final View view;
    private final Dispatcher dispatcher;
    private final Authentication authentication;
    private Page activePage;

    /**
     * List of values changes.
     */
    private ArrayList<ValueEvent> valueChanges = new ArrayList<ValueEvent>();

    /**
     * The current displayed project.
     */
    private ProjectDTO currentProjectDTO;

    /**
     * The current displayed phase.
     */
    private PhaseDTO currentPhaseDTO;

    /**
     * Mapping between phases models ids and tabs items (to quickly get a tab).
     */
    private final HashMap<Integer, TabItem> tabItemsMap;

    /**
     * A map to maintain the current active phase required elements states.
     */
    private final RequiredValueStateList activePhaseRequiredElements = new RequiredValueStateList();

    /**
     * A map to maintain the current displayed phase required elements states.
     */
    private final RequiredValueStateList currentPhaseRequiredElements = new RequiredValueStateList();

    @Inject
    public ProjectPresenter(final Dispatcher dispatcher, View view, Authentication authentication) {
        this.dispatcher = dispatcher;
        this.view = view;
        this.authentication = authentication;
        this.tabItemsMap = new HashMap<Integer, TabItem>();

        // Hides unimplemented panels and actions.
        view.getPanelFinancialProjects().setVisible(false);
        view.getPanelLocalProjects().setVisible(false);
        view.getPanelReminders().setVisible(false);
        view.getPanelWatchedPoints().setVisible(false);
    }

    @Override
    public boolean navigate(PageState place) {
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
                projectState.setTabTitle(projectDTO.getName());
                loadProjectOnView(projectDTO);
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

        // Sets current project status (for the first display, the active phase
        // is rendered).
        currentProjectDTO = projectDTO;
        currentPhaseDTO = currentProjectDTO.getCurrentPhaseDTO();

        // Clears the required elements maps .
        activePhaseRequiredElements.clear();
        currentPhaseRequiredElements.clear();

        // Sorts phases to be displayed in the correct order.
        Collections.sort(currentProjectDTO.getPhasesDTO());

        // --
        // -- TABS CREATION
        // --

        // Removes old tabs configuration (from the previous displayed project).
        view.getTabPanelPhases().removeAll();
        view.getTabPanelPhases().removeAllListeners();
        tabItemsMap.clear();

        // Creates tabs for each phase.
        for (final PhaseDTO phaseDTO : currentProjectDTO.getPhasesDTO()) {

            // Creates the default tab.
            final TabItem tabItem = new TabItem(phaseDTO.getPhaseModelDTO().getName());
            tabItem.setLayout(new FitLayout());
            tabItem.setEnabled(false);
            tabItem.setAutoHeight(true);

            // Map the tab item with the phase id.
            tabItemsMap.put(phaseDTO.getPhaseModelDTO().getId(), tabItem);

            // Adds the tab to the view.
            view.getTabPanelPhases().add(tabItem);

            // If the phase is the active one.
            if (isActivePhase(phaseDTO)) {
                // Enables it, apply the correct style and selects it.
                tabItem.setEnabled(true);
                tabItem.getHeader().addStyleName("sigmah-active-phase");
                view.getTabPanelPhases().setSelection(tabItem);
            }

            // If the phase is ended.
            if (isEndedPhase(phaseDTO)) {
                // Enables it and apply the correct style.
                tabItem.setEnabled(true);
                tabItem.getHeader().addStyleName("sigmah-closed-phase");
            }
        }

        // Enables successors tabs of the current phase.
        enableSuccessorsTabs();

        // --
        // -- TABS LISTENERS
        // --

        // Adds tabs listeners for selection changes (must be added after tabs
        // creation or event fired for each tab).
        for (final PhaseDTO phaseDTO : currentProjectDTO.getPhasesDTO()) {
            final TabItem tabItem = tabItemsMap.get(phaseDTO.getPhaseModelDTO().getId());
            tabItem.addListener(Events.Select, new Listener<ComponentEvent>() {

                /**
                 * Id of the phase to display. <br/>
                 * Important: it's better to manipulate the id instead of the
                 * phases instances to keep coherence after a project update.
                 */
                private int phaseDTOId = phaseDTO.getId();

                private PhaseDTO retrievePhaseDTO() {
                    // Loads the phase of the selected tab (loaded from the
                    // current project instance).
                    for (final PhaseDTO p : currentProjectDTO.getPhasesDTO()) {
                        if (p.getId() == phaseDTOId) {
                            return p;
                        }
                    }

                    return null;
                }

                @Override
                public void handleEvent(ComponentEvent tpe) {

                    final PhaseDTO toDisplayPhase = retrievePhaseDTO();

                    // If the current phase has been modified and it isn't
                    // ended.
                    if (view.getButtonSavePhase().isEnabled() && !isEndedPhase(currentPhaseDTO)) {

                        // Asks the client to save the unsaved elements before
                        // switching phases.
                        MessageBox.confirm(I18N.CONSTANTS.projectPhaseChangeAlert(),
                                I18N.CONSTANTS.projectPhaseChangeAlertDetails(), new Listener<MessageBoxEvent>() {
                                    public void handleEvent(MessageBoxEvent ce) {

                                        // If 'YES' is clicked, saves the
                                        // modifications.
                                        if (Dialog.YES.equals(ce.getButtonClicked().getItemId())) {
                                            view.getButtonSavePhase().fireEvent(Events.OnClick);
                                            if (isActivePhase(currentPhaseDTO)) {
                                                activePhaseRequiredElements.saveState();

                                            }
                                        } else if (Dialog.NO.equals(ce.getButtonClicked().getItemId())) {
                                            // If the last displayed phase was
                                            // the active one, modifications are
                                            // discarded then the required
                                            // elements map is cleared (to
                                            // prevent inconsistent successor
                                            // activation).
                                            if (isActivePhase(currentPhaseDTO)) {
                                                activePhaseRequiredElements.clearState();
                                            }
                                        }

                                        loadPhaseOnTab(toDisplayPhase);
                                    }
                                });
                    } else {
                        loadPhaseOnTab(toDisplayPhase);
                    }
                }
            });
        }

        // --
        // -- BANNER
        // --

        refreshBanner();

        // --
        // -- ACTIVE PHASE SELECTION
        // --

        // Manually fires the first tab selection (active phase).
        loadPhaseOnTab(currentPhaseDTO);

        // --
        // -- LAYOUT
        // --

        // View layouts update.
        ((ProjectView) view).layout();
    }

    /**
     * Loads a project phase into the selected tab panel.
     * 
     * @param phaseDTO
     *            The phase to display.
     */
    private void loadPhaseOnTab(final PhaseDTO phaseDTO) {

        // Sets current project status.
        currentPhaseDTO = phaseDTO;

        // Clears the required elements map for the current displayed phase.
        currentPhaseRequiredElements.clear();
        valueChanges.clear();

        // --
        // -- CLEARS PANELS
        // --

        // Clears all tabs.
        for (final TabItem tab : view.getTabPanelPhases().getItems()) {
            tab.removeAll();
        }

        // Clears panels.
        view.getPanelSelectedPhase().removeAll();
        view.getGridRequiredElements().getStore().removeAll();
        view.getTabPanelPhases().getSelectedItem().add(view.getPanelProjectModel());

        // --
        // -- TOOLBAR
        // --

        refreshActionsToolbar();

        // --
        // -- PHASE LAYOUT
        // --

        final Grid layoutGrid = (Grid) phaseDTO.getPhaseModelDTO().getWidget();
        view.getPanelSelectedPhase().add(layoutGrid);

        // For each layout group.
        for (final LayoutGroupDTO groupDTO : phaseDTO.getPhaseModelDTO().getLayoutDTO().getLayoutGroupsDTO()) {

            // Creates the fieldset and positions it.
            final FieldSet fieldSet = (FieldSet) groupDTO.getWidget();
            final FormPanel formPanel = (FormPanel) fieldSet.getWidget(0);
            layoutGrid.setWidget(groupDTO.getRow(), groupDTO.getColumn(), fieldSet);

            // For each constraint in the current layout group.
            for (LayoutConstraintDTO constraintDTO : groupDTO.getLayoutConstraintsDTO()) {

                // Gets the element managed by this constraint.
                final FlexibleElementDTO elementDTO = constraintDTO.getFlexibleElementDTO();

                // --
                // -- ELEMENT VALUE
                // --

                // Remote call to ask for this element value.
                final GetValue command = new GetValue(currentProjectDTO.getId(), elementDTO.getId(), elementDTO
                        .getClass().getName());
                dispatcher.execute(command, null, new AsyncCallback<ValueResult>() {

                    @Override
                    public void onFailure(Throwable throwable) {
                        Log.error("Error, element value not loaded.");
                    }

                    @Override
                    public void onSuccess(ValueResult valueResult) {

                        if (Log.isDebugEnabled()) {
                            Log.debug("Element value(s) object : " + valueResult.toString());
                        }

                        // --
                        // -- ELEMENT COMPONENT
                        // --

                        // Configures the flexible element for the current
                        // application state before generating its component.
                        elementDTO.setService(dispatcher);
                        elementDTO.setAuthentication(authentication);
                        elementDTO.setCurrentProjectDTO(currentProjectDTO);
                        elementDTO.assignValue(valueResult);

                        // Generates element component (with the value).
                        final Component elementComponent = elementDTO.getComponent(valueResult);
                        elementComponent.addStyleName("sigmah-element");
                        formPanel.add(elementComponent, new FormData("100%"));
                        formPanel.layout();

                        // --
                        // -- ELEMENT HANDLERS
                        // --

                        // Adds a value change handler to this element.
                        elementDTO.addValueHandler(new ValueHandlerImpl());

                        // If this element id a required one.
                        if (elementDTO.getValidates()) {

                            // Adds a specific handler.
                            elementDTO.addRequiredValueHandler(new RequiredValueHandlerImpl(elementDTO));

                            // Adds the element to the 'required panel'.
                            view.getGridRequiredElements().getStore().add(elementDTO);

                            // Map the required element for the current
                            // displayed phase.
                            currentPhaseRequiredElements.putSaved(elementDTO.getId(), elementDTO.isFilledIn());

                            // If the current displayed phase is the active one,
                            // map the required element for the active phase.
                            if (isCurrentPhase(currentProjectDTO.getCurrentPhaseDTO())) {
                                activePhaseRequiredElements.putSaved(elementDTO.getId(), elementDTO.isFilledIn());
                            }
                        }
                    }
                });
            }
        }

        // View layouts update.
        ((ProjectView) view).layout();
    }

    /**
     * Internal class handling the phases activation.
     */
    private class ActivatePhaseListener implements Listener<ButtonEvent> {

        @Override
        public void handleEvent(ButtonEvent be) {

            // If the active phase required elements aren't filled, shows an
            // alert.
            if (!isActivePhaseFilledIn()) {
                MessageBox.info(I18N.CONSTANTS.projectPhaseActivationError(),
                        I18N.CONSTANTS.projectPhaseActivationErrorDetails(), null);
            }
            // Else, remote call to ask a new phase activation.
            else {

                // Activates the current displayed phase.
                dispatcher.execute(new ChangePhase(currentProjectDTO.getId(), currentPhaseDTO.getId()), null,
                        new AsyncCallback<ProjectListResult>() {

                            @Override
                            public void onFailure(Throwable throwable) {
                                Log.error("Error, phase #" + currentPhaseDTO.getId() + " not activated.");
                            }

                            @Override
                            public void onSuccess(ProjectListResult result) {

                                if (Log.isDebugEnabled()) {
                                    Log.debug("Phase activated : " + currentPhaseDTO.getId());
                                }

                                // Sets current project status.
                                currentProjectDTO = result.getList().get(0);
                                currentPhaseDTO = currentProjectDTO.getCurrentPhaseDTO();

                                // Map the required element for the active phase
                                // from the current displayed phase map.
                                activePhaseRequiredElements.clear();
                                activePhaseRequiredElements.putAll(currentPhaseRequiredElements);

                                // --
                                // -- BANNER
                                // --

                                refreshBanner();

                                // --
                                // -- TOOLBAR
                                // --

                                refreshActionsToolbar();

                                // --
                                // -- UPDATES TABS
                                // --

                                // Updates ended phases styles.
                                for (PhaseDTO phase : currentProjectDTO.getPhasesDTO()) {
                                    final TabItem successorTabItem = tabItemsMap.get(phase.getId());
                                    if (phase.isEnded()) {
                                        successorTabItem.getHeader().setStyleName("sigmah-closed-phase");
                                    }
                                }

                                // Updates active phase styles.
                                for (TabItem item : view.getTabPanelPhases().getItems()) {
                                    item.getHeader().removeStyleName("sigmah-active-phase");
                                }
                                tabItemsMap.get(currentPhaseDTO.getPhaseModelDTO().getId()).getHeader()
                                        .addStyleName("sigmah-active-phase");

                                // Enables successors tabs of the current phase.
                                enableSuccessorsTabs();
                            }
                        });
            }
        }
    }

    /**
     * Internal class handling the modifications saving.
     */
    private class SaveListener implements Listener<ButtonEvent> {

        @Override
        public void handleEvent(ButtonEvent be) {
            view.getButtonSavePhase().disable();
            final UpdateProject updateProject = new UpdateProject(currentProjectDTO.getId(), valueChanges);

            dispatcher.execute(updateProject, null, new AsyncCallback<VoidResult>() {

                @Override
                public void onFailure(Throwable caught) {
                    MessageBox.info(I18N.CONSTANTS.cancelled(), I18N.CONSTANTS.error(), null);
                }

                @Override
                public void onSuccess(VoidResult result) {
                    valueChanges.clear();
                    MessageBox.info(I18N.CONSTANTS.ok(), I18N.CONSTANTS.saved(), null);
                }
            });
        }
    }

    /**
     * Returns if a phase is the current displayed phase.
     * 
     * @param phaseDTO
     *            The phase to test.
     * @return If the phase is currently displayed.
     */
    private boolean isCurrentPhase(PhaseDTO phaseDTO) {
        return currentPhaseDTO != null && phaseDTO != null && currentPhaseDTO.getId() == phaseDTO.getId();
    }

    /**
     * Returns if a phase is the active phase of the current project.
     * 
     * @param phaseDTO
     *            The phase to test.
     * @return If the phase is active.
     */
    private boolean isActivePhase(PhaseDTO phaseDTO) {
        return currentProjectDTO != null && currentProjectDTO.getCurrentPhaseDTO() != null && phaseDTO != null
                && currentProjectDTO.getCurrentPhaseDTO().getId() == phaseDTO.getId();
    }

    /**
     * Returns if a phase is ended.
     * 
     * @param phaseDTO
     *            The phase to test.
     * @return If the phase is ended.
     */
    private boolean isEndedPhase(PhaseDTO phaseDTO) {
        return phaseDTO != null && phaseDTO.isEnded();
    }

    /**
     * Returns if the active phase of the current project is filled in.
     * 
     * @return If the active phase of the current project is filled in.
     */
    private boolean isActivePhaseFilledIn() {

        // Checks id the map contains only true booleans.
        return activePhaseRequiredElements.isTrue();
        // return !activePhaseRequiredElements.containsValue(Boolean.FALSE);
    }

    /**
     * Enables the successors tabs of the current displayed phase.
     */
    private void enableSuccessorsTabs() {

        for (final PhaseModelDTO successor : currentPhaseDTO.getPhaseModelDTO().getSuccessorsDTO()) {
            final TabItem successorTabItem = tabItemsMap.get(successor.getId());
            if (successorTabItem != null) {
                successorTabItem.setEnabled(true);
            }
        }
    }

    /**
     * Refreshes the project banner for the current project.
     */
    private void refreshBanner() {

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

    /**
     * Refreshes the actions toolbar for the current displayed phase.
     */
    private void refreshActionsToolbar() {

        // --
        // -- ACTION: ACTIVE PHASE
        // --

        // Always enabled (the listener is in charge of this action validation).
        view.getButtonActivatePhase().setEnabled(activePhaseRequiredElements.isTrue());
        view.getButtonActivatePhase().removeAllListeners();

        // If the current displayed phase is the active one or it is ended, hide
        if (isCurrentPhase(currentProjectDTO.getCurrentPhaseDTO()) || isEndedPhase(currentPhaseDTO)) {
            view.getButtonActivatePhase().setVisible(false);
        }
        // Else shows it for the current displayed phase successors.
        else {
            view.getButtonActivatePhase().setVisible(true);
            view.getButtonActivatePhase().addListener(Events.Select, new ActivatePhaseListener());
        }

        // --
        // -- ACTION: SAVE MODIFICATIONS
        // --

        // Disabled until a field is modified.
        view.getButtonSavePhase().setEnabled(false);
        view.getButtonSavePhase().removeAllListeners();

        // If the phase is ended, hide the save action.
        if (currentPhaseDTO.isEnded()) {
            view.getButtonSavePhase().setVisible(false);
        }
        // Else shows it (for the active phase and its successors).
        else {
            view.getButtonSavePhase().setVisible(true);
            view.getButtonSavePhase().addListener(Events.OnClick, new SaveListener());
        }

        // --
        // -- ACTION: PHASE GUIDE
        // --

        // For the moment, always visible and disabled.
        // TODO Check guide availability to enable this action.
        view.getButtonPhaseGuide().setVisible(true);
        view.getButtonPhaseGuide().removeAllListeners();
        view.getButtonPhaseGuide().setEnabled(false);
    }

    /**
     * Internal class handling the value changes of the flexible elements.
     */
    private class ValueHandlerImpl implements ValueHandler {

        @Override
        public void onValueChange(ValueEvent event) {

            // Stores the change to be saved later.
            final ValueEventWrapper w = new ValueEventWrapper();
            w.setSourceElement(event.getSourceElement());
            w.setValue(event.getValue());
            w.setValues(event.getValues());

            valueChanges.add(event);

            // Enables the save action.
            view.getButtonSavePhase().setEnabled(true);
        }

    }

    /**
     * Internal class handling the value changes of the required elements.
     */
    private class RequiredValueHandlerImpl implements RequiredValueHandler {

        private FlexibleElementDTO elementDTO;

        public RequiredValueHandlerImpl(FlexibleElementDTO elementDTO) {
            this.elementDTO = elementDTO;
        }

        @Override
        public void onRequiredValueChange(RequiredValueEvent event) {

            // Updates the element state for the new value.
            elementDTO.setFilledIn(event.isValueOn());
            view.getGridRequiredElements().getStore().update(elementDTO);

            // Map the required element for the current displayed phase.
            currentPhaseRequiredElements.putActual(elementDTO.getId(), event.isValueOn());

            // If the current displayed phase is the active one,
            // map the required element for the active phase.
            if (isCurrentPhase(currentProjectDTO.getCurrentPhaseDTO())) {
                activePhaseRequiredElements.putActual(elementDTO.getId(), event.isValueOn());
            }
        }
    }

    /**
     * Useful internal class to manage the required elements completions list.
     * 
     * @author tmi
     * 
     */
    private class RequiredValueStateList {

        /**
         * Map the required element, its saved value completion (in db) and its
         * actual value completion (not yet saved).
         */
        private final HashMap<Integer, RequiredValueState> list;

        public RequiredValueStateList() {
            list = new HashMap<Integer, RequiredValueState>();
        }

        /**
         * Clears the list of required elements.
         */
        public void clear() {
            list.clear();
        }

        /**
         * Copies a list of required elements from another manager.
         * 
         * @param other
         *            The other required elements manager.
         */
        public void putAll(RequiredValueStateList other) {
            list.putAll(other.list);
        }

        /**
         * Sets a required element saved value completion (in db). Adds it if
         * necessary.
         * 
         * @param elementDTOId
         *            The required element id.
         * @param savedState
         *            The saved value completion.
         */
        public void putSaved(Integer elementDTOId, Boolean savedState) {

            RequiredValueState state = list.get(elementDTOId);

            if (state == null) {
                state = new RequiredValueState();
                list.put(elementDTOId, state);
            }

            state.setSavedState(savedState);
        }

        /**
         * Sets a required element actual value completion (in local). Adds it
         * if necessary.
         * 
         * @param elementDTOId
         *            The required element id.
         * @param actualState
         *            The actual value completion.
         */
        public void putActual(Integer elementDTOId, Boolean actualState) {

            RequiredValueState state = list.get(elementDTOId);

            if (state == null) {
                state = new RequiredValueState();
                list.put(elementDTOId, state);
            }

            state.setActualState(actualState);
        }

        /**
         * Informs that all actual values completions has been saved to the data
         * layer.
         */
        public void saveState() {
            for (final RequiredValueState state : list.values()) {
                state.saveState();
            }
        }

        /**
         * Informs that all actual values completions has been discarded.
         */
        public void clearState() {
            for (final RequiredValueState state : list.values()) {
                state.clearState();
            }
        }

        /**
         * Returns if all saved values completions are valid.
         * 
         * @return If all saved values completions are valid.
         */
        public boolean isTrue() {
            for (final RequiredValueState state : list.values()) {
                if (!state.isTrue()) {
                    return false;
                }
            }
            return true;
        }

        @Override
        public String toString() {
            return list.toString();
        }
    }

    /**
     * Useful internal class to manage the required elements completion.
     * 
     * @author tmi
     * 
     */
    private class RequiredValueState {

        /**
         * The required element saved value completion (in db).
         */
        private Boolean savedState;

        /**
         * The actual element saved value completion (in local).
         */
        private Boolean actualState;

        public void setSavedState(Boolean savedState) {
            this.savedState = savedState;
        }

        public void setActualState(Boolean actualState) {
            this.actualState = actualState;
        }

        /**
         * Informs that the actual value completion has been saved to the data
         * layer.
         */
        public void saveState() {
            if (actualState != null) {
                savedState = actualState;
                actualState = null;
            }
        }

        /**
         * Informs that the actual value completion has been discarded.
         */
        public void clearState() {
            actualState = null;
        }

        /**
         * Returns if the saved value completion is valid.
         * 
         * @return If the saved value completion is valid.
         */
        public boolean isTrue() {
            return !Boolean.FALSE.equals(savedState);
        }

        @Override
        public String toString() {
            return "saved: " + savedState + " ; actual: " + actualState;
        }
    }

    /**
     * Description of the view managed by this presenter.
     */
    @ImplementedBy(ProjectView.class)
    public interface View {
        public LayoutContainer getPanelProjectModel();

        public LayoutContainer getPanelSelectedPhase();

        public ContentPanel getPanelProjectBanner();

        public ContentPanel getPanelReminders();

        public ContentPanel getPanelWatchedPoints();

        public ContentPanel getPanelFinancialProjects();

        public ContentPanel getPanelLocalProjects();

        public TabPanel getTabPanelPhases();

        public com.extjs.gxt.ui.client.widget.grid.Grid<FlexibleElementDTO> getGridRequiredElements();

        public Button getButtonSavePhase();

        public Button getButtonActivatePhase();

        public Button getButtonPhaseGuide();

        public TabPanel getTabPanelProject();
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
