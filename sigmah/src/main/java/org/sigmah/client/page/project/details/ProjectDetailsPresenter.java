package org.sigmah.client.page.project.details;

import java.util.ArrayList;
import java.util.Date;

import org.sigmah.client.CountriesList;
import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.dispatch.monitor.MaskingAsyncMonitor;
import org.sigmah.client.dispatch.remote.Authentication;
import org.sigmah.client.i18n.I18N;
import org.sigmah.client.page.project.ProjectPresenter;
import org.sigmah.client.page.project.SubPresenter;
import org.sigmah.client.util.Notification;
import org.sigmah.shared.command.GetValue;
import org.sigmah.shared.command.UpdateProject;
import org.sigmah.shared.command.result.ValueResult;
import org.sigmah.shared.command.result.VoidResult;
import org.sigmah.shared.domain.profile.GlobalPermissionEnum;
import org.sigmah.shared.dto.CountryDTO;
import org.sigmah.shared.dto.ProjectDTO;
import org.sigmah.shared.dto.ProjectDetailsDTO;
import org.sigmah.shared.dto.element.DefaultFlexibleElementDTO;
import org.sigmah.shared.dto.element.FlexibleElementDTO;
import org.sigmah.shared.dto.element.handler.ValueEvent;
import org.sigmah.shared.dto.element.handler.ValueHandler;
import org.sigmah.shared.dto.layout.LayoutConstraintDTO;
import org.sigmah.shared.dto.layout.LayoutDTO;
import org.sigmah.shared.dto.layout.LayoutGroupDTO;
import org.sigmah.shared.dto.profile.ProfileUtils;

import com.allen_sauer.gwt.log.client.Log;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Label;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.FieldSet;
import com.extjs.gxt.ui.client.widget.layout.FormData;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Grid;

public class ProjectDetailsPresenter implements SubPresenter {

    /**
     * Description of the view managed by this presenter.
     */
    public static abstract class View extends ContentPanel {

        public abstract Button getSaveButton();

        public abstract ContentPanel getMainPanel();
    }

    /**
     * This presenter view.
     */
    private View view;

    /**
     * The dispatcher.
     */
    private final Dispatcher dispatcher;

    private final Authentication authentication;

    private final CountriesList countriesList;

    /**
     * The main project presenter.
     */
    private final ProjectPresenter projectPresenter;

    /**
     * List of values changes.
     */
    private ArrayList<ValueEvent> valueChanges = new ArrayList<ValueEvent>();

    /**
     * The counter before the main panel is unmasked.
     */
    private int maskCount;

    public ProjectDetailsPresenter(Dispatcher dispatcher, Authentication authentication,
            ProjectPresenter projectPresenter, CountriesList countriesList) {
        this.dispatcher = dispatcher;
        this.projectPresenter = projectPresenter;
        this.authentication = authentication;
        this.countriesList = countriesList;
    }

    @Override
    public Component getView() {

        if (view == null) {
            view = new ProjectDetailsView();
            addListeners();
        }

        valueChanges.clear();
        view.getSaveButton().disable();

        load(projectPresenter.getCurrentProjectDTO().getProjectModelDTO().getProjectDetailsDTO());

        return view;
    }

    @Override
    public void discardView() {
        this.view = null;
    }

    @Override
    public void viewDidAppear() {
        // nothing to do.
    }

    /**
     * Initializes the presenter.
     */
    private void addListeners() {

        // Save action.
        view.getSaveButton().addListener(Events.OnClick, new Listener<ButtonEvent>() {

            @Override
            public void handleEvent(ButtonEvent be) {

                view.getSaveButton().disable();

                final UpdateProject updateProject = new UpdateProject(projectPresenter.getCurrentProjectDTO().getId(),
                        valueChanges);

                dispatcher.execute(updateProject,
                        new MaskingAsyncMonitor(view.getMainPanel(), I18N.CONSTANTS.loading()),
                        new AsyncCallback<VoidResult>() {

                            @Override
                            public void onFailure(Throwable caught) {

                                MessageBox.alert(I18N.CONSTANTS.save(), I18N.CONSTANTS.saveError(), null);
                            }

                            @Override
                            public void onSuccess(VoidResult result) {

                                Notification.show(I18N.CONSTANTS.infoConfirmation(), I18N.CONSTANTS.saveConfirm());

                                // Checks if there is any update needed to the
                                // local project instance.
                                boolean refreshBanner = false;
                                for (ValueEvent event : valueChanges) {
                                    if (event.getSource() instanceof DefaultFlexibleElementDTO) {
                                        updateCurrentProject(((DefaultFlexibleElementDTO) event.getSource()),
                                                event.getSingleValue());
                                        refreshBanner = true;
                                    }
                                }

                                valueChanges.clear();

                                if (refreshBanner) {
                                    projectPresenter.refreshBanner();
                                }
                            }
                        });
            }
        });
    }

