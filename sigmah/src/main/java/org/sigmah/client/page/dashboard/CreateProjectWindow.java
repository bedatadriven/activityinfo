package org.sigmah.client.page.dashboard;

import java.util.ArrayList;
import java.util.HashMap;

import org.sigmah.client.UserInfo;
import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.dispatch.remote.Authentication;
import org.sigmah.client.i18n.I18N;
import org.sigmah.client.page.project.dashboard.funding.FundingIconProvider;
import org.sigmah.client.page.project.dashboard.funding.FundingIconProvider.IconSize;
import org.sigmah.client.util.NumberUtils;
import org.sigmah.shared.command.CreateEntity;
import org.sigmah.shared.command.GetProjectModels;
import org.sigmah.shared.command.result.CreateResult;
import org.sigmah.shared.command.result.ProjectModelListResult;
import org.sigmah.shared.domain.ProjectModelType;
import org.sigmah.shared.dto.OrgUnitDTOLight;
import org.sigmah.shared.dto.ProjectDTOLight;
import org.sigmah.shared.dto.ProjectModelDTO;
import org.sigmah.shared.dto.ProjectModelDTOLight;

import com.allen_sauer.gwt.log.client.Log;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.store.StoreEvent;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.ComboBox.TriggerAction;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.LabelField;
import com.extjs.gxt.ui.client.widget.form.NumberField;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Grid;

/**
 * Manages a pop-up window to create a new project.
 * 
 * @author tmi
 * 
 */
public class CreateProjectWindow {

    /**
     * Defines the different modes for the creation.
     * 
     * @author tmi
     * 
     */
    public static enum Mode {

        /**
         * Create project mode.
         */
        SIMPLE,

        /**
         * Create a project and link it with another project as a funding
         * project.
         */
        FUNDING,

        /**
         * Create a project and link it with another project as a funded
         * project.
         */
        FUNDED;
    }

    /**
     * Listener.
     * 
     * @author tmi
     * 
     */
    public static interface CreateProjectListener {

        /**
         * Method called when a project is created in the {@link Mode#SIMPLE}
         * mode.
         * 
         * @param project
         *            The new project.
         */
        public void projectCreated(ProjectDTOLight project);

        /**
         * Method called when a project is created in the {@link Mode#FUNDING}
         * mode.
         * 
         * @param project
         *            The new project.
         * @param percentage
         *            The funding percentage.
         */
        public void projectCreatedAsFunding(ProjectDTOLight project, double percentage);

        /**
         * Method called when a project is created in the {@link Mode#FUNDED}
         * mode.
         * 
         * @param project
         *            The new project.
         * @param percentage
         *            The funding percentage.
         */
        public void projectCreatedAsFunded(ProjectDTOLight project, double percentage);
    }

    private static CreateProjectWindow instance;

    public static CreateProjectWindow getInstance(final Dispatcher dispatcher, final Authentication authentication,
            final UserInfo info) {
        if (instance == null) {
            instance = new CreateProjectWindow(dispatcher, authentication, info);
        }
        return instance;
    }

    private final ArrayList<CreateProjectListener> listeners;
    private final Dispatcher dispatcher;
    private final UserInfo info;
    private final Window window;
    private final FormPanel formPanel;
    private final TextField<String> nameField;
    private final TextField<String> fullNameField;
    private final ComboBox<ProjectModelDTOLight> modelsField;
    private final LabelField modelType;
    private final ListStore<ProjectModelDTOLight> modelsStore;
    private final NumberField budgetField;
    private final NumberField amountField;
    private final LabelField percentageField;
    private ProjectDTOLight currentFunding;
    private Mode currentMode;
    private final ListStore<OrgUnitDTOLight> orgUnitsStore;
    private final ComboBox<OrgUnitDTOLight> orgUnitsField;

    /**
     * Counter to wait that required data are loaded before showing the window.
     */
    private int countBeforeShow;

    /**
     * Flag to display only one alert message.
     */
    private boolean alert = false;

