/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */
package org.sigmah.client.page.project.dashboard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.dispatch.monitor.MaskingAsyncMonitor;
import org.sigmah.client.dispatch.remote.Authentication;
import org.sigmah.client.i18n.I18N;
import org.sigmah.client.icon.IconImageBundle;
import org.sigmah.client.page.dashboard.CreateProjectWindow;
import org.sigmah.client.page.dashboard.CreateProjectWindow.CreateProjectListener;
import org.sigmah.client.page.dashboard.CreateProjectWindow.Mode;
import org.sigmah.client.page.project.ProjectPresenter;
import org.sigmah.client.page.project.SubPresenter;
import org.sigmah.client.page.project.dashboard.funding.FundingIconProvider;
import org.sigmah.client.page.project.dashboard.funding.FundingIconProvider.IconSize;
import org.sigmah.client.page.project.logframe.FormWindow;
import org.sigmah.client.page.project.logframe.FormWindow.FormSubmitListener;
import org.sigmah.client.ui.FlexibleGrid;
import org.sigmah.client.util.Notification;
import org.sigmah.client.util.NumberUtils;
import org.sigmah.shared.command.ChangePhase;
import org.sigmah.shared.command.CreateEntity;
import org.sigmah.shared.command.GetProjects;
import org.sigmah.shared.command.GetValue;
import org.sigmah.shared.command.UpdateProject;
import org.sigmah.shared.command.result.CreateResult;
import org.sigmah.shared.command.result.ProjectListResult;
import org.sigmah.shared.command.result.ValueResult;
import org.sigmah.shared.command.result.VoidResult;
import org.sigmah.shared.domain.ProjectModelType;
import org.sigmah.shared.dto.CountryDTO;
import org.sigmah.shared.dto.PhaseDTO;
import org.sigmah.shared.dto.PhaseModelDTO;
import org.sigmah.shared.dto.ProjectDTO;
import org.sigmah.shared.dto.ProjectDTOLight;
import org.sigmah.shared.dto.ProjectFundingDTO;
import org.sigmah.shared.dto.element.DefaultFlexibleElementDTO;
import org.sigmah.shared.dto.element.FlexibleElementDTO;
import org.sigmah.shared.dto.element.handler.RequiredValueEvent;
import org.sigmah.shared.dto.element.handler.RequiredValueHandler;
import org.sigmah.shared.dto.element.handler.ValueEvent;
import org.sigmah.shared.dto.element.handler.ValueHandler;
import org.sigmah.shared.dto.layout.LayoutConstraintDTO;
import org.sigmah.shared.dto.layout.LayoutGroupDTO;

