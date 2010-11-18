package org.sigmah.client.page.dashboard;

import java.util.ArrayList;
import java.util.HashMap;

import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.dispatch.remote.Authentication;
import org.sigmah.client.i18n.I18N;
import org.sigmah.shared.command.CreateEntity;
import org.sigmah.shared.command.GetCountries;
import org.sigmah.shared.command.GetProjectModels;
import org.sigmah.shared.command.result.CountryResult;
import org.sigmah.shared.command.result.CreateResult;
import org.sigmah.shared.command.result.ProjectModelListResult;
import org.sigmah.shared.domain.ProjectModelType;
import org.sigmah.shared.dto.CountryDTO;
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
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.NumberField;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.user.client.rpc.AsyncCallback;

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

    private final ArrayList<CreateProjectListener> listeners;
    private final Dispatcher dispatcher;
    private final Window window;
    private final FormPanel formPanel;
    private final TextField<String> nameField;
    private final TextField<String> fullNameField;
    private final ComboBox<ProjectModelDTOLight> modelsField;
    private final TextField<String> modelType;
    private final ListStore<ProjectModelDTOLight> modelsStore;
    private final ListStore<CountryDTO> countriesStore;
    private final ComboBox<CountryDTO> countriesField;
    private final NumberField budgetField;
    private final NumberField percentageField;
    private Mode currentMode;

    /**
     * Counter to wait that required data are loaded before showing the window.
     */
    private int countBeforeShow;

    /**
     * Flag to display only one alert message.
     */
    private boolean alert = false;

    public CreateProjectWindow(final Dispatcher dispatcher, final Authentication authentication) {

        listeners = new ArrayList<CreateProjectListener>();

        this.dispatcher = dispatcher;

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
        budgetField.setFieldLabel(I18N.CONSTANTS.projectPlannedBudget());
        budgetField.setValue(0);
        budgetField.setAllowBlank(false);

        // Models list.
        modelsField = new ComboBox<ProjectModelDTOLight>();
        modelsField.setFieldLabel(I18N.CONSTANTS.projectModel());
        modelsField.setAllowBlank(false);
        modelsField.setValueField("id");
        modelsField.setDisplayField("name");
        modelsField.setEditable(true);

        // Model type
        modelType = new TextField<String>();
        modelType.setFieldLabel(I18N.CONSTANTS.createProjectType());
        modelType.setEnabled(false);
        modelType.setAllowBlank(true);

        modelsField.addListener(Events.Select, new Listener<BaseEvent>() {

            @Override
            public void handleEvent(BaseEvent be) {
                modelType.setValue(ProjectModelType.getName(modelsField.getSelection().get(0)
                        .getVisibility(authentication.getOrganizationId())));
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

        // Percentage for funding.
        percentageField = new NumberField();

        // Countries list.
        countriesField = new ComboBox<CountryDTO>();
        countriesField.setFieldLabel(I18N.CONSTANTS.projectCountry());
        countriesField.setAllowBlank(false);
        countriesField.setValueField("id");
        countriesField.setDisplayField("name");
        countriesField.setEditable(true);

        // Countries list store.
        countriesStore = new ListStore<CountryDTO>();
        countriesStore.addListener(Events.Add, new Listener<StoreEvent<CountryDTO>>() {

            @Override
            public void handleEvent(StoreEvent<CountryDTO> be) {
                countriesField.setEnabled(true);
            }
        });

        countriesStore.addListener(Events.Clear, new Listener<StoreEvent<CountryDTO>>() {

            @Override
            public void handleEvent(StoreEvent<CountryDTO> be) {
                countriesField.setEnabled(false);
            }
        });
        countriesField.setStore(countriesStore);

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

        formPanel.add(nameField);
        formPanel.add(fullNameField);
        formPanel.add(budgetField);
        formPanel.add(countriesField);
        formPanel.add(modelsField);
        formPanel.add(modelType);
        formPanel.add(percentageField);
        formPanel.addButton(createButton);

        formPanel.setLabelWidth(165);

        // Main window panel.
        final ContentPanel mainPanel = new ContentPanel();
        mainPanel.setHeaderVisible(false);
        mainPanel.setLayout(new FitLayout());
        mainPanel.add(formPanel);

        // Window.
        window = new Window();
        window.setHeading(I18N.CONSTANTS.createProject());
        window.setSize(415, 280);
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
        final int countryId = countriesField.getValue().getId();

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
            sb.append(" ; country id=");
            sb.append(countryId);

            Log.debug(sb.toString());
        }

        // Stores the project properties in a map to be send to the server.
        final HashMap<String, Object> projectProperties = new HashMap<String, Object>();
        projectProperties.put("name", name);
        projectProperties.put("fullName", fullName);
        projectProperties.put("budget", budget);
        projectProperties.put("modelId", modelId);
        projectProperties.put("countryId", countryId);
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
                    fireProjectCreatedAsFunding((ProjectDTOLight) result.getEntity(), percentageField.getValue()
                            .doubleValue());
                    break;
                case FUNDED:
                    fireProjectCreatedAsFunded((ProjectDTOLight) result.getEntity(), percentageField.getValue()
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
     */
    private void missingRequiredData() {

        if (alert) {
            return;
        }

        alert = true;

        MessageBox.alert(I18N.CONSTANTS.createProjectDisable(), I18N.CONSTANTS.createProjectDisableDetails(), null);

        window.hide();
    }

    /**
     * Initializes and show the window.
     */
    public void show() {
        show(Mode.SIMPLE);
    }

    /**
     * Initializes and show the window.
     * 
     * @param mode
     *            The display mode.
     */
    public void show(Mode mode) {

        currentMode = mode;

        // Resets window state.
        nameField.setValue(null);
        fullNameField.setValue(null);
        modelsField.setValue(null);
        countriesField.setValue(null);
        modelType.setValue(null);
        modelsStore.removeAll();
        countriesStore.removeAll();
        alert = false;

        // Manages the display mode.
        switch (currentMode) {
        case FUNDING:
            percentageField.setVisible(true);
            percentageField.setValue(0);
            percentageField.setAllowBlank(false);
            percentageField.setFieldLabel(I18N.CONSTANTS.projectFundedByDetails());
            break;
        case FUNDED:
            percentageField.setVisible(true);
            percentageField.setValue(0);
            percentageField.setAllowBlank(false);
            percentageField.setFieldLabel(I18N.CONSTANTS.projectFinancesDetails());
            break;
        default:
            percentageField.setVisible(false);
            percentageField.setAllowBlank(true);
            break;
        }

        // There are two remote calls.
        countBeforeShow = 2;

        // Retrieves countries.
        dispatcher.execute(new GetCountries(), null, new AsyncCallback<CountryResult>() {

            @Override
            public void onFailure(Throwable arg0) {
                missingRequiredData();
            }

            @Override
            public void onSuccess(CountryResult result) {

                if (result.getData() == null || result.getData().isEmpty()) {
                    missingRequiredData();
                    return;
                }

                countriesStore.add(result.getData());
                countriesStore.commitChanges();

                countBeforeShow();
            }
        });

        // Retrieves project models (with an optional filter on the type).
        dispatcher.execute(new GetProjectModels(), null, new AsyncCallback<ProjectModelListResult>() {

            @Override
            public void onFailure(Throwable arg0) {
                missingRequiredData();
            }

            @Override
            public void onSuccess(ProjectModelListResult result) {

                if (result.getList() == null || result.getList().isEmpty()) {
                    missingRequiredData();
                    return;
                }

                modelsStore.add(result.getList());
                modelsStore.commitChanges();

                countBeforeShow();
            }
        });
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