    private CreateProjectWindow(final Dispatcher dispatcher, final Authentication authentication, final UserInfo info) {

        listeners = new ArrayList<CreateProjectListener>();

        this.dispatcher = dispatcher;
        this.info = info;

        // Name field.
        nameField = new TextField<String>();
        nameField.setMaxLength(16);
        nameField.setFieldLabel(I18N.CONSTANTS.projectName());
        nameField.setAllowBlank(false);

        // Full name field.
        fullNameField = new TextField<String>();
        fullNameField.setMaxLength(50);
        fullNameField.setFieldLabel(I18N.CONSTANTS.projectFullName());
        fullNameField.setAllowBlank(false);

        // Budget field.
        budgetField = new NumberField();
        budgetField.setFieldLabel(I18N.CONSTANTS.projectPlannedBudget() + " (" + I18N.CONSTANTS.currencyEuro() + ')');
        budgetField.setValue(0);
        budgetField.setAllowBlank(false);

        // Models list.
        modelsField = new ComboBox<ProjectModelDTOLight>();
        modelsField.setFieldLabel(I18N.CONSTANTS.projectModel());
        modelsField.setAllowBlank(false);
        modelsField.setValueField("id");
        modelsField.setDisplayField("name");
        modelsField.setEditable(true);
        modelsField.setEmptyText(I18N.CONSTANTS.projectModelEmptyChoice());
        modelsField.setTriggerAction(TriggerAction.ALL);

        // Model type
        modelType = new LabelField();
        modelType.setFieldLabel(I18N.CONSTANTS.createProjectType());

        modelsField.addListener(Events.Select, new Listener<BaseEvent>() {

            @Override
            public void handleEvent(BaseEvent be) {

                final ProjectModelType type = modelsField.getSelection().get(0)
                        .getVisibility(authentication.getOrganizationId());

                final Grid iconGrid = new Grid(1, 2);
                iconGrid.setCellPadding(0);
                iconGrid.setCellSpacing(0);

                iconGrid.setWidget(0, 0, FundingIconProvider.getProjectTypeIcon(type, IconSize.MEDIUM).createImage());
                DOM.setStyleAttribute(iconGrid.getCellFormatter().getElement(0, 0), "paddingTop", "2px");
                iconGrid.setText(0, 1, ProjectModelType.getName(type));
                DOM.setStyleAttribute(iconGrid.getCellFormatter().getElement(0, 1), "paddingLeft", "5px");

                modelType.setText(iconGrid.getElement().getString());
            }
        });

        // Models list store.
        modelsStore = new ListStore<ProjectModelDTOLight>();
        modelsStore.addListener(Events.Add, new Listener<StoreEvent<ProjectModelDTO>>() {

            @Override
            public void handleEvent(StoreEvent<ProjectModelDTO> be) {
                modelsField.setEnabled(true);
            }
        });

        modelsStore.addListener(Events.Clear, new Listener<StoreEvent<ProjectModelDTO>>() {

            @Override
            public void handleEvent(StoreEvent<ProjectModelDTO> be) {
                modelsField.setEnabled(false);
            }
        });
        modelsField.setStore(modelsStore);

        // Amount for funding.
        amountField = new NumberField();
        amountField.addListener(Events.Change, new Listener<BaseEvent>() {

            @Override
            public void handleEvent(BaseEvent be) {

                // Checks that values are filled.
                if (amountField.getValue() == null || amountField.getValue().doubleValue() < 0) {
                    amountField.setValue(0);
                }

                if (budgetField.getValue() == null || budgetField.getValue().doubleValue() < 0) {
                    budgetField.setValue(0);
                    amountField.setValue(0);
                }

                // Computes the ratio between the allocated amount and the
                // funding project.
                if (currentFunding == null) {
                    percentageField.setText(I18N.CONSTANTS.createProjectPercentageNotAvailable());
                } else {

                    switch (currentMode) {
                    case FUNDED: {

                        final double fundingBudget = currentFunding.getPlannedBudget();
                        final double budget = budgetField.getValue().doubleValue();

                        // Checks the budget bounds and adjusts the allocated
                        // amount.
                        double min;
                        if (budget <= 0) {
                            amountField.setValue(0);
                            percentageField.setText("0.0 %");
                            break;
                        } else if ((min = Math.min(fundingBudget, budget)) < amountField.getValue().doubleValue()) {
                            amountField.setValue(min);
                        }

                        percentageField.setText(NumberUtils.ratioAsString(amountField.getValue().doubleValue(), budget));
                    }
                        break;
                    case FUNDING: {

                        final double fundingBudget = currentFunding.getPlannedBudget();
                        final double budget = budgetField.getValue().doubleValue();

                        if (fundingBudget <= 0) {
                            percentageField.setText("0.0 %");
                            return;
                        }

                        double min;
                        if (budget <= 0) {
                            amountField.setValue(0);
                            percentageField.setText("0.0 %");
                            break;
                        } else if ((min = Math.min(fundingBudget, budget)) < amountField.getValue().doubleValue()) {
                            amountField.setValue(min);
                        }

                        percentageField.setText(NumberUtils.ratioAsString(amountField.getValue().doubleValue(),
                                fundingBudget));
                    }
                        break;
                    default:
                        percentageField.setText(I18N.CONSTANTS.createProjectPercentageNotAvailable());
                        break;
                    }
                }
            }
        });

        // Percentage for funding.
        percentageField = new LabelField();
        percentageField.setFieldLabel(I18N.CONSTANTS.createProjectPercentage());

        // Org units list.
        orgUnitsField = new ComboBox<OrgUnitDTOLight>();
        orgUnitsField.setFieldLabel(I18N.CONSTANTS.orgunit());
        orgUnitsField.setAllowBlank(false);
        orgUnitsField.setValueField("id");
        orgUnitsField.setDisplayField("completeName");
        orgUnitsField.setEditable(true);
        orgUnitsField.setEmptyText(I18N.CONSTANTS.orgunitEmptyChoice());
        orgUnitsField.setTriggerAction(TriggerAction.ALL);

        // Org units list store.
        orgUnitsStore = new ListStore<OrgUnitDTOLight>();
        orgUnitsStore.addListener(Events.Add, new Listener<StoreEvent<OrgUnitDTOLight>>() {

            @Override
            public void handleEvent(StoreEvent<OrgUnitDTOLight> be) {
                orgUnitsField.setEnabled(true);
            }
        });

        orgUnitsStore.addListener(Events.Clear, new Listener<StoreEvent<OrgUnitDTOLight>>() {

            @Override
            public void handleEvent(StoreEvent<OrgUnitDTOLight> be) {
                orgUnitsField.setEnabled(false);
            }
        });

        orgUnitsField.setStore(orgUnitsStore);

        // Create button.
        final Button createButton = new Button(I18N.CONSTANTS.createProjectCreateButton());
        createButton.addListener(Events.OnClick, new Listener<ButtonEvent>() {
            @Override
            public void handleEvent(ButtonEvent be) {
                createProject();
            }
        });

        // Form panel.
        formPanel = new FormPanel();
        formPanel.setBodyBorder(false);
        formPanel.setHeaderVisible(false);
        formPanel.setPadding(5);
        formPanel.setLabelWidth(165);
        formPanel.setFieldWidth(350);

        formPanel.add(nameField);
        formPanel.add(fullNameField);
        formPanel.add(budgetField);
        formPanel.add(orgUnitsField);
        formPanel.add(modelsField);
        formPanel.add(modelType);
        formPanel.add(amountField);
        formPanel.add(percentageField);
        formPanel.addButton(createButton);

        // Main window panel.
        final ContentPanel mainPanel = new ContentPanel();
        mainPanel.setHeaderVisible(false);
        mainPanel.setLayout(new FitLayout());
        mainPanel.add(formPanel);

        // Window.
        window = new Window();
        window.setHeading(I18N.CONSTANTS.createProject());
        window.setSize(550, 290);
        window.setPlain(true);
        window.setModal(true);
        window.setBlinkModal(true);
        window.setLayout(new FitLayout());
        window.add(mainPanel);
    }

