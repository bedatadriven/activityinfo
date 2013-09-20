package org.activityinfo.client.page.config;

/*
 * #%L
 * ActivityInfo Server
 * %%
 * Copyright (C) 2009 - 2013 UNICEF
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import org.activityinfo.client.dispatch.AsyncMonitor;
import org.activityinfo.client.i18n.I18N;
import org.activityinfo.client.page.config.LockedPeriodsPresenter.AddLockedPeriodView;
import org.activityinfo.shared.dto.ActivityDTO;
import org.activityinfo.shared.dto.LockedPeriodDTO;
import org.activityinfo.shared.dto.ProjectDTO;
import org.activityinfo.shared.dto.UserDatabaseDTO;

import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.FieldEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.HorizontalPanel;
import com.extjs.gxt.ui.client.widget.form.CheckBox;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.ComboBox.TriggerAction;
import com.extjs.gxt.ui.client.widget.form.DateField;
import com.extjs.gxt.ui.client.widget.form.FieldSet;
import com.extjs.gxt.ui.client.widget.form.FormPanel;
import com.extjs.gxt.ui.client.widget.form.LabelField;
import com.extjs.gxt.ui.client.widget.form.Radio;
import com.extjs.gxt.ui.client.widget.form.RadioGroup;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.ui.Widget;

public class AddLockedPeriodDialog extends FormPanel implements
    AddLockedPeriodView {

    private FieldSet fieldsetContainer;
    private RadioGroup radiogroupContainer;
    private Radio radioDatabase;
    private Radio radioActivity;
    private Radio radioProject;
    private LabelField labelDatabase;
    private LabelField labelDatabaseName;
    private LabelField labelProject;
    private LabelField labelActivity;

    private HorizontalPanel panelDatabase;
    private HorizontalPanel panelActivity;
    private HorizontalPanel panelProject;

    private UserDatabaseDTO userDatabase;

    private ComboBox<ProjectDTO> comboboxProjects;
    private ListStore<ProjectDTO> storeProjects;
    private ComboBox<ActivityDTO> comboboxActivities;
    private ListStore<ActivityDTO> storeActivities;

    private TextField<String> textfieldName;
    private DateField datefieldFromDate;
    private DateField datefieldToDate;
    private CheckBox checkboxEnabled;

    private EventBus eventBus = new SimpleEventBus();

    public AddLockedPeriodDialog() {
        super();

        initializeComponent();

        setStartState();
    }

    @Override
    public void setUserDatabase(UserDatabaseDTO userDatabase) {
        this.userDatabase = userDatabase;

        labelDatabaseName.setText(userDatabase.getName());

        storeProjects.removeAll();
        storeProjects.add(userDatabase.getProjects());

        storeActivities.removeAll();
        storeActivities.add(userDatabase.getActivities());
    }

    private void initializeComponent() {
        setWidth(400);
        setHeight(280);

        setHeaderVisible(false);

        fieldsetContainer = new FieldSet();
        fieldsetContainer.setHeading(I18N.CONSTANTS.type());

        comboboxProjects = new ComboBox<ProjectDTO>();
        storeProjects = new ListStore<ProjectDTO>();
        comboboxProjects.setStore(storeProjects);
        comboboxProjects.setDisplayField("name");
        comboboxProjects.setForceSelection(true);
        comboboxProjects.setTriggerAction(TriggerAction.ALL);
        comboboxProjects.setEditable(false);

        comboboxActivities = new ComboBox<ActivityDTO>();
        storeActivities = new ListStore<ActivityDTO>();
        comboboxActivities.setStore(storeActivities);
        comboboxActivities.setDisplayField("name");
        comboboxActivities.setForceSelection(true);
        comboboxActivities.setTriggerAction(TriggerAction.ALL);
        comboboxActivities.setEditable(false);

        radiogroupContainer = new RadioGroup();
        radiogroupContainer.setFieldLabel(I18N.CONSTANTS.type());

        labelDatabase = new LabelField(I18N.CONSTANTS.database());
        labelDatabase.setWidth(100);

        labelDatabaseName = new LabelField();

        radioDatabase = new Radio();
        radioDatabase.setFieldLabel(I18N.CONSTANTS.database());
        radioDatabase.addListener(Events.Change, new Listener<FieldEvent>() {
            @Override
            public void handleEvent(FieldEvent be) {

            }
        });
        radiogroupContainer.add(radioDatabase);
        panelDatabase = new HorizontalPanel();
        panelDatabase.add(labelDatabase);
        panelDatabase.add(radioDatabase);
        panelDatabase.add(labelDatabaseName);
        fieldsetContainer.add(panelDatabase);

        radioActivity = new Radio();
        radioActivity.setFieldLabel(I18N.CONSTANTS.activity());

        labelActivity = new LabelField(I18N.CONSTANTS.activity());
        labelActivity.setWidth(100);

        panelActivity = new HorizontalPanel();
        panelActivity.add(labelActivity);
        panelActivity.add(radioActivity);
        panelActivity.add(comboboxActivities);
        fieldsetContainer.add(panelActivity);
        radiogroupContainer.add(radioActivity);

        radioProject = new Radio();
        radioProject.setFieldLabel(I18N.CONSTANTS.project());
        radioProject.addListener(Events.Change, new Listener<FieldEvent>() {
            @Override
            public void handleEvent(FieldEvent be) {
                comboboxProjects.setEnabled(radioProject.getValue());
            }
        });

        labelProject = new LabelField(I18N.CONSTANTS.project());
        labelProject.setWidth(100);

        panelProject = new HorizontalPanel();
        panelProject.add(labelProject);
        panelProject.add(radioProject);
        panelProject.add(comboboxProjects);
        fieldsetContainer.add(panelProject);
        radiogroupContainer.add(radioProject);

        add(fieldsetContainer);

        textfieldName = new TextField<String>();
        textfieldName.setFieldLabel(I18N.CONSTANTS.name());
        textfieldName.setAllowBlank(false);
        add(textfieldName);

        checkboxEnabled = new CheckBox();
        checkboxEnabled.setFieldLabel(I18N.CONSTANTS.enabledColumn());
        add(checkboxEnabled);

        datefieldFromDate = new DateField();
        datefieldFromDate.setFieldLabel(I18N.CONSTANTS.fromDate());
        datefieldFromDate.setAllowBlank(false);
        add(datefieldFromDate);

        datefieldToDate = new DateField();
        datefieldToDate.setFieldLabel(I18N.CONSTANTS.toDate());
        datefieldFromDate.setAllowBlank(false);
        add(datefieldToDate);

        radiogroupContainer.addListener(Events.Change,
            new Listener<FieldEvent>() {

                @Override
                public void handleEvent(FieldEvent be) {
                    comboboxActivities.setAllowBlank(!radioActivity.getValue());
                    comboboxProjects.setAllowBlank(!radioProject.getValue());
                    comboboxActivities.clearInvalid();
                    comboboxProjects.clearInvalid();
                }
            });
    }

    @Override
    public void initialize() {

    }

    @Override
    public AsyncMonitor getLoadingMonitor() {
        return null;
    }

    @Override
    public void setValue(LockedPeriodDTO value) {
        if (value != null) {
            textfieldName.setValue(value.getName());
            checkboxEnabled.setValue(value.isEnabled());
            datefieldFromDate.setValue(value.getFromDate()
                .atMidnightInMyTimezone());
            datefieldToDate
                .setValue(value.getToDate().atMidnightInMyTimezone());
        }
    }

    @Override
    public LockedPeriodDTO getValue() {
        LockedPeriodDTO newLockedPeriod = new LockedPeriodDTO();
        newLockedPeriod.setName(textfieldName.getValue());
        newLockedPeriod.setEnabled(checkboxEnabled.getValue());
        newLockedPeriod.setFromDate(datefieldFromDate.getValue());
        newLockedPeriod.setToDate(datefieldToDate.getValue());
        if (radioActivity.getValue() && comboboxActivities.getValue() != null) {
            newLockedPeriod.setParent(comboboxActivities.getValue());
        }
        if (radioProject.getValue() && comboboxProjects.getValue() != null) {
            newLockedPeriod.setParent(comboboxProjects.getValue());
        }
        if (radioDatabase.getValue()) {
            newLockedPeriod.setParent(userDatabase);
        }

        return newLockedPeriod;
    }

    @Override
    public Widget asWidget() {
        return this;
    }

    @Override
    public HandlerRegistration addCreateHandler(
        org.activityinfo.client.mvp.CanCreate.CreateHandler handler) {
        return eventBus.addHandler(CreateEvent.TYPE, handler);
    }

    @Override
    public void create(LockedPeriodDTO item) {
    }

    @Override
    public void setCreateEnabled(boolean createEnabled) {
    }

    @Override
    public HandlerRegistration addCancelCreateHandler(
        org.activityinfo.client.mvp.CanCreate.CancelCreateHandler handler) {
        return eventBus.addHandler(CancelCreateEvent.TYPE, handler);
    }

    @Override
    public HandlerRegistration addStartCreateHandler(
        org.activityinfo.client.mvp.CanCreate.StartCreateHandler handler) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void startCreate() {
        this.show();
    }

    private void setStartState() {
        textfieldName.setValue(null);
        datefieldFromDate.setValue(null);
        datefieldToDate.setValue(null);
        checkboxEnabled.setValue(true);
        radioActivity.setValue(false);
        radioProject.setValue(false);
        radioDatabase.setValue(true);
    }

    @Override
    public void cancelCreate() {
        setStartState();
        this.hide();
    }

    @Override
    public void update(LockedPeriodDTO item) {
        // TODO Auto-generated method stub

    }

    @Override
    public void cancelUpdate(LockedPeriodDTO item) {
        // TODO Auto-generated method stub

    }

    @Override
    public void cancelUpdateAll() {
        // TODO Auto-generated method stub

    }

    @Override
    public void startUpdate() {
        // TODO Auto-generated method stub

    }

    @Override
    public void setUpdateEnabled(boolean updateEnabled) {
        // TODO Auto-generated method stub

    }

    @Override
    public HandlerRegistration addUpdateHandler(
        org.activityinfo.client.mvp.CanUpdate.UpdateHandler handler) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public HandlerRegistration addCancelUpdateHandler(
        org.activityinfo.client.mvp.CanUpdate.CancelUpdateHandler handler) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public HandlerRegistration addRequestUpdateHandler(
        org.activityinfo.client.mvp.CanUpdate.RequestUpdateHandler handler) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public AsyncMonitor getCreatingMonitor() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public AsyncMonitor getUpdatingMonitor() {
        // TODO Auto-generated method stub
        return null;
    }
}
