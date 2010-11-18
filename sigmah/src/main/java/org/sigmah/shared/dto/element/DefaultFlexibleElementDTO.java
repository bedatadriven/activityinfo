package org.sigmah.shared.dto.element;

import java.util.Date;

import org.sigmah.client.i18n.I18N;
import org.sigmah.shared.command.GetCountries;
import org.sigmah.shared.command.result.CountryResult;
import org.sigmah.shared.command.result.ValueResult;
import org.sigmah.shared.domain.element.DefaultFlexibleElementType;
import org.sigmah.shared.dto.CountryDTO;
import org.sigmah.shared.dto.UserPermissionDTO;
import org.sigmah.shared.dto.element.handler.RequiredValueEvent;
import org.sigmah.shared.dto.element.handler.ValueEvent;

import com.allen_sauer.gwt.log.client.Log;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.DatePickerEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.Label;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.ComboBox.TriggerAction;
import com.extjs.gxt.ui.client.widget.form.DateField;
import com.extjs.gxt.ui.client.widget.form.FieldSet;
import com.extjs.gxt.ui.client.widget.form.NumberField;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * DTO mapping class for entity element.DefaultFlexibleElement.
 * 
 * @author tmi
 * 
 */
public class DefaultFlexibleElementDTO extends FlexibleElementDTO {

    private static final long serialVersionUID = 3746586633233053639L;

    private transient ListStore<CountryDTO> countriesStore;
    private transient ListStore<UserPermissionDTO> usersStore;

    @Override
    public String getEntityName() {
        return "element.DefaultFlexibleElement";
    }

    // Type.
    public DefaultFlexibleElementType getType() {
        return get("type");
    }

    public void setType(DefaultFlexibleElementType type) {
        set("type", type);
    }

    public ListStore<CountryDTO> getCountriesStore() {
        return countriesStore;
    }

    public ListStore<UserPermissionDTO> getUsersStore() {
        return usersStore;
    }

