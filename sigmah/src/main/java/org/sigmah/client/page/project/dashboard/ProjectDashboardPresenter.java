/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */
package org.sigmah.client.page.project.dashboard;

import com.allen_sauer.gwt.log.client.Log;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.TabItem;
import com.extjs.gxt.ui.client.widget.TabPanel;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.FieldSet;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Grid;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.dispatch.monitor.MaskingAsyncMonitor;
import org.sigmah.client.dispatch.remote.Authentication;
import org.sigmah.client.i18n.I18N;
import org.sigmah.client.page.project.SubPresenter;
import org.sigmah.client.page.project.ProjectPresenter;
import org.sigmah.shared.command.ChangePhase;
import org.sigmah.shared.command.GetValue;
import org.sigmah.shared.command.UpdateProject;
import org.sigmah.shared.command.result.ProjectListResult;
import org.sigmah.shared.command.result.ValueResult;
import org.sigmah.shared.command.result.VoidResult;
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

/**
 *
 * @author Denis Colliot (dcolliot@ideia.fr)
 * @author Raphaël Calabro (rcalabro@ideia.fr)
 */
public class ProjectDashboardPresenter implements SubPresenter {

    public static abstract class View extends LayoutContainer {

        public abstract LayoutContainer getPanelProjectModel();

        public abstract LayoutContainer getPanelSelectedPhase();

        public abstract ContentPanel getPanelReminders();

        public abstract ContentPanel getPanelWatchedPoints();

        public abstract ContentPanel getPanelFinancialProjects();

        public abstract ContentPanel getPanelLocalProjects();

        public abstract TabPanel getTabPanelPhases();

        public abstract com.extjs.gxt.ui.client.widget.grid.Grid<FlexibleElementDTO> getGridRequiredElements();

        public abstract Button getButtonSavePhase();

        public abstract Button getButtonActivatePhase();

        public abstract Button getButtonPhaseGuide();

        public abstract TabPanel getTabPanelProject();
    }
    /**
     * The view managed by this presenter.
     */
    private View view;
    private ProjectPresenter projectPresenter;
    private Dispatcher dispatcher;
    private final Authentication authentication;
    /**
     * List of values changes.
     */
    private ArrayList<ValueEvent> valueChanges = new ArrayList<ValueEvent>();
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
    /**
     * The counter before the main panel is unmasked.
     */
    private int maskCount;

    public ProjectDashboardPresenter(Dispatcher dispatcher, Authentication authentication, ProjectPresenter projectPresenter) {
        this.authentication = authentication;
        this.dispatcher = dispatcher;
        this.projectPresenter = projectPresenter;

        this.tabItemsMap = new HashMap<Integer, TabItem>();
    }

    @Override
    public View getView() {
        if (view == null) {
            view = new ProjectDashboardView(); // Inject this ?

            // Hides unimplemented panels and actions.
            view.getPanelFinancialProjects().setVisible(false);
            view.getPanelLocalProjects().setVisible(false);
            view.getPanelReminders().setVisible(false);
            view.getPanelWatchedPoints().setVisible(false);
        }

        valueChanges.clear();
        view.getButtonSavePhase().setEnabled(false);
        loadProjectDashboard(projectPresenter.getCurrentProjectDTO());

        return view;
    }

    @Override
    public void viewDidAppear() {}

    /**
     * Mask the main panel and set the mask counter.
     *
     * @param count
     *            The mask counter.
     */
    private void mask(int count) {
        maskCount = count;
        view.getTabPanelPhases().mask(I18N.CONSTANTS.loading());
    }

    /**
     * Decrements the mask counter and unmask the main panel if the counter
     * reaches <code>0</code>.
     */
    private void unmask() {
        maskCount--;
        if (maskCount == 0) {
            view.getTabPanelPhases().unmask();
        }
    }

    public void loadProjectDashboard(final ProjectDTO projectDTO) { 
        // Sets current project status (for the first display, the active phase
        // is rendered).
        final PhaseDTO currentPhaseDTO = projectDTO.getCurrentPhaseDTO();

        projectPresenter.setCurrentPhaseDTO(currentPhaseDTO);

        // Clears the required elements maps .
        activePhaseRequiredElements.clear();
        currentPhaseRequiredElements.clear();

        // Sorts phases to be displayed in the correct order.
        Collections.sort(projectDTO.getPhasesDTO());

        // --
        // -- TABS CREATION
        // --

        // Removes old tabs configuration (from the previous displayed project).
        view.getTabPanelPhases().removeAll();
        view.getTabPanelPhases().removeAllListeners();
        tabItemsMap.clear();

        // Creates tabs for each phase.
        for (final PhaseDTO phaseDTO : projectDTO.getPhasesDTO()) {

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
                tabItem.getHeader().addStyleName("project-phase-active");
                view.getTabPanelPhases().setSelection(tabItem);
            }

            // If the phase is ended.
            if (isEndedPhase(phaseDTO)) {
                // Enables it and apply the correct style.
                tabItem.setEnabled(true);
                tabItem.getHeader().addStyleName("project-phase-closed");
            }
        }

