/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */
package org.sigmah.client.page.project.calendar;

import com.allen_sauer.gwt.log.client.Log;
import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.MessageBox;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.ComboBox.TriggerAction;
import com.extjs.gxt.ui.client.widget.form.DateField;
import com.extjs.gxt.ui.client.widget.form.Field;
import com.extjs.gxt.ui.client.widget.form.TextArea;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.form.Time;
import com.extjs.gxt.ui.client.widget.form.TimeField;
import com.extjs.gxt.ui.client.widget.grid.CheckBoxSelectionModel;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.FitData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;
import com.extjs.gxt.ui.client.widget.toolbar.SeparatorToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.i18n.client.constants.DateTimeConstants;
import com.google.gwt.user.client.rpc.AsyncCallback;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.dispatch.remote.Authentication;
import org.sigmah.client.i18n.I18N;
import org.sigmah.client.page.project.calendar.ProjectCalendarPresenter.CalendarWrapper;
import org.sigmah.client.ui.CalendarWidget;
import org.sigmah.shared.command.CreateEntity;
import org.sigmah.shared.command.Delete;
import org.sigmah.shared.command.UpdateEntity;
import org.sigmah.shared.command.result.CreateResult;
import org.sigmah.shared.command.result.VoidResult;
import org.sigmah.shared.domain.calendar.Calendar;
import org.sigmah.shared.domain.calendar.Event;
import org.sigmah.shared.domain.profile.GlobalPermissionEnum;
import org.sigmah.shared.dto.profile.ProfileUtils;

/**
 * 
 * @author Raphaël Calabro (rcalabro@ideia.fr)
 */
@SuppressWarnings({ "deprecation", "unchecked" })
public class ProjectCalendarView extends LayoutContainer {

    private Button addEventButton;
    private Dialog addPersonalEventDialog;