    /**
     * Creates a project for the given fields.
     */
    private void createProject() {

        // Checks the form completion.
        if (!formPanel.isValid()) {
            MessageBox.alert(I18N.CONSTANTS.createProjectFormIncomplete(),
                    I18N.CONSTANTS.createProjectFormIncompleteDetails(), null);
            return;
        }

        // Gets values.
        final String name = nameField.getValue();
        final String fullName = fullNameField.getValue();
        final Double budget = budgetField.getValue().doubleValue();
        final long modelId = modelsField.getValue().getId();
        // final int countryId = countriesField.getValue().getId();
        final int orgUnitId = orgUnitsField.getValue().getId();

        if (Log.isDebugEnabled()) {

            final StringBuilder sb = new StringBuilder();
            sb.append("Create a new project with parameters: ");
            sb.append("name=");
            sb.append(name);
            sb.append(" ; full name=");
            sb.append(fullName);
            sb.append(" ; budget=");
            sb.append(budget);
            sb.append(" ; model id=");
            sb.append(modelId);
            sb.append(" ; org unit id=");
            sb.append(orgUnitId);

            Log.debug(sb.toString());
        }

        // Stores the project properties in a map to be send to the server.
        final HashMap<String, Object> projectProperties = new HashMap<String, Object>();
        projectProperties.put("name", name);
        projectProperties.put("fullName", fullName);
        projectProperties.put("budget", budget);
        projectProperties.put("modelId", modelId);
        projectProperties.put("orgUnitId", orgUnitId);
        projectProperties.put("calendarName", I18N.CONSTANTS.calendarDefaultName());

        // Creates the project.
        dispatcher.execute(new CreateEntity("Project", projectProperties), null, new AsyncCallback<CreateResult>() {

            @Override
            public void onFailure(Throwable arg0) {
                MessageBox.alert(I18N.CONSTANTS.createProjectFailed(), I18N.CONSTANTS.createProjectFailedDetails(),
                        null);
            }

            @Override
            public void onSuccess(CreateResult result) {

                if (Log.isDebugEnabled()) {
                    Log.debug("Project created with id #" + result.getNewId() + ".");
                }

                // Manages the display mode.
                switch (currentMode) {
                case FUNDING:
                    fireProjectCreatedAsFunding((ProjectDTOLight) result.getEntity(), amountField.getValue()
                            .doubleValue());
                    break;
                case FUNDED:
                    fireProjectCreatedAsFunded((ProjectDTOLight) result.getEntity(), amountField.getValue()
                            .doubleValue());
                    break;
                default:
                    fireProjectCreated((ProjectDTOLight) result.getEntity());
                    break;
                }
            }
        });

        window.hide();
    }