    @Override
    public Component getComponent(ValueResult valueResult, boolean enabled) {

        final Component component;

        switch (getType()) {
        // Project code.
        case CODE: {

            final TextField<String> textField = createStringField(16, false);

            // Sets the value to the field.
            textField.setValue(currentProjectDTO.getName());

            // Sets the field label.
            setLabel(I18N.CONSTANTS.projectName());
            textField.setFieldLabel(getLabel());

            component = textField;
        }
            break;
        // Project title.
        case TITLE: {

            final TextField<String> textField = createStringField(50, true);

            // Sets the value to the field.
            textField.setValue(currentProjectDTO.getFullName());

            // Sets the field label.
            setLabel(I18N.CONSTANTS.projectFullName());
            textField.setFieldLabel(getLabel());

            component = textField;
        }
            break;

        case START_DATE: {

            final DateField dateField = createDateField(false);

            // Sets the value to the field.
            dateField.setValue(currentProjectDTO.getStartDate());

            // Sets the field label.
            setLabel(I18N.CONSTANTS.projectStartDate());
            dateField.setFieldLabel(getLabel());

            component = dateField;
        }
            break;
        case END_DATE: {

            final DateField dateField = createDateField(true);

            // Sets the value to the field.
            dateField.setValue(currentProjectDTO.getEndDate());

            // Sets the field label.
            setLabel(I18N.CONSTANTS.projectEndDate());
            dateField.setFieldLabel(getLabel());

            component = dateField;
        }
            break;
        case BUDGET: {

            // Planned budget.
            final NumberField plannedBudgetField = createNumberField(false);
            plannedBudgetField.setFieldLabel(I18N.CONSTANTS.projectPlannedBudget());

            // Spend budget.
            final NumberField spendBudgetField = createNumberField(false);
            spendBudgetField.setFieldLabel(I18N.CONSTANTS.projectSpendBudget());

            // Received budget.
            final NumberField receivedBudgetField = createNumberField(false);
            receivedBudgetField.setFieldLabel(I18N.CONSTANTS.projectReceivedBudget());

            // Spent ratio.
            final Label ratioLabel = new Label();
            ratioLabel.addStyleName("project-label-10");

            // Listener.
            final Listener<BaseEvent> listener = new Listener<BaseEvent>() {

                final double minValue = 0.0;

                @Override
                public void handleEvent(BaseEvent be) {

                    // Retrieves values.
                    final Number plannedBudget = plannedBudgetField.getValue();
                    final Double plannedBudgetAsDouble = plannedBudget.doubleValue();

                    final Number spendBudget = spendBudgetField.getValue();
                    final Double spendBudgetAsDouble = spendBudget.doubleValue();

                    final Number receivedBudget = receivedBudgetField.getValue();
                    final Double receivedBudgetAsDouble = receivedBudget.doubleValue();

                    // Checks the numbers intervals.
                    final boolean isValueOn = plannedBudgetAsDouble >= minValue && spendBudgetAsDouble >= minValue
                            && receivedBudgetAsDouble >= minValue;

                    // The numbers are saved as strings.
                    final String plannedBudgetRawValue = String.valueOf(plannedBudgetAsDouble);
                    final String spendBudgetRawValue = String.valueOf(spendBudgetAsDouble);
                    final String receivedBudgetRawValue = String.valueOf(receivedBudgetAsDouble);
                    final String rawValue = plannedBudgetRawValue + '|' + spendBudgetRawValue + '|'
                            + receivedBudgetRawValue;

                    final Double ratio = spendBudgetAsDouble / plannedBudgetAsDouble * 100;
                    ratioLabel.setText(I18N.CONSTANTS.flexibleElementBudgetDistributionRatio() + ": "
                            + ratio.intValue() + " %");

                    fireEvents(rawValue, isValueOn);
                }
            };

            plannedBudgetField.addListener(Events.Change, listener);
            spendBudgetField.addListener(Events.Change, listener);
            receivedBudgetField.addListener(Events.Change, listener);

            // Sets the value to the fields.
            plannedBudgetField.setValue(currentProjectDTO.getPlannedBudget());
            spendBudgetField.setValue(currentProjectDTO.getSpendBudget());
            receivedBudgetField.setValue(currentProjectDTO.getReceivedBudget());
            final Double ratio = currentProjectDTO.getSpendBudget() / currentProjectDTO.getPlannedBudget() * 100;
            ratioLabel
                    .setText(I18N.CONSTANTS.flexibleElementBudgetDistributionRatio() + ": " + ratio.intValue() + " %");

            // Fieldset.
            final FieldSet fieldset = new FieldSet();
            fieldset.setCollapsible(true);
            fieldset.setLayout(new FormLayout());

            fieldset.add(plannedBudgetField);
            fieldset.add(receivedBudgetField);
            fieldset.add(spendBudgetField);
            fieldset.add(ratioLabel);

            // Sets the field label.
            setLabel(I18N.CONSTANTS.projectBudget());
            fieldset.setHeading(getLabel());

            component = fieldset;
        }
            break;
        case COUNTRY: {

            final ComboBox<CountryDTO> comboBox = new ComboBox<CountryDTO>();
            comboBox.setEmptyText(I18N.CONSTANTS.flexibleElementDefaultSelectCountry());

            if (countriesStore == null) {
                countriesStore = new ListStore<CountryDTO>();
            }

            comboBox.setStore(countriesStore);
            comboBox.setDisplayField("name");
            comboBox.setValueField("id");
            comboBox.setTriggerAction(TriggerAction.ALL);
            comboBox.setEditable(true);
            comboBox.setAllowBlank(false);

            // Sets the field label.
            setLabel(I18N.CONSTANTS.projectCountry());
            comboBox.setFieldLabel(getLabel());

            // Retrieves the county list.
            if (countriesStore.getCount() == 0) {
                dispatcher.execute(new GetCountries(), null, new AsyncCallback<CountryResult>() {

                    @Override
                    public void onFailure(Throwable e) {
                        Log.error("[getComponent] Error while getting countries list.", e);
                    }

                    @Override
                    public void onSuccess(CountryResult result) {

                        // Fills the store.
                        countriesStore.add(result.getData());

                        // Sets the value to the field.
                        comboBox.setValue(currentProjectDTO.getCountry());

                        // Listens to the selection changes.
                        comboBox.addSelectionChangedListener(new SelectionChangedListener<CountryDTO>() {

                            @Override
                            public void selectionChanged(SelectionChangedEvent<CountryDTO> se) {

                                String value = null;
                                final boolean isValueOn;

                                // Gets the selected choice.
                                final CountryDTO choice = se.getSelectedItem();

                                // Checks if the choice isn't the default empty
                                // choice.
                                isValueOn = choice != null && choice.getId() != -1;

                                if (choice != null) {
                                    value = String.valueOf(choice.getId());
                                }

                                if (value != null) {
                                    // Fires value change event.
                                    handlerManager.fireEvent(new ValueEvent(DefaultFlexibleElementDTO.this, value));
                                }

                                // Required element ?
                                if (getValidates()) {
                                    handlerManager.fireEvent(new RequiredValueEvent(isValueOn));
                                }
                            }
                        });
                    }
                });
            } else {

                // Sets the value to the field.
                comboBox.setValue(currentProjectDTO.getCountry());

                // Listens to the selection changes.
                comboBox.addSelectionChangedListener(new SelectionChangedListener<CountryDTO>() {

                    @Override
                    public void selectionChanged(SelectionChangedEvent<CountryDTO> se) {

                        String value = null;
                        final boolean isValueOn;

                        // Gets the selected choice.
                        final CountryDTO choice = se.getSelectedItem();

                        // Checks if the choice isn't the default empty
                        // choice.
                        isValueOn = choice != null && choice.getId() != -1;

                        if (choice != null) {
                            value = String.valueOf(choice.getId());
                        }

                        if (value != null) {
                            // Fires value change event.
                            handlerManager.fireEvent(new ValueEvent(DefaultFlexibleElementDTO.this, value));
                        }

                        // Required element ?
                        if (getValidates()) {
                            handlerManager.fireEvent(new RequiredValueEvent(isValueOn));
                        }
                    }
                });
            }

            component = comboBox;
        }
            break;
        case OWNER: {

            final TextField<String> textField = new TextField<String>();
            textField.setReadOnly(true);

            // Sets the field label.
            setLabel(I18N.CONSTANTS.projectManager());
            textField.setFieldLabel(getLabel());

            // Sets the value to the field.
            textField.setValue(currentProjectDTO.getOwnerFirstName() != null ? currentProjectDTO.getOwnerFirstName()
                    + " " + currentProjectDTO.getOwnerName() : currentProjectDTO.getOwnerName());

            component = textField;

            /*
             * final ComboBox<UserPermissionDTO> comboBox = new
             * ComboBox<UserPermissionDTO>();
             * comboBox.setEmptyText("Select a manager...");
             * 
             * if (usersStore == null) { usersStore = new
             * ListStore<UserPermissionDTO>(); }
             * 
             * comboBox.setStore(usersStore); comboBox.setDisplayField("name");
             * comboBox.setValueField("email");
             * comboBox.setTriggerAction(TriggerAction.ALL);
             * comboBox.setEditable(true); comboBox.setAllowBlank(false);
             * 
             * // Sets the field label.
             * setLabel(I18N.CONSTANTS.projectManager());
             * comboBox.setFieldLabel(getLabel());
             * 
             * // Retrieves the users list. if (usersStore.getCount() == 0) {
             * dispatcher.execute(new GetUsers(currentProjectDTO.getId()), null,
             * new AsyncCallback<UserResult>() {
             * 
             * @Override public void onFailure(Throwable e) {
             * Log.error("[getComponent] Error while getting users list.", e); }
             * 
             * @Override public void onSuccess(UserResult result) {
             * 
             * // Owner permission. final UserPermissionDTO perm = new
             * UserPermissionDTO();
             * perm.setName(currentProjectDTO.getOwnerName());
             * perm.setFirstName(currentProjectDTO.getOwnerFirstName());
             * perm.setEmail(currentProjectDTO.getOwnerEmail());
             * 
             * // Fills the store. usersStore.add(perm);
             * usersStore.add(result.getData());
             * 
             * // Sets the value to the field. comboBox.setValue(perm);
             * 
             * // Listens to the selection changes.
             * comboBox.addSelectionChangedListener(new
             * SelectionChangedListener<UserPermissionDTO>() {
             * 
             * @Override public void
             * selectionChanged(SelectionChangedEvent<UserPermissionDTO> se) {
             * 
             * String value = null; final boolean isValueOn;
             * 
             * // Gets the selected choice. final UserPermissionDTO choice =
             * se.getSelectedItem();
             * 
             * // Checks if the choice isn't the default empty // choice.
             * isValueOn = choice != null && !"".equals(choice.getEmail());
             * 
             * if (choice != null) { value = choice.getEmail(); }
             * 
             * if (value != null) { // Fires value change event.
             * handlerManager.fireEvent(new
             * ValueEvent(DefaultFlexibleElementDTO.this, value)); }
             * 
             * // Required element ? if (getValidates()) {
             * handlerManager.fireEvent(new RequiredValueEvent(isValueOn)); } }
             * }); } }); } else {
             * 
             * // Owner permission. final UserPermissionDTO perm = new
             * UserPermissionDTO();
             * perm.setName(currentProjectDTO.getOwnerName());
             * perm.setFirstName(currentProjectDTO.getOwnerFirstName());
             * perm.setEmail(currentProjectDTO.getOwnerEmail());
             * 
             * // Sets the value to the field. comboBox.setValue(perm);
             * 
             * // Listens to the selection changes.
             * comboBox.addSelectionChangedListener(new
             * SelectionChangedListener<UserPermissionDTO>() {
             * 
             * @Override public void
             * selectionChanged(SelectionChangedEvent<UserPermissionDTO> se) {
             * 
             * String value = null; final boolean isValueOn;
             * 
             * // Gets the selected choice. final UserPermissionDTO choice =
             * se.getSelectedItem();
             * 
             * // Checks if the choice isn't the default empty // choice.
             * isValueOn = choice != null && !"".equals(choice.getEmail());
             * 
             * if (choice != null) { value = String.valueOf(choice.getEmail());
             * }
             * 
             * if (value != null) { // Fires value change event.
             * handlerManager.fireEvent(new
             * ValueEvent(DefaultFlexibleElementDTO.this, value)); }
             * 
             * // Required element ? if (getValidates()) {
             * handlerManager.fireEvent(new RequiredValueEvent(isValueOn)); } }
             * }); }
             * 
             * component = comboBox;
             */
        }
            break;
        default:
            throw new IllegalArgumentException("[getComponent] The type '" + getType()
                    + "' for the default flexible element doen't exist.");
        }

        // Sets the enable state.
        component.setEnabled(enabled);

        return component;
    }