    public ProjectCalendarView(final CalendarWidget calendar, final ListStore<CalendarWrapper> calendarStore,
            final CheckBoxSelectionModel<CalendarWrapper> selectionModel, final Dispatcher dispatcher,
            final Authentication authentication) {

        final BorderLayout borderLayout = new BorderLayout();
        borderLayout.setContainerStyle("x-border-layout-ct main-background");
        setLayout(borderLayout);

        final ContentPanel calendarList = new ContentPanel(new FitLayout());
        calendarList.setHeading(I18N.CONSTANTS.projectTabCalendar());
        final BorderLayoutData calendarListData = new BorderLayoutData(LayoutRegion.WEST, 250);
        calendarListData.setCollapsible(true);
        add(calendarList, calendarListData);

        // Calendar list
        final ColumnConfig countryName = new ColumnConfig("name", I18N.CONSTANTS.name(), 200);
        final ColumnModel countryColumnModel = new ColumnModel(Arrays.asList(selectionModel.getColumn(), countryName));

        final Grid<CalendarWrapper> calendarGrid = new Grid<CalendarWrapper>(calendarStore, countryColumnModel);
        calendarGrid.setAutoExpandColumn("name");
        calendarGrid.setSelectionModel(selectionModel);
        calendarGrid.addPlugin(selectionModel);

        calendarGrid.getView().setForceFit(true);

        calendarList.add(calendarGrid);

        // Calendar
        selectionModel.addSelectionChangedListener(new SelectionChangedListener<CalendarWrapper>() {

            @Override
            public void selectionChanged(SelectionChangedEvent<CalendarWrapper> se) {
                final List<CalendarWrapper> wrappers = se.getSelection();
                final ArrayList<Calendar> calendars = new ArrayList<Calendar>();
                for (final CalendarWrapper wrapper : wrappers) {
                    calendars.add(wrapper.getCalendar());
                }
                calendar.setCalendars(calendars);
            }
        });

        // Defining the first day of the week
        // LocaleInfo uses 1 for Sunday and 2 for Monday. Substracting 1 since
        // Date starts with 0 for Sunday.
        final DateTimeConstants constants = LocaleInfo.getCurrentLocale().getDateTimeConstants();
        calendar.setFirstDayOfWeek(Integer.parseInt(constants.firstDayOfTheWeek()) - 1);

        final ContentPanel calendarView = new ContentPanel(new FitLayout());
        // Retrieving the current calendar header
        calendarView.setHeading(calendar.getHeading());
        // Listening for further calendar header changes
        calendar.setListener(new CalendarWidget.CalendarListener() {

            @Override
            public void afterRefresh() {
                calendarView.setHeading(calendar.getHeading());
            }
        });

        // Toolbar
        final ToolBar toolbar = new ToolBar();

        // Today button - center the calendar on the current day
        final Button todayButton = new Button(I18N.CONSTANTS.today());
        todayButton.addListener(Events.Select, new Listener<BaseEvent>() {

            @Override
            public void handleEvent(BaseEvent be) {
                calendar.today();
            }
        });
        toolbar.add(todayButton);

        toolbar.add(new SeparatorToolItem());

        // Week button - changes the calendar to display weeks
        final Button weekButton = new Button(I18N.CONSTANTS.week());
        weekButton.addListener(Events.Select, new Listener<BaseEvent>() {

            @Override
            public void handleEvent(BaseEvent be) {
                calendar.setDisplayMode(CalendarWidget.DisplayMode.WEEK);
            }
        });
        toolbar.add(weekButton);

        // Week button - changes the calendar to display monthes
        final Button monthButton = new Button(I18N.CONSTANTS.month());
        monthButton.addListener(Events.Select, new Listener<BaseEvent>() {

            @Override
            public void handleEvent(BaseEvent be) {
                calendar.setDisplayMode(CalendarWidget.DisplayMode.MONTH);
            }
        });
        toolbar.add(monthButton);

        toolbar.add(new SeparatorToolItem());

        // Previous button - move back from one unit of time (week / month)
        final Button previousButton = new Button(I18N.CONSTANTS.previous());
        previousButton.addListener(Events.Select, new Listener<BaseEvent>() {

            @Override
            public void handleEvent(BaseEvent be) {
                calendar.previous();
            }
        });
        toolbar.add(previousButton);

        // Next button - move forward from one unit of time (week / month)
        final Button nextButton = new Button(I18N.CONSTANTS.next());
        nextButton.addListener(Events.Select, new Listener<BaseEvent>() {

            @Override
            public void handleEvent(BaseEvent be) {
                calendar.next();
            }
        });
        toolbar.add(nextButton);

        toolbar.add(new SeparatorToolItem());

        addEventButton = new Button(I18N.CONSTANTS.calendarAddEvent());
        addEventButton.addListener(Events.Select, new Listener<BaseEvent>() {

            @Override
            public void handleEvent(BaseEvent be) {
                // Displays an "Add Event" popup
                getEditPersonalEventDialog(null, calendarStore, calendar, dispatcher).show();
            }
        });

        if (ProfileUtils.isGranted(authentication, GlobalPermissionEnum.EDIT_PROJECT)) {
            toolbar.add(addEventButton);
        }

        calendarView.setTopComponent(toolbar);

        final FitData fitData = new FitData(16);
        calendarView.addStyleName("panel-background");
        calendarView.add(calendar, fitData);

        // Configuring calendar delegate
        calendar.setDelegate(new CalendarWidget.Delegate() {

            @Override
            public void edit(Event event, CalendarWidget calendarWidget) {
                getEditPersonalEventDialog(event, calendarStore, calendar, dispatcher).show();
            }

            @Override
            public void delete(final Event event, final CalendarWidget calendarWidget) {
                final Delete delete = new Delete("calendar.PersonalEvent", (Integer) event.getIdentifier());
                dispatcher.execute(delete, null, new AsyncCallback<VoidResult>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        MessageBox.alert(I18N.CONSTANTS.error(), I18N.CONSTANTS.calendarDeleteEventError(), null);
                    }

                    @Override
                    public void onSuccess(VoidResult result) {
                        final List<Event> oldEventList = event
                                .getParent()
                                .getEvents()
                                .get(new Date(event.getDtstart().getYear(), event.getDtstart().getMonth(), event
                                        .getDtstart().getDate()));
                        oldEventList.remove(event);

                        calendarWidget.refresh();
                    }
                });
            }
        });

        final BorderLayoutData calendarViewData = new BorderLayoutData(LayoutRegion.CENTER);
        calendarViewData.setMargins(new Margins(0, 0, 0, 8));

        add(calendarView, calendarViewData);
    }

    public Button getAddEventButton() {
        return addEventButton;
    }

    public Dialog getEditPersonalEventDialog(final Event event, final ListStore<CalendarWrapper> calendarStore,
            final CalendarWidget calendarWidget, final Dispatcher dispatcher) {
        if (addPersonalEventDialog == null) {
            final Dialog dialog = new Dialog();
            dialog.setButtons(Dialog.OKCANCEL);
            dialog.setHeading(I18N.CONSTANTS.calendarAddEvent());
            dialog.setModal(true);

            dialog.setResizable(false);
            dialog.setWidth("340px");

            // Dialog content
            final FormLayout formLayout = new FormLayout();
            dialog.setLayout(formLayout);

            // Calendar list
            final ListStore<CalendarWrapper> calendarBoxStore = new ListStore<CalendarWrapper>();
            final ComboBox<CalendarWrapper> calendarBox = new ComboBox<CalendarWrapper>();
            calendarBox.setFieldLabel(I18N.CONSTANTS.projectTabCalendar());
            calendarBox.setStore(calendarBoxStore);
            calendarBox.setAllowBlank(false);
            calendarBox.setDisplayField("name");
            calendarBox.setName("calendarId");
            calendarBox.setTriggerAction(TriggerAction.ALL);
            calendarBox.setEmptyText(I18N.CONSTANTS.calendarEmptyChoice());
            dialog.add(calendarBox);

            // Event summary
            final TextField<String> eventSummaryField = new TextField<String>();
            eventSummaryField.setFieldLabel(I18N.CONSTANTS.calendarEventObject());
            eventSummaryField.setAllowBlank(false);
            eventSummaryField.setName("summary");
            dialog.add(eventSummaryField);

            // Event date
            final DateField eventStartField = new DateField();
            eventStartField.setFieldLabel(I18N.CONSTANTS.calendarEventDate());
            eventStartField.setAllowBlank(false);
            eventStartField.setName("date");
            dialog.add(eventStartField);

            // Event start time
            final TimeField eventStartHourField = new TimeField();
            eventStartHourField.setFieldLabel(I18N.CONSTANTS.calendarEventStartHour());
            eventStartHourField.setName("startDate");
            dialog.add(eventStartHourField);

            // Event end time
            final TimeField eventEndHourField = new TimeField();
            eventEndHourField.setFieldLabel(I18N.CONSTANTS.calendarEventEndHour());
            eventEndHourField.setName("endDate");
            dialog.add(eventEndHourField);

            // Event description
            final TextArea descriptionField = new TextArea();
            descriptionField.setFieldLabel(I18N.CONSTANTS.calendarEventDescription());
            descriptionField.setName("description");
            dialog.add(descriptionField);

            // Cancel button - closes the dialog
            dialog.getButtonById(Dialog.CANCEL).addSelectionListener(new SelectionListener<ButtonEvent>() {

                @Override
                public void componentSelected(ButtonEvent ce) {
                    dialog.hide();
                }
            });

            addPersonalEventDialog = dialog;
        }

        // Cleaning previous entries or setting entries if edit mode
        if (event == null) {
            final int size = addPersonalEventDialog.getItemCount();
            for (int index = 0; index < size; index++) {
                final Field<?> field = (Field<?>) addPersonalEventDialog.getWidget(index);
                field.setValue(null);
                field.clearInvalid();
            }
        } else {
            boolean fullDayEvent = event.getDtend() != null
                    && (event.getDtstart().getDate() != event.getDtend().getDate()
                            || event.getDtstart().getMonth() != event.getDtend().getMonth() || event.getDtstart()
                            .getYear() != event.getDtend().getYear());

            ((ComboBox<CalendarWrapper>) addPersonalEventDialog.getWidget(0)).setValue(new CalendarWrapper(event
                    .getParent()));
            ((TextField<String>) addPersonalEventDialog.getWidget(1)).setValue(event.getSummary());
            ((DateField) addPersonalEventDialog.getWidget(2)).setValue(new Date(event.getDtstart().getYear(), event
                    .getDtstart().getMonth(), event.getDtstart().getDate()));
            if (!fullDayEvent) {
                ((TimeField) addPersonalEventDialog.getWidget(3)).setValue(((TimeField) addPersonalEventDialog
                        .getWidget(3)).findModel(event.getDtstart()));

                if (event.getDtend() != null) {
                    ((TimeField) addPersonalEventDialog.getWidget(4)).setValue(((TimeField) addPersonalEventDialog
                            .getWidget(4)).findModel(event.getDtend()));
                } else {
                    ((TimeField) addPersonalEventDialog.getWidget(4)).setValue(null);
                }
            } else {
                ((TimeField) addPersonalEventDialog.getWidget(3)).setValue(null);
                ((TimeField) addPersonalEventDialog.getWidget(4)).setValue(null);
            }
            ((TextArea) addPersonalEventDialog.getWidget(5)).setValue(event.getDescription());
        }

        // Defining the selectable calendars
        final ComboBox<CalendarWrapper> calendarBox = (ComboBox<CalendarWrapper>) addPersonalEventDialog.getWidget(0);
        final ListStore<CalendarWrapper> calendarBoxStore = calendarBox.getStore();
        calendarBoxStore.removeAll();
        for (int index = 0; index < calendarStore.getCount(); index++) {
            final CalendarWrapper calendarWrapper = calendarStore.getAt(index);
            if (calendarWrapper.getCalendar().isEditable()) {
                calendarBoxStore.add(calendarWrapper);
            }
        }

        // OK button - attempts to save the new event
        addPersonalEventDialog.getButtonById(Dialog.OK).removeAllListeners();
        addPersonalEventDialog.getButtonById(Dialog.OK).addSelectionListener(new SelectionListener<ButtonEvent>() {

            @Override
            public void componentSelected(ButtonEvent ce) {
                // Saves
                boolean valid = true;

                final HashMap<String, Serializable> properties = new HashMap<String, Serializable>();

                final int size = addPersonalEventDialog.getItemCount();
                for (int index = 0; index < size; index++) {
                    final Field<Serializable> field = (Field<Serializable>) addPersonalEventDialog.getWidget(index);
                    valid = field.isValid(false) && valid;

                    properties.put(field.getName(), field.getValue());
                }

                if (valid) {
                    final AsyncCallback<Event> callback = new AsyncCallback<Event>() {

                        @Override
                        public void onFailure(Throwable caught) {
                            addPersonalEventDialog.hide();
                            Log.error(I18N.CONSTANTS.calendarAddEventError(), caught);
                            MessageBox.alert(I18N.CONSTANTS.error(), I18N.CONSTANTS.calendarAddEventError(), null);
                        }

                        @Override
                        public void onSuccess(Event result) {
                            calendarWidget.refresh();
                            addPersonalEventDialog.hide();
                        }
                    };

                    if (event == null) {
                        addPersonalEvent(properties, dispatcher, callback);
                    } else {
                        editPersonalEvent(event, properties, dispatcher, callback);
                    }
                } else {
                    MessageBox.alert(I18N.CONSTANTS.error(), I18N.CONSTANTS.calendarAddEventEmptyFields(), null);
                }
            }
        });

        return addPersonalEventDialog;
    }

    private void addPersonalEvent(final Map<String, Serializable> properties, final Dispatcher dispatcher,
            final AsyncCallback<Event> callback) {
        final CreateEntity createEntity = new CreateEntity("PersonalEvent", properties);
        dispatcher.execute(createEntity, null, new AsyncCallback<CreateResult>() {

            @Override
            public void onFailure(Throwable caught) {
                callback.onFailure(caught);
            }

            @Override
            public void onSuccess(CreateResult result) {
                // Creating events
                final Event event = new Event();
                event.setIdentifier((Integer) result.getNewId());
                updateEvent(event, properties);

                callback.onSuccess(event);
            }
        });
    }

    private void editPersonalEvent(final Event event, final Map<String, ?> properties, final Dispatcher dispatcher,
            final AsyncCallback<Event> callback) {
        final UpdateEntity updateEntity = new UpdateEntity("PersonalEvent", (Integer) event.getIdentifier(),
                (Map<String, Object>) properties);
        dispatcher.execute(updateEntity, null, new AsyncCallback<VoidResult>() {

            @Override
            public void onFailure(Throwable caught) {
                callback.onFailure(caught);
            }

            @Override
            public void onSuccess(VoidResult result) {
                final Calendar calendar = event.getParent();

                final List<Event> oldEventList = calendar.getEvents().get(
                        new Date(event.getDtstart().getYear(), event.getDtstart().getMonth(), event.getDtstart()
                                .getDate()));
                oldEventList.remove(event);

                updateEvent(event, properties);

                callback.onSuccess(event);
            }
        });
    }

    private void updateEvent(Event event, final Map<String, ?> properties) {
        event.setSummary((String) properties.get("summary"));
        event.setDescription((String) properties.get("description"));

        final Date day = (Date) properties.get("date");
        final Time startHour = (Time) properties.get("startDate");
        final Time endHour = (Time) properties.get("endDate");

        if (startHour != null) {
            event.setDtstart(new Date(day.getYear(), day.getMonth(), day.getDate(), startHour.getHour(), startHour
                    .getMinutes()));
            if (endHour != null) {
                event.setDtend(new Date(day.getYear(), day.getMonth(), day.getDate(), endHour.getHour(), endHour
                        .getMinutes()));
            } else {
                event.setDtend(null);
            }

        } else {
            event.setDtstart(new Date(day.getYear(), day.getMonth(), day.getDate()));
            event.setDtend(new Date(day.getYear(), day.getMonth(), day.getDate() + 1));
        }

        // Adding the new event to the calendar
        final CalendarWrapper wrapper = (CalendarWrapper) properties.get("calendarId");
        final Calendar calendar = wrapper.getCalendar();

        event.setParent(calendar);

        List<Event> events = calendar.getEvents().get(day);
        if (events == null) {
            events = new ArrayList<Event>();
            calendar.getEvents().put(day, events);
        }
        events.add(event);
    }
}
