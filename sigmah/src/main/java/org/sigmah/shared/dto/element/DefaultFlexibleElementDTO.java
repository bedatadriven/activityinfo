package org.sigmah.shared.dto.element;

import java.util.Date;
import java.util.List;

import org.sigmah.client.i18n.I18N;
import org.sigmah.client.util.HistoryTokenText;
import org.sigmah.client.util.NumberUtils;
import org.sigmah.shared.command.GetCountries;
import org.sigmah.shared.command.result.CountryResult;
import org.sigmah.shared.command.result.ValueResult;
import org.sigmah.shared.command.result.ValueResultUtils;
import org.sigmah.shared.domain.element.DefaultFlexibleElementType;
import org.sigmah.shared.dto.CountryDTO;
import org.sigmah.shared.dto.element.handler.RequiredValueEvent;
import org.sigmah.shared.dto.element.handler.ValueEvent;
import org.sigmah.shared.dto.history.HistoryTokenListDTO;

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
import com.extjs.gxt.ui.client.widget.form.Field;
import com.extjs.gxt.ui.client.widget.form.FieldSet;
import com.extjs.gxt.ui.client.widget.form.LabelField;
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

    private transient DefaultFlexibleElementContainer container;

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

    @Override
    protected Component getComponent(ValueResult valueResult, boolean enabled) {

        if (currentContainerDTO instanceof DefaultFlexibleElementContainer) {
            container = (DefaultFlexibleElementContainer) currentContainerDTO;
        } else {
            throw new IllegalArgumentException(
                    "The flexible elements container isn't an instance of DefaultFlexibleElementContainer. The default flexible element connot be instanciated.");
        }

        final DateTimeFormat DATE_FORMAT = DateTimeFormat.getFormat(I18N.CONSTANTS.flexibleElementDateFormat());
        final Component component;

        switch (getType()) {
        // Project code.
        case CODE: {

            final Field<?> field;

            // Builds the field and sets its value.
            if (enabled) {
                final TextField<String> textField = createStringField(16, false);
                textField.setValue(container.getName());
                field = textField;

            } else {
                final LabelField labelField = createLabelField();
                labelField.setValue(container.getName());
                field = labelField;
            }

            // Sets the field label.
            setLabel(I18N.CONSTANTS.projectName());
            field.setFieldLabel(getLabel());

            component = field;
        }
            break;
        // Project title.
        case TITLE: {

            final Field<?> field;

            // Builds the field and sets its value.
            if (enabled) {
                final TextField<String> textField = createStringField(50, false);
                textField.setValue(container.getFullName());
                field = textField;

            } else {
                final LabelField labelField = createLabelField();
                labelField.setValue(container.getFullName());
                field = labelField;
            }

            // Sets the field label.
            setLabel(I18N.CONSTANTS.projectFullName());
            field.setFieldLabel(getLabel());

            component = field;
        }
            break;

        case START_DATE: {

            final Field<?> field;
            final Date sd = container.getStartDate();

            // Builds the field and sets its value.
            if (enabled) {
                final DateField dateField = createDateField(false);
                dateField.setValue(sd);
                field = dateField;

            } else {

                final LabelField labelField = createLabelField();
                if (sd != null) {
                    labelField.setValue(DATE_FORMAT.format(sd));
                }
                field = labelField;
            }

            // Sets the field label.
            setLabel(I18N.CONSTANTS.projectStartDate());
            field.setFieldLabel(getLabel());

            component = field;
        }
            break;
        case END_DATE: {

            final Field<?> field;
            final Date ed = container.getEndDate();

            // Builds the field and sets its value.
            if (enabled) {
                final DateField dateField = createDateField(true);
                dateField.setValue(ed);
                field = dateField;

            } else {

                final LabelField labelField = createLabelField();
                if (ed != null) {
                    labelField.setValue(DATE_FORMAT.format(ed));
                }
                field = labelField;
            }

            // Sets the field label.
            setLabel(I18N.CONSTANTS.projectEndDate());
            field.setFieldLabel(getLabel());

            component = field;
        }
            break;
        case BUDGET: {

            final Field<?> plannedBudgetField;
            final Field<?> spendBudgetField;
            final Field<?> receivedBudgetField;

            final Double pb = container.getPlannedBudget();
            final Double sb = container.getSpendBudget();
            final Double rb = container.getReceivedBudget();

            // Spent ratio.
            final Label ratioLabel = new Label();
            ratioLabel.addStyleName("project-label-10");
            ratioLabel.addStyleName("flexibility-label");

            if (enabled) {

                // Planned budget.
                final NumberField plannedBudgetNumberField = createNumberField(false);
                plannedBudgetNumberField.setFieldLabel(I18N.CONSTANTS.projectPlannedBudget());

                // Spend budget.
                final NumberField spendBudgetNumberField = createNumberField(false);
                spendBudgetNumberField.setFieldLabel(I18N.CONSTANTS.projectSpendBudget());

                // Received budget.
                final NumberField receivedBudgetNumberField = createNumberField(false);
                receivedBudgetNumberField.setFieldLabel(I18N.CONSTANTS.projectReceivedBudget());

                // Listener.
                final Listener<BaseEvent> listener = new Listener<BaseEvent>() {

                    final double minValue = 0.0;

                    @Override
                    public void handleEvent(BaseEvent be) {

                        // Retrieves values.
                        final Number plannedBudget = plannedBudgetNumberField.getValue();
                        final Double plannedBudgetAsDouble = plannedBudget.doubleValue();

                        final Number spendBudget = spendBudgetNumberField.getValue();
                        final Double spendBudgetAsDouble = spendBudget.doubleValue();

                        final Number receivedBudget = receivedBudgetNumberField.getValue();
                        final Double receivedBudgetAsDouble = receivedBudget.doubleValue();

                        // Checks the numbers intervals.
                        final boolean isValueOn = plannedBudgetAsDouble >= minValue && spendBudgetAsDouble >= minValue
                                && receivedBudgetAsDouble >= minValue;

                        // The numbers are saved as strings.
                        final String plannedBudgetRawValue = String.valueOf(plannedBudgetAsDouble);
                        final String spendBudgetRawValue = String.valueOf(spendBudgetAsDouble);
                        final String receivedBudgetRawValue = String.valueOf(receivedBudgetAsDouble);
                        final String rawValue = ValueResultUtils.mergeElements(plannedBudgetRawValue,
                                spendBudgetRawValue, receivedBudgetRawValue);

                        ratioLabel.setText(I18N.CONSTANTS.flexibleElementBudgetDistributionRatio() + ": "
                                + NumberUtils.ratioAsString(spendBudgetAsDouble, plannedBudgetAsDouble));

                        fireEvents(rawValue, isValueOn);
                    }
                };

                plannedBudgetNumberField.addListener(Events.Change, listener);
                spendBudgetNumberField.addListener(Events.Change, listener);
                receivedBudgetNumberField.addListener(Events.Change, listener);

                // Sets the value to the fields.
                plannedBudgetNumberField.setValue(pb);
                spendBudgetNumberField.setValue(sb);
                receivedBudgetNumberField.setValue(rb);

                plannedBudgetField = plannedBudgetNumberField;
                spendBudgetField = spendBudgetNumberField;
                receivedBudgetField = receivedBudgetNumberField;

            } else {

                final LabelField plannedBudgetLabelField = createLabelField();
                plannedBudgetLabelField.setFieldLabel(I18N.CONSTANTS.projectPlannedBudget());

                final LabelField spendBudgetLabelField = createLabelField();
                spendBudgetLabelField.setFieldLabel(I18N.CONSTANTS.projectSpendBudget());

                final LabelField receivedBudgetLabelField = createLabelField();
                receivedBudgetLabelField.setFieldLabel(I18N.CONSTANTS.projectReceivedBudget());

                // Sets the value to the fields.
                plannedBudgetLabelField.setValue(pb);
                spendBudgetLabelField.setValue(sb);
                receivedBudgetLabelField.setValue(rb);

                plannedBudgetField = plannedBudgetLabelField;
                spendBudgetField = spendBudgetLabelField;
                receivedBudgetField = receivedBudgetLabelField;
            }

            ratioLabel.setText(I18N.CONSTANTS.flexibleElementBudgetDistributionRatio() + ": "
                    + NumberUtils.ratioAsString(sb, pb));

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

            final Field<?> field;
            final CountryDTO c = container.getCountry();

            if (enabled) {
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
                comboBox.setAllowBlank(true);

                // Retrieves the county list.
                if (countriesStore.getCount() == 0) {

                    if (countries != null) {

                        countries.getCountries(new AsyncCallback<List<CountryDTO>>() {

                            @Override
                            public void onFailure(Throwable e) {
                                Log.error("[getComponent] Error while getting countries list.", e);
                            }

                            @Override
                            public void onSuccess(List<CountryDTO> result) {

                                // Fills the store.
                                countriesStore.add(result);

                                // Sets the value to the field.
                                if (c != null) {
                                    comboBox.setValue(c);
                                }

                                // Listens to the selection changes.
                                comboBox.addSelectionChangedListener(new SelectionChangedListener<CountryDTO>() {

                                    @Override
                                    public void selectionChanged(SelectionChangedEvent<CountryDTO> se) {

                                        String value = null;
                                        final boolean isValueOn;

                                        // Gets the selected choice.
                                        final CountryDTO choice = se.getSelectedItem();

                                        // Checks if the choice isn't the
                                        // default empty choice.
                                        isValueOn = choice != null && choice.getId() != -1;

                                        if (choice != null) {
                                            value = String.valueOf(choice.getId());
                                        }

                                        if (value != null) {
                                            // Fires value change event.
                                            handlerManager.fireEvent(new ValueEvent(DefaultFlexibleElementDTO.this,
                                                    value));
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
                                if (c != null) {
                                    comboBox.setValue(c);
                                }

                                // Listens to the selection changes.
                                comboBox.addSelectionChangedListener(new SelectionChangedListener<CountryDTO>() {

                                    @Override
                                    public void selectionChanged(SelectionChangedEvent<CountryDTO> se) {

                                        String value = null;
                                        final boolean isValueOn;

                                        // Gets the selected choice.
                                        final CountryDTO choice = se.getSelectedItem();

                                        // Checks if the choice isn't the
                                        // default empty choice.
                                        isValueOn = choice != null && choice.getId() != -1;

                                        if (choice != null) {
                                            value = String.valueOf(choice.getId());
                                        }

                                        if (value != null) {
                                            // Fires value change event.
                                            handlerManager.fireEvent(new ValueEvent(DefaultFlexibleElementDTO.this,
                                                    value));
                                        }

                                        // Required element ?
                                        if (getValidates()) {
                                            handlerManager.fireEvent(new RequiredValueEvent(isValueOn));
                                        }
                                    }
                                });
                            }
                        });
                    }
                } else {

                    // Sets the value to the field.
                    if (c != null) {
                        comboBox.setValue(c);
                    }

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

                field = comboBox;
            } else {

                final LabelField labelField = createLabelField();

                if (c == null) {
                    labelField.setValue("-");
                } else {
                    labelField.setValue(c.getName());
                }

                field = labelField;
            }

            // Sets the field label.
            setLabel(I18N.CONSTANTS.projectCountry());
            field.setFieldLabel(getLabel());

            component = field;
        }
            break;
        case OWNER: {

            final LabelField labelField = createLabelField();

            // Sets the field label.
            setLabel(I18N.CONSTANTS.projectManager());
            labelField.setFieldLabel(getLabel());

            // Sets the value to the field.
            labelField.setValue(container.getOwnerFirstName() != null ? container.getOwnerFirstName() + " "
                    + container.getOwnerName() : container.getOwnerName());

            component = labelField;
        }
            break;
        default:
            throw new IllegalArgumentException("[getComponent] The type '" + getType()
                    + "' for the default flexible element doen't exist.");
        }

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

    /**
     * Create a label field to represent a default flexible element.
     * 
     * @return The label field.
     */
    private LabelField createLabelField() {

        final LabelField labelField = new LabelField();
        labelField.setLabelSeparator(":");

        return labelField;
    }

    @Override
    public Object renderHistoryToken(HistoryTokenListDTO token) {

        ensureHistorable();

        final String value = token.getTokens().get(0).getValue();

        if (getType() != null) {
            switch (getType()) {
            case COUNTRY:
                if (countries != null) {
                    final CountryDTO c = countries.getCountry(Integer.valueOf(value));
                    if (c != null) {
                        return new HistoryTokenText(c.getName());
                    } else {
                        return new HistoryTokenText("#" + value);
                    }
                } else {
                    return new HistoryTokenText("#" + value);
                }
            case BUDGET:

                if (Log.isDebugEnabled()) {
                    Log.debug("[renderHistoryToken] Case BUDGET ; value to split '" + value + "'.");
                }

                final List<String> budgets = ValueResultUtils.splitElements(value);

                if (Log.isDebugEnabled()) {
                    Log.debug("[renderHistoryToken] Case BUDGET ; splitted values (" + budgets.size() + ") '" + budgets
                            + "'.");
                }

                if (Log.isDebugEnabled()) {
                    Log.debug("[renderHistoryToken] Case BUDGET ; planned budget '" + budgets.get(0) + "'.");
                }

                final double plannedBudget = Double.parseDouble(budgets.get(0));

                if (Log.isDebugEnabled()) {
                    Log.debug("[renderHistoryToken] Case BUDGET ; spent budget '" + budgets.get(1) + "'.");
                }

                final double spendBudget = Double.parseDouble(budgets.get(1));

                if (Log.isDebugEnabled()) {
                    Log.debug("[renderHistoryToken] Case BUDGET ; received budget '" + budgets.get(2) + "'.");
                }

                final double receivedBudget = Double.parseDouble(budgets.get(2));

                return new HistoryTokenText(I18N.CONSTANTS.projectPlannedBudget() + ": " + plannedBudget,
                        I18N.CONSTANTS.projectReceivedBudget() + ": " + receivedBudget,
                        I18N.CONSTANTS.projectSpendBudget() + ": " + spendBudget);
            case START_DATE:
            case END_DATE:
                final DateTimeFormat format = DateTimeFormat.getFormat(I18N.CONSTANTS.flexibleElementDateFormat());
                final long time = Long.valueOf(value);
                return new HistoryTokenText(format.format(new Date(time)));
            default:
                return super.renderHistoryToken(token);
            }
        } else {
            return super.renderHistoryToken(token);
        }
    }
}