        // Enables successors tabs of the current phase.
        enableSuccessorsTabs();

        // --
        // -- TABS LISTENERS
        // --

        // Adds tabs listeners for selection changes (must be added after tabs
        // creation or event fired for each tab).
        for (final PhaseDTO phaseDTO : projectDTO.getPhasesDTO()) {
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
                    for (final PhaseDTO p : projectDTO.getPhasesDTO()) {
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

                            @Override
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
        // -- ACTIVE PHASE SELECTION
        // --

        // Manually fires the first tab selection (active phase).
        // loadPhaseOnTab(currentPhaseDTO);
    }

    /**
     * Loads a project phase into the selected tab panel.
     *
     * @param phaseDTO
     *            The phase to display.
     */
    private void loadPhaseOnTab(final PhaseDTO phaseDTO) {
        // Masks the main panel.
        int count = 0;
        for (final LayoutGroupDTO groupDTO : phaseDTO.getPhaseModelDTO().getLayoutDTO().getLayoutGroupsDTO()) {
            count += groupDTO.getLayoutConstraintsDTO().size();
        }
        mask(count);

        // Sets current project status.
        projectPresenter.setCurrentPhaseDTO(phaseDTO);

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
                final GetValue command = new GetValue(projectPresenter.getCurrentProjectDTO().getId(), elementDTO.getId(),
                        elementDTO.getEntityName());
                dispatcher.execute(command, null, new AsyncCallback<ValueResult>() {

                    @Override
                    public void onFailure(Throwable throwable) {
                        Log.error("Error, element value not loaded.");
                        unmask();
                    }

                    @Override
                    public void onSuccess(ValueResult valueResult) {

                        if (Log.isDebugEnabled()) {
                            Log.debug("Element value(s) object : " + valueResult);
                        }

                        // --
                        // -- ELEMENT COMPONENT
                        // --

                        // Configures the flexible element for the current
                        // application state before generating its component.
                        elementDTO.setService(dispatcher);
                        elementDTO.setAuthentication(authentication);
                        elementDTO.setCurrentProjectDTO(projectPresenter.getCurrentProjectDTO());
                        elementDTO.assignValue(valueResult);

                        // Generates element component (with the value).
                        elementDTO.init();
                        final Component elementComponent = elementDTO.getComponent(valueResult);
                        elementComponent.addStyleName("sigmah-element");

                        // Component width.
                        final FormData formData;
                        if (elementDTO.getPreferredWidth() == 0) {
                            formData = new FormData("100%");
                        } else {
                            formData = new FormData(elementDTO.getPreferredWidth(), -1);
                        }

                        formPanel.add(elementComponent, formData);
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
                            if (isCurrentPhase(projectPresenter.getCurrentProjectDTO().getCurrentPhaseDTO())) {
                                activePhaseRequiredElements.putSaved(elementDTO.getId(), elementDTO.isFilledIn());
                            }
                        }

                        unmask();
                    }
                });
            }
        }

        // View layouts update.
        view.getTabPanelPhases().addStyleName("x-border-panel"); //FIXME: This should be done by Ext, not be the developer!
        view.layout();
    }

    /**
     * Internal class handling the value changes of the flexible elements.
     */
    private class ValueHandlerImpl implements ValueHandler {

        @Override
        public void onValueChange(ValueEvent event) {

            // Stores the change to be saved later.
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
            if (isCurrentPhase(projectPresenter.getCurrentProjectDTO().getCurrentPhaseDTO())) {
                activePhaseRequiredElements.putActual(elementDTO.getId(), event.isValueOn());
            }
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
        if (isCurrentPhase(projectPresenter.getCurrentProjectDTO().getCurrentPhaseDTO())
                || isEndedPhase(projectPresenter.getCurrentPhaseDTO())) {
            view.getButtonActivatePhase().setVisible(false);
        } // Else shows it for the current displayed phase successors.
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
        if (projectPresenter.getCurrentPhaseDTO().isEnded()) {
            view.getButtonSavePhase().setVisible(false);
        } // Else shows it (for the active phase and its successors).
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
     * Returns if a phase is the current displayed phase.
     *
     * @param phaseDTO
     *            The phase to test.
     * @return If the phase is currently displayed.
     */
    public boolean isCurrentPhase(PhaseDTO phaseDTO) {
        final PhaseDTO currentPhaseDTO = projectPresenter.getCurrentPhaseDTO();
        return currentPhaseDTO != null && phaseDTO != null && currentPhaseDTO.getId() == phaseDTO.getId();
    }

    /**
     * Returns if a phase is the active phase of the current project.
     *
     * @param phaseDTO
     *            The phase to test.
     * @return If the phase is active.
     */
    public boolean isActivePhase(PhaseDTO phaseDTO) {
        final ProjectDTO currentProjectDTO = projectPresenter.getCurrentProjectDTO();

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
    public boolean isEndedPhase(PhaseDTO phaseDTO) {
        return phaseDTO != null && phaseDTO.isEnded();
    }

    /**
     * Returns if the active phase of the current project is filled in.
     *
     * @return If the active phase of the current project is filled in.
     */
    public boolean isActivePhaseFilledIn() {

        // Checks id the map contains only true booleans.
        return activePhaseRequiredElements.isTrue();
        // return !activePhaseRequiredElements.containsValue(Boolean.FALSE);
    }

    /**
     * Enables the successors tabs of the current displayed phase.
     */
    private void enableSuccessorsTabs() {

        for (final PhaseModelDTO successor : projectPresenter.getCurrentPhaseDTO().getPhaseModelDTO().getSuccessorsDTO()) {
            final TabItem successorTabItem = tabItemsMap.get(successor.getId());
            if (successorTabItem != null) {
                successorTabItem.setEnabled(true);
            }
        }
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
            } // Else, remote call to ask a new phase activation.
            else {
                final ProjectDTO currentProjectDTO = projectPresenter.getCurrentProjectDTO();
                final PhaseDTO currentPhaseDTO = projectPresenter.getCurrentPhaseDTO();

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
                                final ProjectDTO newProjectDTO = result.getList().get(0);
                                projectPresenter.setCurrentProjectDTO(newProjectDTO);
                                projectPresenter.setCurrentPhaseDTO(newProjectDTO.getCurrentPhaseDTO());

                                // Map the required element for the active phase
                                // from the current displayed phase map.
                                activePhaseRequiredElements.clear();
                                activePhaseRequiredElements.putAll(currentPhaseRequiredElements);

                                // --
                                // -- BANNER
                                // --

                                projectPresenter.refreshBanner();

                                // --
                                // -- TOOLBAR
                                // --

                                refreshActionsToolbar();

                                // --
                                // -- UPDATES TABS
                                // --

                                // Updates ended phases styles.
                                for (PhaseDTO phase : currentProjectDTO.getPhasesDTO()) {
                                    final TabItem successorTabItem = tabItemsMap.get(phase.getPhaseModelDTO().getId());
                                    if (phase.isEnded()) {
                                        successorTabItem.getHeader().setStyleName("sigmah-closed-phase");
                                    }
                                }

                                // Updates active phase styles.
                                for (TabItem item : view.getTabPanelPhases().getItems()) {
                                    item.getHeader().removeStyleName("sigmah-active-phase");
                                }
                                tabItemsMap.get(currentPhaseDTO.getPhaseModelDTO().getId()).getHeader().addStyleName("sigmah-active-phase");

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
            final UpdateProject updateProject = new UpdateProject(projectPresenter.getCurrentProjectDTO().getId(), valueChanges);

            dispatcher.execute(updateProject,
                    new MaskingAsyncMonitor(view.getTabPanelPhases(), I18N.CONSTANTS.loading()),
                    new AsyncCallback<VoidResult>() {

                        @Override
                        public void onFailure(Throwable caught) {
                            MessageBox.info(I18N.CONSTANTS.cancelled(), I18N.CONSTANTS.error(), null);

                            currentPhaseRequiredElements.clearState();

                            if (isActivePhase(projectPresenter.getCurrentPhaseDTO())) {
                                activePhaseRequiredElements.clearState();
                            }
                        }

                        @Override
                        public void onSuccess(VoidResult result) {
                            valueChanges.clear();
                            MessageBox.info(I18N.CONSTANTS.ok(), I18N.CONSTANTS.saved(), null);

                            currentPhaseRequiredElements.saveState();

                            if (isActivePhase(projectPresenter.getCurrentPhaseDTO())) {
                                activePhaseRequiredElements.saveState();
                            }
                        }
                    });
        }
    }
}