import com.allen_sauer.gwt.log.client.Log;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MenuEvent;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.TabItem;
import com.extjs.gxt.ui.client.widget.TabPanel;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.FieldSet;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.LabelField;
import com.extjs.gxt.ui.client.widget.form.NumberField;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.extjs.gxt.ui.client.widget.menu.Menu;
import com.extjs.gxt.ui.client.widget.menu.MenuItem;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Grid;

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

        public abstract void flushToolbar();

        public abstract void fillToolbar();

        public abstract FlexibleGrid<ProjectFundingDTO> getFinancialProjectGrid();

        public abstract FlexibleGrid<ProjectFundingDTO> getLocalPartnerProjectGrid();

        public abstract Button getAddFinancialProjectButton();

        public abstract Button getAddLocalPartnerProjectButton();

        public abstract Button getCreateFinancialProjectButton();

        public abstract Button getCreateLocalPartnerProjectButton();
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

    public ProjectDashboardPresenter(Dispatcher dispatcher, Authentication authentication,
            ProjectPresenter projectPresenter) {
        this.authentication = authentication;
        this.dispatcher = dispatcher;
        this.projectPresenter = projectPresenter;

        this.tabItemsMap = new HashMap<Integer, TabItem>();
    }

    @Override
    public View getView() {

        if (view == null) {

            // Inject this ?
            view = new ProjectDashboardView(authentication);

            addLinkedProjectsListeners();

            // Hides unimplemented panels and actions.
            view.getPanelReminders().setVisible(false);
            view.getPanelWatchedPoints().setVisible(false);
        }

        valueChanges.clear();
        view.getButtonSavePhase().disable();
        loadProjectDashboard(projectPresenter.getCurrentProjectDTO());

        return view;
    }

    @Override
    public void viewDidAppear() {
    }

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

            // Refreshes the toolbar.
            refreshActionsToolbar();
        }
    }

    public void loadProjectDashboard(final ProjectDTO projectDTO) {

        // Clears the required elements maps .
        activePhaseRequiredElements.clear();
        currentPhaseRequiredElements.clear();

        // Sorts phases to be displayed in the correct order.
        Collections.sort(projectDTO.getPhasesDTO());

        view.getFinancialProjectGrid().getStore().removeAll();
        view.getLocalPartnerProjectGrid().getStore().removeAll();

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
                    for (final PhaseDTO p : projectPresenter.getCurrentProjectDTO().getPhasesDTO()) {
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
                    if (view.getButtonSavePhase().isEnabled()
                            && !isEndedPhase(projectPresenter.getCurrentDisplayedPhaseDTO())) {

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
                                            if (isActivePhase(projectPresenter.getCurrentDisplayedPhaseDTO())) {
                                                activePhaseRequiredElements.saveState();

                                            }
                                        } else if (Dialog.NO.equals(ce.getButtonClicked().getItemId())) {
                                            // If the last displayed phase was
                                            // the active one, modifications are
                                            // discarded then the required
                                            // elements map is cleared (to
                                            // prevent inconsistent successor
                                            // activation).
                                            if (isActivePhase(projectPresenter.getCurrentDisplayedPhaseDTO())) {
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

        loadLinkedProjects(projectDTO);
    }

    /**
     * Loads linked projects
     * 
     * @param projectDTO
     *            The project.
     */
    private void loadLinkedProjects(final ProjectDTO projectDTO) {

        // Adds funding.
        for (final ProjectFundingDTO funding : projectDTO.getFunding()) {
            view.getFinancialProjectGrid().getStore().add(funding);
        }

        // Adds funded.
        for (final ProjectFundingDTO funded : projectDTO.getFunded()) {
            view.getLocalPartnerProjectGrid().getStore().add(funded);
        }
    }

    /**
     * Loads a project phase into the selected tab panel.
     * 
     * @param phaseDTO
     *            The phase to display.
     */
    private void loadPhaseOnTab(final PhaseDTO phaseDTO) {

        if (Log.isDebugEnabled()) {
            Log.debug("[loadPhaseOnTab] Loading phase #" + phaseDTO.getId() + " with model #"
                    + phaseDTO.getPhaseModelDTO().getId() + " and definition #"
                    + phaseDTO.getPhaseModelDTO().getDefinitionDTO().getId() + ".");
        }

        // Phase ended state
        final boolean isEnded = isEndedPhase(phaseDTO);

        // Masks the main panel.
        int count = 0;
        for (final LayoutGroupDTO groupDTO : phaseDTO.getPhaseModelDTO().getLayoutDTO().getLayoutGroupsDTO()) {
            count += groupDTO.getLayoutConstraintsDTO().size();
        }
        mask(count);

        // Sets current project status.
        projectPresenter.setCurrentDisplayedPhaseDTO(phaseDTO);

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
                final GetValue command = new GetValue(projectPresenter.getCurrentProjectDTO().getId(),
                        elementDTO.getId(), elementDTO.getEntityName());
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
                        final Component elementComponent = elementDTO.getComponent(valueResult, !isEnded);

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
        // FIXME: This should be done by Ext, not be the developer!
        view.getTabPanelPhases().addStyleName("x-border-panel");
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

            if (!projectPresenter.getCurrentDisplayedPhaseDTO().isEnded()) {
                // Enables the save action.
                view.getButtonSavePhase().enable();
            }
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

        // If the current displayed phase is ended, the toolbar is hidden.
        if (isEndedPhase(projectPresenter.getCurrentDisplayedPhaseDTO())) {
            view.flushToolbar();
            return;
        }

        view.fillToolbar();

        // --
        // -- ACTION: ACTIVATE OR CLOSE PHASE
        // --

        boolean enabled = activePhaseRequiredElements.isTrue();
        view.getButtonActivatePhase().setEnabled(enabled);
        view.getButtonActivatePhase().removeAllListeners();

        // If the current displayed phase is the active one or it is ended, the
        // close action is displayed.
        if (isCurrentPhase(projectPresenter.getCurrentProjectDTO().getCurrentPhaseDTO())) {

            view.getButtonActivatePhase().setText(I18N.CONSTANTS.projectClosePhaseButton());
            view.getButtonActivatePhase().setIcon(IconImageBundle.ICONS.close());

            if (!enabled) {
                view.getButtonActivatePhase().setTitle(I18N.CONSTANTS.projectCannotClose());
            } else {
                view.getButtonActivatePhase().setTitle("");
            }

            view.getButtonActivatePhase().addListener(Events.Select, new Listener<ButtonEvent>() {

                @Override
                public void handleEvent(ButtonEvent be) {
                    view.getButtonActivatePhase().showMenu();
                }
            });

            // Builds the button menu to select the next phase after closing the
            // current displayed one.
            final Menu successorsMenu = new Menu();

            final List<PhaseDTO> successors = projectPresenter.getCurrentProjectDTO().getSuccessors(
                    projectPresenter.getCurrentDisplayedPhaseDTO());

            // If the current displayed phase hasn't successor, the close action
            // ends the project.
            if (successors == null || successors.isEmpty()) {

                final MenuItem endItem = new MenuItem(I18N.CONSTANTS.projectEnd(), IconImageBundle.ICONS.activate());
                endItem.addListener(Events.Select, new Listener<MenuEvent>() {

                    @Override
                    public void handleEvent(MenuEvent me) {

                        activatePhase(null, true);
                    }
                });

                successorsMenu.add(endItem);
            }
            // Each successor is added to the list of choices.
            else {

                for (final PhaseDTO successor : successors) {

                    final MenuItem successorItem = new MenuItem(I18N.MESSAGES.projectActivate(successor
                            .getPhaseModelDTO().getName()), IconImageBundle.ICONS.activate());
                    successorItem.addListener(Events.Select, new Listener<MenuEvent>() {

                        @Override
                        public void handleEvent(MenuEvent me) {

                            activatePhase(successor, true);
                        }
                    });
                    successorsMenu.add(successorItem);
                }
            }

            view.getButtonActivatePhase().setMenu(successorsMenu);
        }
        // Else the active action is displayed.
        else {

            if (!enabled) {
                view.getButtonActivatePhase().setTitle(I18N.CONSTANTS.projectCannotActivate());
            } else {
                view.getButtonActivatePhase().setTitle("");
            }

            view.getButtonActivatePhase().setMenu(null);

            view.getButtonActivatePhase().setText(I18N.CONSTANTS.projectActivatePhaseButton());
            view.getButtonActivatePhase().setIcon(IconImageBundle.ICONS.activate());

            view.getButtonActivatePhase().addListener(Events.Select, new Listener<ButtonEvent>() {

                @Override
                public void handleEvent(ButtonEvent be) {
                    activatePhase(projectPresenter.getCurrentDisplayedPhaseDTO(), false);
                }
            });
        }

        // --
        // -- ACTION: SAVE MODIFICATIONS
        // --

        // Disabled until a field is modified.
        view.getButtonSavePhase().setEnabled(false);
        view.getButtonSavePhase().removeAllListeners();

        // If the phase isn't ended, adds the save action.
        if (!isEndedPhase(projectPresenter.getCurrentDisplayedPhaseDTO())) {
            view.getButtonSavePhase().addListener(Events.OnClick, new SaveListener());
        }

        // --
        // -- ACTION: PHASE GUIDE
        // --

        // Check guide availability.
        view.getButtonPhaseGuide().removeAllListeners();

        if (projectPresenter.getCurrentDisplayedPhaseDTO().getPhaseModelDTO().isGuideAvailable()) {

            final String guide = projectPresenter.getCurrentDisplayedPhaseDTO().getPhaseModelDTO().getGuide();

            view.getButtonPhaseGuide().setEnabled(true);
            view.getButtonPhaseGuide().setTitle(guide);
            view.getButtonPhaseGuide().addListener(Events.OnClick, new Listener<BaseEvent>() {

                @Override
                public void handleEvent(BaseEvent be) {
                    Window.open(guide, "_blank", null);
                }
            });
        } else {
            view.getButtonPhaseGuide().setEnabled(false);
            view.getButtonPhaseGuide().setTitle(I18N.CONSTANTS.projectPhaseGuideUnavailable());
        }
    }

    /**
     * Returns if a phase is the current displayed phase.
     * 
     * @param phaseDTO
     *            The phase to test.
     * @return If the phase is currently displayed.
     */
    public boolean isCurrentPhase(PhaseDTO phaseDTO) {
        final PhaseDTO currentPhaseDTO = projectPresenter.getCurrentDisplayedPhaseDTO();
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

        for (final PhaseModelDTO successor : projectPresenter.getCurrentProjectDTO().getCurrentPhaseDTO()
                .getPhaseModelDTO().getSuccessorsDTO()) {
            final TabItem successorTabItem = tabItemsMap.get(successor.getId());
            if (successorTabItem != null) {
                successorTabItem.setEnabled(true);
            }
        }
    }

    /**
     * Activates a phase.
     * 
     * @param phase
     *            The phase to activate.
     * @param reload
     *            If the current displayed phase must be reloaded.
     */
    private void activatePhase(final PhaseDTO phase, final boolean reload) {

        // If the active phase required elements aren't filled, shows an
        // alert and returns.
        if (!isActivePhaseFilledIn()) {
            MessageBox.alert(I18N.CONSTANTS.projectPhaseActivationError(),
                    I18N.CONSTANTS.projectPhaseActivationErrorDetails(), null);
            return;
        }

        // If the phase to activate is null, the active phase will only be
        // closed.
        if (phase == null) {

            // Confirms that the user wants to end the project.
            MessageBox.confirm(
                    I18N.CONSTANTS.projectEnd(),
                    I18N.MESSAGES.projectEnd(projectPresenter.getCurrentProjectDTO().getCurrentPhaseDTO()
                            .getPhaseModelDTO().getName()), new Listener<MessageBoxEvent>() {

                        @Override
                        public void handleEvent(MessageBoxEvent be) {

                            if (Dialog.YES.equals(be.getButtonClicked().getItemId())) {

                                // Activates the current displayed phase.
                                dispatcher.execute(new ChangePhase(projectPresenter.getCurrentProjectDTO().getId(),
                                        null), null, new AsyncCallback<ProjectDTO>() {

                                    @Override
                                    public void onFailure(Throwable e) {

                                        Log.error("[activatePhase] The project hasn't be ended.", e);
                                        MessageBox.alert(I18N.CONSTANTS.projectEndError(),
                                                I18N.CONSTANTS.projectEndErrorDetails(), null);
                                    }

                                    @Override
                                    public void onSuccess(ProjectDTO result) {

                                        if (Log.isDebugEnabled()) {
                                            Log.debug("[activatePhase] Project successfully ended.");
                                        }

                                        // Sets the new current project (after
                                        // update).
                                        projectPresenter.setCurrentProjectDTO(result);

                                        // Sets the new current displayed phase
                                        // (not necessary the active one).
                                        for (final PhaseDTO phase : projectPresenter.getCurrentProjectDTO()
                                                .getPhasesDTO()) {
                                            if (phase.getId() == projectPresenter.getCurrentDisplayedPhaseDTO().getId()) {
                                                projectPresenter.setCurrentDisplayedPhaseDTO(phase);
                                            }
                                        }

                                        refreshDashboardAfterUpdate(reload);
                                    }
                                });
                            }
                        }
                    });
        }
        // Else the active will be closed and the new phase will be activated.
        else {

            // Confirms that the user wants to close the active phase and
            // activate the given one.
            MessageBox.confirm(
                    I18N.CONSTANTS.projectCloseAndActivate(),
                    I18N.MESSAGES.projectCloseAndActivate(projectPresenter.getCurrentProjectDTO().getCurrentPhaseDTO()
                            .getPhaseModelDTO().getName(), phase.getPhaseModelDTO().getName()),
                    new Listener<MessageBoxEvent>() {

                        @Override
                        public void handleEvent(MessageBoxEvent be) {

                            if (Dialog.YES.equals(be.getButtonClicked().getItemId())) {

                                // Activates the current displayed phase.
                                dispatcher.execute(new ChangePhase(projectPresenter.getCurrentProjectDTO().getId(),
                                        phase.getId()), null, new AsyncCallback<ProjectDTO>() {

                                    @Override
                                    public void onFailure(Throwable e) {

                                        Log.error("[activatePhase] The phase #" + phase.getId()
                                                + " hasn't be activated.", e);
                                        MessageBox.alert(I18N.CONSTANTS.projectActivatePhaseError(),
                                                I18N.CONSTANTS.projectActivatePhaseErrorDetails(), null);
                                    }

                                    @Override
                                    public void onSuccess(ProjectDTO result) {

                                        if (Log.isDebugEnabled()) {
                                            Log.debug("[activatePhase] Phase #" + phase.getId()
                                                    + " successfully activated.");
                                        }

                                        // Sets the new current project (after
                                        // update).
                                        projectPresenter.setCurrentProjectDTO(result);

                                        // Sets the new current displayed phase
                                        // (not necessary the active one).
                                        for (final PhaseDTO phase : projectPresenter.getCurrentProjectDTO()
                                                .getPhasesDTO()) {
                                            if (phase.getId() == projectPresenter.getCurrentDisplayedPhaseDTO().getId()) {
                                                projectPresenter.setCurrentDisplayedPhaseDTO(phase);
                                            }
                                        }

                                        refreshDashboardAfterUpdate(reload);
                                    }
                                });
                            }
                        }
                    });
        }
    }

    /**
     * Refreshes the dashboard after an update of the project instance.
     * 
     * @param reload
     *            If the current displayed phase must be reloaded.
     */
    private void refreshDashboardAfterUpdate(boolean reload) {

        if (Log.isDebugEnabled()) {
            Log.debug("[refreshDashboardAfterUpdate] Refreshes the dashboard.");
        }

        // Map the required element for the active phase from the current
        // displayed phase map.
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

        // Updates closed phases styles.
        for (final PhaseDTO phase : projectPresenter.getCurrentProjectDTO().getPhasesDTO()) {
            final TabItem successorTabItem = tabItemsMap.get(phase.getPhaseModelDTO().getId());
            if (phase.isEnded()) {
                successorTabItem.getHeader().addStyleName("project-phase-closed");
            }
        }

        // Updates active phase styles.
        for (final TabItem item : view.getTabPanelPhases().getItems()) {
            item.getHeader().removeStyleName("project-phase-active");
        }

        final PhaseDTO phase;
        if ((phase = projectPresenter.getCurrentProjectDTO().getCurrentPhaseDTO()) != null) {

            // Updates active phase styles.
            tabItemsMap.get(phase.getPhaseModelDTO().getId()).getHeader().addStyleName("project-phase-active");

            // Enables successors tabs of the current phase.
            enableSuccessorsTabs();
        }

        if (reload) {
            loadPhaseOnTab(projectPresenter.getCurrentDisplayedPhaseDTO());
        }
    }

    /**
     * Internal class handling the modifications saving.
     */
    private class SaveListener implements Listener<ButtonEvent> {

        @Override
        public void handleEvent(ButtonEvent be) {
            view.getButtonSavePhase().disable();
            final UpdateProject updateProject = new UpdateProject(projectPresenter.getCurrentProjectDTO().getId(),
                    valueChanges);

            dispatcher.execute(updateProject,
                    new MaskingAsyncMonitor(view.getTabPanelPhases(), I18N.CONSTANTS.loading()),
                    new AsyncCallback<VoidResult>() {

                        @Override
                        public void onFailure(Throwable caught) {
                            MessageBox.alert(I18N.CONSTANTS.save(), I18N.CONSTANTS.saveError(), null);

                            currentPhaseRequiredElements.clearState();

                            if (isActivePhase(projectPresenter.getCurrentDisplayedPhaseDTO())) {
                                activePhaseRequiredElements.clearState();
                            }
                        }

                        @Override
                        public void onSuccess(VoidResult result) {

                            Notification.show(I18N.CONSTANTS.infoConfirmation(), I18N.CONSTANTS.saveConfirm());

                            // Checks if there is any update needed to the local
                            // project instance.
                            boolean refreshBanner = false;
                            for (ValueEvent event : valueChanges) {
                                if (event.getSource() instanceof DefaultFlexibleElementDTO) {
                                    updateCurrentProject(((DefaultFlexibleElementDTO) event.getSource()),
                                            (String) event.getValue());
                                    refreshBanner = true;
                                }
                            }

                            valueChanges.clear();

                            currentPhaseRequiredElements.saveState();

                            if (isActivePhase(projectPresenter.getCurrentDisplayedPhaseDTO())) {
                                activePhaseRequiredElements.saveState();
                            }

                            refreshActionsToolbar();
                            if (refreshBanner) {
                                projectPresenter.refreshBanner();
                            }
                        }
                    });
        }
    }

    /**
     * Updates locally the DTO to avoid a remote server call.
     * 
     * @param element
     *            The default flexible element.
     * @param value
     *            The new value.
     */
    private void updateCurrentProject(DefaultFlexibleElementDTO element, String value) {

        final ProjectDTO currentProjectDTO = projectPresenter.getCurrentProjectDTO();

        switch (element.getType()) {
        case CODE:
            currentProjectDTO.setName(value);
            break;
        case TITLE:
            currentProjectDTO.setFullName(value);
            break;
        case START_DATE:
            if ("".equals(value)) {
                currentProjectDTO.setStartDate(null);
            } else {
                try {
                    final long timestamp = Long.parseLong(value);
                    currentProjectDTO.setStartDate(new Date(timestamp));
                } catch (NumberFormatException e) {
                    // nothing, invalid date.
                }
            }
            break;
        case END_DATE:
            if ("".equals(value)) {
                currentProjectDTO.setEndDate(null);
            } else {
                try {
                    final long timestamp = Long.parseLong(value);
                    currentProjectDTO.setEndDate(new Date(timestamp));
                } catch (NumberFormatException e) {
                    // nothing, invalid date.
                }
            }
            break;
        case BUDGET:
            try {

                final String[] budgets = value.split("\\|");
                final double plannedBudget = Double.parseDouble(budgets[0]);
                final double spendBudget = Double.parseDouble(budgets[1]);
                final double receivedBudget = Double.parseDouble(budgets[2]);

                currentProjectDTO.setPlannedBudget(plannedBudget);
                currentProjectDTO.setSpendBudget(spendBudget);
                currentProjectDTO.setReceivedBudget(receivedBudget);

            } catch (Exception e) {
                // nothing, invalid budget.
            }
            break;
        case COUNTRY:
            final CountryDTO country = element.getCountriesStore().findModel("id", Integer.parseInt(value));
            if (country != null) {
                currentProjectDTO.setCountry(country);
            } else {
                // nothing, invalid country.
            }
            break;
        case OWNER:

            // The owner component doesn't fire any event for now.

            /*
             * final UserPermissionDTO user =
             * element.getUsersStore().findModel("email", value); if (user !=
             * null) { currentProjectDTO.setOwnerName(user.getName());
             * currentProjectDTO.setOwnerFirstName(user.getFirstName());
             * currentProjectDTO.setOwnerEmail(user.getEmail()); } else { //
             * nothing, invalid user. }
             */
            break;
        default:
            // Nothing, unknown type.
            break;
        }
    }

    /**
     * Create the actions to add and create financial projects and local partner
     * projects.
     */
    private void addLinkedProjectsListeners() {

        // ------------------------
        // -- FINANCIAL
        // ------------------------

        // Adds the add financial project actions.
        view.getAddFinancialProjectButton().addListener(Events.OnClick, new Listener<ButtonEvent>() {

            final FormWindow window = new FormWindow();

            @Override
            public void handleEvent(ButtonEvent be) {

                // Gets all potential financial projects.
                dispatcher.execute(new GetProjects(), null, new AsyncCallback<ProjectListResult>() {

                    @Override
                    public void onFailure(Throwable e) {

                        Log.error("[execute] Error while getting the projects list with type " + null + ".", e);
                        MessageBox.alert(I18N.CONSTANTS.createProjectTypeError(),
                                I18N.CONSTANTS.createProjectTypeErrorDetails(), null);
                    }

                    @Override
                    public void onSuccess(ProjectListResult result) {

                        // Filters the project itself.
                        result.getList().remove(projectPresenter.getCurrentProjectDTO().light());

                        // Checks if there is at least one funding
                        // project available.
                        if (result.getList().isEmpty()) {
                            MessageBox.alert(I18N.CONSTANTS.createProjectTypeFundingSelectNone(),
                                    I18N.CONSTANTS.createProjectTypeFundingSelectNoneDetails(), null);
                            return;
                        }

                        // Generates a human-readable name to select a
                        // project.
                        for (final ProjectDTOLight p : result.getList()) {
                            p.generateCompleteName();
                        }

                        // Resets the window.
                        window.clear();

                        // Adds fields.
                        final ComboBox<ProjectDTOLight> projects = window.addChoicesList(
                                I18N.CONSTANTS.createProjectTypeFunding(), result.getList(), false, "completeName");

                        final LabelField modelTypeLabel = window.addLabelField(I18N.CONSTANTS.createProjectType());
                        modelTypeLabel.setHeight(25);

                        final NumberField amountField = window.addNumberField(I18N.CONSTANTS.projectFundedByDetails()
                                + " (" + I18N.CONSTANTS.currencyEuro() + ')', true);

                        final LabelField percentageField = window.addLabelField(I18N.CONSTANTS
                                .createProjectPercentage());
                        percentageField.setValue("0 %");

                        projects.addListener(Events.Select, new Listener<BaseEvent>() {

                            @Override
                            public void handleEvent(BaseEvent be) {

                                final ProjectModelType type = projects.getSelection().get(0)
                                        .getProjectModelType(authentication.getOrganizationId());

                                final Grid iconGrid = new Grid(1, 2);
                                iconGrid.setCellPadding(0);
                                iconGrid.setCellSpacing(0);

                                iconGrid.setWidget(0, 0, FundingIconProvider.getProjectTypeIcon(type, IconSize.MEDIUM)
                                        .createImage());
                                DOM.setStyleAttribute(iconGrid.getCellFormatter().getElement(0, 0), "paddingTop", "2px");
                                iconGrid.setText(0, 1, ProjectModelType.getName(type));
                                DOM.setStyleAttribute(iconGrid.getCellFormatter().getElement(0, 1), "paddingLeft",
                                        "5px");

                                modelTypeLabel.setText(iconGrid.getElement().getString());
                            }
                        });

                        amountField.addListener(Events.Change, new Listener<BaseEvent>() {

                            @Override
                            public void handleEvent(BaseEvent be) {

                                if (amountField.getValue() == null) {
                                    amountField.setValue(0);
                                }

                                percentageField.setText(NumberUtils.ratioAsString(amountField.getValue(),
                                        projectPresenter.getCurrentProjectDTO().getPlannedBudget()));
                            }
                        });

                        // Adds the submit listener.
                        window.addFormSubmitListener(new FormSubmitListener() {

                            @Override
                            public void formSubmitted(Object... values) {

                                // Checks that the values are correct.
                                if (values == null || values.length < 4) {
                                    return;
                                }

                                final Object value0 = values[0];
                                if (!(value0 instanceof ProjectDTOLight)) {
                                    return;
                                }

                                final Object value1 = values[2];
                                if (!(value1 instanceof Number)) {
                                    return;
                                }

                                // Retrieves the selected project and adds it as
                                // a new funding.
                                final ProjectDTOLight p = (ProjectDTOLight) value0;

                                // Sets the funding parameters.
                                final HashMap<String, Object> parameters = new HashMap<String, Object>();
                                parameters.put("fundingId", p.getId());
                                parameters.put("fundedId", projectPresenter.getCurrentProjectDTO().getId());
                                parameters.put("percentage", ((Number) value1).doubleValue());

                                // Create the new funding.
                                dispatcher.execute(new CreateEntity("ProjectFunding", parameters), null,
                                        new AsyncCallback<CreateResult>() {

                                            @Override
                                            public void onFailure(Throwable e) {
                                                Log.error("[execute] Error while creating a new funding.", e);
                                                MessageBox.alert(
                                                        I18N.CONSTANTS.createProjectTypeFundingCreationError(),
                                                        I18N.CONSTANTS.createProjectTypeFundingCreationDetails(), null);
                                            }

                                            @Override
                                            public void onSuccess(CreateResult result) {

                                                Notification.show(I18N.CONSTANTS.infoConfirmation(),
                                                        I18N.CONSTANTS.createProjectTypeFundingSelectOk());

                                                final ProjectFundingDTO r = (ProjectFundingDTO) result.getEntity();

                                                // Updates.
                                                if (result.getNewId() != -1) {
                                                    view.getFinancialProjectGrid().getStore().update(r);
                                                }
                                                // Creates.
                                                else {
                                                    view.getFinancialProjectGrid().getStore().add(r);
                                                }

                                                projectPresenter.getCurrentProjectDTO().addFunding(r);
                                            }
                                        });
                            }
                        });

                        // Show the selection window.
                        window.show(I18N.CONSTANTS.createProjectTypeFunding(),
                                I18N.CONSTANTS.createProjectTypeFundingSelectDetails() + " '"
                                        + projectPresenter.getCurrentProjectDTO().getName() + "'.");
                    }
                });
            };
        });

        // Adds the create financial project actions.
        view.getCreateFinancialProjectButton().addListener(Events.OnClick, new Listener<ButtonEvent>() {

            private final CreateProjectWindow window = new CreateProjectWindow(dispatcher, authentication);

            {
                window.addListener(new CreateProjectListener() {

                    @Override
                    public void projectCreated(ProjectDTOLight project) {
                        // nothing to do (must not be called).
                    }

                    @Override
                    public void projectCreatedAsFunding(ProjectDTOLight project, double percentage) {

                        // Sets the funding parameters.
                        final HashMap<String, Object> parameters = new HashMap<String, Object>();
                        parameters.put("fundingId", project.getId());
                        parameters.put("fundedId", projectPresenter.getCurrentProjectDTO().getId());
                        parameters.put("percentage", percentage);

                        // Create the new funding.
                        dispatcher.execute(new CreateEntity("ProjectFunding", parameters), null,
                                new AsyncCallback<CreateResult>() {

                                    @Override
                                    public void onFailure(Throwable e) {
                                        Log.error("[execute] Error while creating a new funding.", e);
                                        MessageBox.alert(I18N.CONSTANTS.createProjectTypeFundingCreationError(),
                                                I18N.CONSTANTS.createProjectTypeFundingCreationDetails(), null);
                                    }

                                    @Override
                                    public void onSuccess(CreateResult result) {

                                        Notification.show(I18N.CONSTANTS.infoConfirmation(),
                                                I18N.CONSTANTS.createProjectTypeFundingSelectOk());

                                        final ProjectFundingDTO r = (ProjectFundingDTO) result.getEntity();

                                        // Adds the just created funding to the
                                        // list.
                                        view.getFinancialProjectGrid().getStore().add(r);

                                        projectPresenter.getCurrentProjectDTO().addFunding(r);
                                    }
                                });
                    }

                    @Override
                    public void projectCreatedAsFunded(ProjectDTOLight project, double percentage) {
                        // nothing to do (must not be called).
                    }
                });
            }

            @Override
            public void handleEvent(ButtonEvent be) {
                window.show(Mode.FUNDING, projectPresenter.getCurrentProjectDTO().light());
            }
        });

        // ------------------------
        // -- LOCAL PARTNER
        // ------------------------

        // Adds the add local partner project action.
        view.getAddLocalPartnerProjectButton().addListener(Events.OnClick, new Listener<ButtonEvent>() {

            final FormWindow window = new FormWindow();

            @Override
            public void handleEvent(ButtonEvent be) {

                // Gets all potential local partner projects.
                dispatcher.execute(new GetProjects(), null, new AsyncCallback<ProjectListResult>() {

                    @Override
                    public void onFailure(Throwable e) {

                        Log.error("[execute] Error while getting the projects list with type " + null + ".", e);
                        MessageBox.alert(I18N.CONSTANTS.createProjectTypeError(),
                                I18N.CONSTANTS.createProjectTypeErrorDetails(), null);
                    }

                    @Override
                    public void onSuccess(ProjectListResult result) {

                        // Filters the project itself.
                        result.getList().remove(projectPresenter.getCurrentProjectDTO().light());

                        // Checks if there is at least one local partner
                        // project available.
                        if (result.getList().isEmpty()) {
                            MessageBox.alert(I18N.CONSTANTS.createProjectTypePartnerSelectNone(),
                                    I18N.CONSTANTS.createProjectTypePartnerSelectNoneDetails(), null);
                            return;
                        }

                        // Generates a human-readable name to select a
                        // project.
                        for (final ProjectDTOLight p : result.getList()) {
                            p.generateCompleteName();
                        }

                        // Resets the window.
                        window.clear();

                        // Adds fields.
                        final ComboBox<ProjectDTOLight> projects = window.addChoicesList(
                                I18N.CONSTANTS.createProjectTypeFunding(), result.getList(), false, "completeName");

                        final LabelField modelTypeLabel = window.addLabelField(I18N.CONSTANTS.createProjectType());
                        modelTypeLabel.setHeight(25);

                        final NumberField amountField = window.addNumberField(I18N.CONSTANTS.projectFinancesDetails()
                                + " (" + I18N.CONSTANTS.currencyEuro() + ')', true);

                        final LabelField percentageField = window.addLabelField(I18N.CONSTANTS
                                .createProjectPercentage());
                        percentageField.setValue("0 %");

                        projects.addListener(Events.Select, new Listener<BaseEvent>() {

                            @Override
                            public void handleEvent(BaseEvent be) {

                                final ProjectModelType type = projects.getSelection().get(0)
                                        .getProjectModelType(authentication.getOrganizationId());

                                final Grid iconGrid = new Grid(1, 2);
                                iconGrid.setCellPadding(0);
                                iconGrid.setCellSpacing(0);

                                iconGrid.setWidget(0, 0, FundingIconProvider.getProjectTypeIcon(type, IconSize.MEDIUM)
                                        .createImage());
                                DOM.setStyleAttribute(iconGrid.getCellFormatter().getElement(0, 0), "paddingTop", "2px");
                                iconGrid.setText(0, 1, ProjectModelType.getName(type));
                                DOM.setStyleAttribute(iconGrid.getCellFormatter().getElement(0, 1), "paddingLeft",
                                        "5px");

                                modelTypeLabel.setText(iconGrid.getElement().getString());
                            }
                        });

                        amountField.addListener(Events.Change, new Listener<BaseEvent>() {

                            @Override
                            public void handleEvent(BaseEvent be) {

                                if (amountField.getValue() == null) {
                                    amountField.setValue(0);
                                }

                                final List<ProjectDTOLight> selection = projects.getSelection();

                                if (selection == null || selection.isEmpty()) {
                                    percentageField.setText(I18N.CONSTANTS.createProjectPercentageNotAvailable());
                                } else {
                                    percentageField.setText(NumberUtils.ratioAsString(amountField.getValue(), selection
                                            .get(0).getPlannedBudget()));
                                }
                            }
                        });

                        // Adds the submit listener.
                        window.addFormSubmitListener(new FormSubmitListener() {

                            @Override
                            public void formSubmitted(Object... values) {

                                // Checks that the values are correct.
                                if (values == null || values.length < 2) {
                                    return;
                                }

                                final Object value0 = values[0];
                                if (!(value0 instanceof ProjectDTOLight)) {
                                    return;
                                }

                                final Object value1 = values[2];
                                if (!(value1 instanceof Number)) {
                                    return;
                                }

                                // Retrieves the selected project and adds it as
                                // a new partner.
                                final ProjectDTOLight p = (ProjectDTOLight) value0;

                                // Sets the local partner parameters.
                                final HashMap<String, Object> parameters = new HashMap<String, Object>();
                                parameters.put("fundingId", projectPresenter.getCurrentProjectDTO().getId());
                                parameters.put("fundedId", p.getId());
                                parameters.put("percentage", ((Number) value1).doubleValue());

                                // Create the new funding.
                                dispatcher.execute(new CreateEntity("ProjectFunding", parameters), null,
                                        new AsyncCallback<CreateResult>() {

                                            @Override
                                            public void onFailure(Throwable e) {
                                                Log.error("[execute] Error while creating a new funding.", e);
                                                MessageBox.alert(
                                                        I18N.CONSTANTS.createProjectTypeFundingCreationError(),
                                                        I18N.CONSTANTS.createProjectTypeFundingCreationDetails(), null);
                                            }

                                            @Override
                                            public void onSuccess(CreateResult result) {

                                                Notification.show(I18N.CONSTANTS.infoConfirmation(),
                                                        I18N.CONSTANTS.createProjectTypePartnerSelectOk());

                                                final ProjectFundingDTO r = (ProjectFundingDTO) result.getEntity();

                                                // Updates.
                                                if (result.getNewId() != -1) {
                                                    view.getLocalPartnerProjectGrid().getStore().update(r);
                                                }
                                                // Creates.
                                                else {
                                                    view.getLocalPartnerProjectGrid().getStore().add(r);
                                                }

                                                projectPresenter.getCurrentProjectDTO().addFunded(r);
                                            }
                                        });
                            }
                        });

                        // Show the selection window.
                        window.show(I18N.CONSTANTS.createProjectTypePartner(),
                                I18N.CONSTANTS.createProjectTypePartnerSelectDetails() + " '"
                                        + projectPresenter.getCurrentProjectDTO().getName() + "'.");
                    }
                });
            }
        });

        // Adds the create local partner project actions.
        view.getCreateLocalPartnerProjectButton().addListener(Events.OnClick, new Listener<ButtonEvent>() {

            private final CreateProjectWindow window = new CreateProjectWindow(dispatcher, authentication);

            {
                window.addListener(new CreateProjectListener() {

                    @Override
                    public void projectCreated(ProjectDTOLight project) {
                        // nothing to do (must not be called).
                    }

                    @Override
                    public void projectCreatedAsFunding(ProjectDTOLight project, double percentage) {
                        // nothing to do (must not be called).
                    }

                    @Override
                    public void projectCreatedAsFunded(ProjectDTOLight project, double percentage) {

                        // Sets the local partner parameters.
                        final HashMap<String, Object> parameters = new HashMap<String, Object>();
                        parameters.put("fundingId", projectPresenter.getCurrentProjectDTO().getId());
                        parameters.put("fundedId", project.getId());
                        parameters.put("percentage", percentage);

                        // Create the new funding.
                        dispatcher.execute(new CreateEntity("ProjectFunding", parameters), null,
                                new AsyncCallback<CreateResult>() {

                                    @Override
                                    public void onFailure(Throwable e) {
                                        Log.error("[execute] Error while creating a new funding.", e);
                                        MessageBox.alert(I18N.CONSTANTS.createProjectTypeFundingCreationError(),
                                                I18N.CONSTANTS.createProjectTypeFundingCreationDetails(), null);
                                    }

                                    @Override
                                    public void onSuccess(CreateResult result) {

                                        Notification.show(I18N.CONSTANTS.infoConfirmation(),
                                                I18N.CONSTANTS.createProjectTypePartnerSelectOk());

                                        final ProjectFundingDTO r = (ProjectFundingDTO) result.getEntity();

                                        // Adds the just created local partner
                                        // to the list.
                                        view.getLocalPartnerProjectGrid().getStore().add(r);

                                        projectPresenter.getCurrentProjectDTO().addFunded(r);
                                    }
                                });
                    }
                });
            }

            @Override
            public void handleEvent(ButtonEvent be) {
                window.show(Mode.FUNDED, projectPresenter.getCurrentProjectDTO().light());
            }
        });
    }
}