    @Override
    public boolean isCorrectRequiredValue(ValueResult result) {
        // These elements don't have any value.
        return true;
    }

    /**
     * Method in charge of firing value events.
     * 
     * @param value
     *            The raw value which is serialized to the server and saved to
     *            the data layer.
     * @param isValueOn
     *            If the value is correct.
     */
    private void fireEvents(String value, boolean isValueOn) {

        handlerManager.fireEvent(new ValueEvent(this, value));

        // Required element ?
        if (getValidates()) {
            handlerManager.fireEvent(new RequiredValueEvent(isValueOn));
        }
    }

    /**
     * Create a text field to represent a default flexible element.
     * 
     * @param length
     *            The max length of the field.
     * @param allowBlank
     *            If the field allow blank value.
     * @return The text field.
     */
    private TextField<String> createStringField(final int length, final boolean allowBlank) {

        final TextField<String> textField = new TextField<String>();
        textField.setAllowBlank(allowBlank);

        // Sets the max length.
        textField.setMaxLength(length);

        // Adds the listeners.
        textField.addListener(Events.OnKeyUp, new Listener<BaseEvent>() {

            @Override
            public void handleEvent(BaseEvent be) {

                String rawValue = textField.getValue();

                if (rawValue == null) {
                    rawValue = "";
                }

                // The value is valid if it contains at least one non-blank
                // character.
                final boolean isValueOn = !rawValue.trim().equals("") && !(rawValue.length() > length);

                if (!(!allowBlank && !isValueOn)) {
                    fireEvents(rawValue, isValueOn);
                }
            }
        });

        return textField;
    }