    /**
     * Loads the presenter with the project details.
     * 
     * @param details
     *            The details.
     */
    private void load(ProjectDetailsDTO details) {

        // Clear panel.
        view.getMainPanel().removeAll();

        // Layout.
        final LayoutDTO layout = details.getLayoutDTO();

        // If the element are read only.
        final boolean readOnly = !ProfileUtils.isGranted(authentication, GlobalPermissionEnum.EDIT_PROJECT);

        // Counts elements.
        int count = 0;
        for (final LayoutGroupDTO groupDTO : layout.getLayoutGroupsDTO()) {
            count += groupDTO.getLayoutConstraintsDTO().size();
        }

        // Executes layout.
        if (count != 0) {

            // Masks the main panel.
            mask(count);

            final Grid gridLayout = (Grid) layout.getWidget();

            for (final LayoutGroupDTO groupLayout : layout.getLayoutGroupsDTO()) {

                // Creates the fieldset and positions it.
                final FieldSet formPanel = (FieldSet) groupLayout.getWidget();
                gridLayout.setWidget(groupLayout.getRow(), groupLayout.getColumn(), formPanel);

                // For each constraint in the current layout group.
                if (groupLayout.getLayoutConstraintsDTO() != null) {
                    for (final LayoutConstraintDTO constraintDTO : groupLayout.getLayoutConstraintsDTO()) {

                        // Gets the element managed by this constraint.
                        final FlexibleElementDTO elementDTO = constraintDTO.getFlexibleElementDTO();

                        // --
                        // -- ELEMENT VALUE
                        // --

                        // Retrieving the current amendment id
                        Integer amendmentId = null;
                        if(projectPresenter.getCurrentProjectDTO().getCurrentAmendment() != null)
                            amendmentId = projectPresenter.getCurrentProjectDTO().getCurrentAmendment().getId();

                        // Remote call to ask for this element value.
                        final GetValue command = new GetValue(projectPresenter.getCurrentProjectDTO().getId(),
                                elementDTO.getId(), elementDTO.getEntityName(), amendmentId);
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

                                // Configures the flexible element for the
                                // current application state before generating
                                // its component.
                                elementDTO.setService(dispatcher);
                                elementDTO.setAuthentication(authentication);
                                elementDTO.setCountries(countriesList);
                                elementDTO.setCurrentContainerDTO(projectPresenter.getCurrentProjectDTO());
                                elementDTO.assignValue(valueResult);

                                // Generates element component (with the value).
                                elementDTO.init();
                                final Component elementComponent = elementDTO.getElementComponent(valueResult,
                                        !readOnly && !valueResult.isAmendment());

                                // Component width.
                                final FormData formData;
                                if (elementDTO.getPreferredWidth() == 0) {
                                    formData = new FormData("100%");
                                } else {
                                    formData = new FormData(elementDTO.getPreferredWidth(), -1);
                                }

                                if (elementComponent != null) {
                                    formPanel.add(elementComponent, formData);
                                }
                                formPanel.layout();

                                // --
                                // -- ELEMENT HANDLERS
                                // --

                                // Adds a value change handler to this element.
                                elementDTO.addValueHandler(new ValueHandlerImpl());

                                unmask();
                            }
                        });
                    }
                }
            }

            view.getMainPanel().add(gridLayout);
        }
        // Default details page.
        else {
            final Label l = new Label(I18N.CONSTANTS.projectDetailsNoDetails());
            l.addStyleName("project-label-10");
            view.getMainPanel().add(l);
        }

        view.layout();
    }

    /**
     * Mask the main panel and set the mask counter.
     * 
     * @param count
     *            The mask counter.
     */
    private void mask(int count) {

        if (count <= 0) {
            return;
        }

        maskCount = count;
        view.getMainPanel().mask(I18N.CONSTANTS.loading());
    }

    /**
     * Decrements the mask counter and unmask the main panel if the counter
     * reaches <code>0</code>.
     */
    private void unmask() {
        maskCount--;
        if (maskCount == 0) {
            view.getMainPanel().unmask();
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
     * Internal class handling the value changes of the flexible elements.
     */
    private class ValueHandlerImpl implements ValueHandler {

        @Override
        public void onValueChange(ValueEvent event) {

            // Stores the change to be saved later.
            valueChanges.add(event);

            if (!projectPresenter.getCurrentDisplayedPhaseDTO().isEnded()) {

                // Enables the save action.
                view.getSaveButton().enable();
            }
        }
    }
}