    /**
     * Informs the user that some required data cannot be recovered. The project
     * cannot be created.
     * 
     * @param msg
     *            The alert message.
     */
    private void missingRequiredData(String msg) {

        if (alert) {
            return;
        }

        alert = true;

        MessageBox.alert(I18N.CONSTANTS.createProjectDisable(), msg, null);

        window.hide();
    }

    /**
     * Initializes and show the window.
     */
    public void show() {
        show(Mode.SIMPLE, null);
    }

    /**
     * Initializes and show the window.
     * 
     * @param mode
     *            The display mode.
     * @param funding
     *            The current project which is linked to the created project.
     */
    public void show(Mode mode, ProjectDTOLight funding) {

        currentMode = mode;
        currentFunding = funding;

        // Resets window state.
        nameField.reset();
        fullNameField.reset();
        modelsField.reset();
        // countriesField.reset();
        orgUnitsField.reset();
        modelType.setValue("");
        amountField.setValue(0);
        budgetField.setValue(0);
        percentageField.setText("0 %");
        alert = false;

        // Manages the display mode.
        switch (currentMode) {
        case FUNDING:
            amountField.setVisible(true);
            amountField.setValue(0);
            amountField.setAllowBlank(false);
            amountField.setFieldLabel(I18N.CONSTANTS.projectFundedByDetails() + " (" + I18N.CONSTANTS.currencyEuro()
                    + ')');
            percentageField.setVisible(true);
            break;
        case FUNDED:
            amountField.setVisible(true);
            amountField.setValue(0);
            amountField.setAllowBlank(false);
            amountField.setFieldLabel(I18N.CONSTANTS.projectFinancesDetails() + " (" + I18N.CONSTANTS.currencyEuro()
                    + ')');
            percentageField.setVisible(true);
            break;
        default:
            amountField.setVisible(false);
            amountField.setAllowBlank(true);
            percentageField.setVisible(false);
            break;
        }

        // There are two remote calls.
        countBeforeShow = 2;

        if (orgUnitsStore.getCount() == 0) {

            info.getOrgUnit(new AsyncCallback<OrgUnitDTOLight>() {

                @Override
                public void onSuccess(OrgUnitDTOLight result) {
                    fillOrgUnitsList(result);

                    if (orgUnitsStore.getCount() == 0) {
                        Log.error("[show] No available org unit.");
                        missingRequiredData(I18N.CONSTANTS.createProjectDisableOrgUnit());
                        return;
                    }

                    countBeforeShow();
                }

                @Override
                public void onFailure(Throwable caught) {
                    Log.error("[show] Error while getting the org units.", caught);
                    missingRequiredData(I18N.CONSTANTS.createProjectDisableOrgUnitError());
                }
            });
        } else {
            countBeforeShow();
        }

        if (modelsStore.getCount() == 0) {

            // Retrieves project models (with an optional filter on the type).
            dispatcher.execute(new GetProjectModels(), null, new AsyncCallback<ProjectModelListResult>() {

                @Override
                public void onFailure(Throwable caught) {
                    missingRequiredData(I18N.CONSTANTS.createProjectDisableModelError());
                }

                @Override
                public void onSuccess(ProjectModelListResult result) {

                    if (result.getList() == null || result.getList().isEmpty()) {
                        Log.error("[missingRequiredData] No available project model.");
                        missingRequiredData(I18N.CONSTANTS.createProjectDisableModel());
                        return;
                    }

                    modelsStore.add(result.getList());
                    modelsStore.commitChanges();

                    countBeforeShow();
                }
            });
        } else {
            countBeforeShow();
        }
    }