    /**
     * Create a date field to represent a default flexible element.
     * 
     * @param allowBlank
     *            If the field allow blank value.
     * @return The date field.
     */
    private DateField createDateField(final boolean allowBlank) {

        final DateTimeFormat DATE_FORMAT = DateTimeFormat.getFormat(I18N.CONSTANTS.flexibleElementDateFormat());

        // Creates a date field which manages date picker selections and
        // manual selections.
        final DateField dateField = new DateField();
        dateField.getPropertyEditor().setFormat(DATE_FORMAT);
        dateField.setEditable(allowBlank);
        dateField.setAllowBlank(allowBlank);
        preferredWidth = 120;

        // Adds the listeners.

        dateField.getDatePicker().addListener(Events.Select, new Listener<DatePickerEvent>() {
            @Override
            public void handleEvent(DatePickerEvent be) {

                // The date is saved as a timestamp.
                final String rawValue = String.valueOf(be.getDate().getTime());
                // The date picker always returns a valid date.
                final boolean isValueOn = true;

                fireEvents(rawValue, isValueOn);
            }
        });

        dateField.addListener(Events.OnKeyUp, new Listener<BaseEvent>() {

            @Override
            public void handleEvent(BaseEvent be) {

                final Date date = dateField.getValue();

                // The date is invalid, fires only a required event to
                // invalidate some previously valid date.
                if (date == null) {

                    // Required element ?
                    if (getValidates()) {
                        handlerManager.fireEvent(new RequiredValueEvent(false));
                    }

                    if (allowBlank) {
                        fireEvents("", false);
                    }

                    return;
                }

                // The date is saved as a timestamp.
                final String rawValue = String.valueOf(date.getTime());
                // The date is valid here.
                final boolean isValueOn = true;

                if (!(!allowBlank && !isValueOn)) {
                    fireEvents(rawValue, isValueOn);
                }
            }
        });

        return dateField;
    }

    /**
     * Create a number field to represent a default flexible element.
     * 
     * @param allowBlank
     *            If the field allow blank value.
     * @return The number field.
     */
    private NumberField createNumberField(final boolean allowBlank) {

        final NumberField numberField = new NumberField();
        numberField.setAllowDecimals(true);
        numberField.setAllowNegative(false);
        numberField.setAllowBlank(allowBlank);
        preferredWidth = 120;

        // Decimal format
        final NumberFormat format = NumberFormat.getDecimalFormat();
        numberField.setFormat(format);

        // Sets the min value.
        final Number minValue = 0.0;
        numberField.setMinValue(minValue);

        return numberField;
    }
}