    /**
     * Decrements the local counter before showing the window.
     */
    private void countBeforeShow() {

        countBeforeShow--;

        if (countBeforeShow == 0) {
            window.show();
        }
    }

    /**
     * Fills combobox with the children of the given root org units.
     * 
     * @param root
     *            The root org unit.
     */
    private void fillOrgUnitsList(OrgUnitDTOLight root) {

        for (final OrgUnitDTOLight child : root.getChildrenDTO()) {
            recursiveFillOrgUnitsList(child);
        }
    }

    /**
     * Fills recursively the combobox from the given root org unit.
     * 
     * @param root
     *            The root org unit.
     */
    private void recursiveFillOrgUnitsList(OrgUnitDTOLight root) {

        if (root.getCanContainProjects()) {
            orgUnitsStore.add(root);
        }

        for (final OrgUnitDTOLight child : root.getChildrenDTO()) {
            recursiveFillOrgUnitsList(child);
        }
    }

    public void addListener(CreateProjectListener l) {
        listeners.add(l);
    }

    public void removeListener(CreateProjectListener l) {
        listeners.remove(l);
    }

    /**
     * Method called when a project is created.
     * 
     * @param project
     *            The new project.
     */
    public void fireProjectCreated(ProjectDTOLight project) {
        for (final CreateProjectListener l : listeners) {
            l.projectCreated(project);
        }
    }

    /**
     * Method called when a project is created.
     * 
     * @param project
     *            The new project.
     * @param percentage
     *            The funding percentage.
     */
    public void fireProjectCreatedAsFunding(ProjectDTOLight project, double percentage) {
        for (final CreateProjectListener l : listeners) {
            l.projectCreatedAsFunding(project, percentage);
        }
    }

    /**
     * Method called when a project is created.
     * 
     * @param project
     *            The new project.
     * @param percentage
     *            The funding percentage.
     */
    public void fireProjectCreatedAsFunded(ProjectDTOLight project, double percentage) {
        for (final CreateProjectListener l : listeners) {
            l.projectCreatedAsFunded(project, percentage);
        }
    }
}
