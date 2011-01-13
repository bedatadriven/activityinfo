/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.page.project.calendar;

import com.allen_sauer.gwt.log.client.Log;
import com.extjs.gxt.ui.client.data.BaseModel;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.grid.CheckBoxSelectionModel;
import com.google.gwt.user.client.rpc.AsyncCallback;
import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.dispatch.remote.Authentication;
import org.sigmah.client.i18n.I18N;
import org.sigmah.client.page.project.SubPresenter;
import org.sigmah.client.page.project.ProjectPresenter;
import org.sigmah.client.ui.CalendarWidget;
import org.sigmah.shared.command.GetCalendar;
import org.sigmah.shared.domain.calendar.ActivityCalendarIdentifier;
import org.sigmah.shared.domain.calendar.Calendar;
import org.sigmah.shared.domain.calendar.CalendarType;
import org.sigmah.shared.domain.calendar.MonitoredPointCalendarIdentifier;
import org.sigmah.shared.domain.calendar.ReminderCalendarIdentifier;
import org.sigmah.shared.dto.ProjectDTO;

/**
 * 
 * @author Raphaël Calabro (rcalabro@ideia.fr)
 */
public class ProjectCalendarPresenter implements SubPresenter {
    private final ProjectPresenter projectPresenter;
    private ProjectDTO currentProjectDTO;

    private ProjectCalendarView view;
    private CalendarWidget calendar;
    private Dispatcher dispatcher;
    private Authentication authentication;

    private ListStore<CalendarWrapper> calendarStore;
    private CheckBoxSelectionModel<CalendarWrapper> selectionModel;

    private boolean needsRefresh;

    private int calendarIndex = 1;

    /**
     * Wrapper class that allow the use of {@link Calendar}s objects with
     * Ext-GWT.
     */
    public static class CalendarWrapper extends BaseModel {

        private static final long serialVersionUID = 1017103235263407544L;

        private Calendar calendar;

        /**
         * Empty constructor, needed by the serialization process.
         */
        public CalendarWrapper() {
        }

        /**
         * Wrap the given Calendar as a BaseModel object.
         * 
         * @param calendar
         *            the calendar to wrap.
         */
        public CalendarWrapper(Calendar calendar) {
            this.set("name", calendar.getName());
            this.set("id", calendar.getIdentifier());
            this.calendar = calendar;
        }

        public Calendar getCalendar() {
            return calendar;
        }

        public void setCalendar(Calendar calendar) {
            this.calendar = calendar;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final CalendarWrapper other = (CalendarWrapper) obj;
            if (this.calendar != other.calendar && (this.calendar == null || !this.calendar.equals(other.calendar))) {
                return false;
            }
            return true;
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 53 * hash + (this.calendar != null ? this.calendar.hashCode() : 0);
            return hash;
        }
    }

    public ProjectCalendarPresenter(Dispatcher dispatcher, Authentication authentication, ProjectPresenter projectPresenter) {
        this.dispatcher = dispatcher;
        this.authentication=authentication;
        this.projectPresenter = projectPresenter;
    }

    @Override
    public Component getView() {
        if (view == null) {
            calendar = new CalendarWidget(CalendarWidget.COLUMN_HEADERS, true);
            calendarStore = new ListStore<CalendarWrapper>();
            selectionModel = new CheckBoxSelectionModel<CalendarWrapper>();

            view = new ProjectCalendarView(calendar, calendarStore, selectionModel, dispatcher, authentication);
        }

        // If the current project has changed, clear the view
        if (!projectPresenter.getCurrentProjectDTO().equals(currentProjectDTO)) {
            view.getAddEventButton().setEnabled(false);
            calendarStore.removeAll(); // Reset the calendar list
            calendar.today(); // Reset the current date
            calendar.setDisplayMode(CalendarWidget.DisplayMode.MONTH);
            calendarIndex = 1; // Reset the styles
            currentProjectDTO = projectPresenter.getCurrentProjectDTO();

        } else
            needsRefresh = true;

        return view;
    }

    @Override
    public void viewDidAppear() {
        // Fetching the calendars
        if (calendarStore.getCount() == 0 || needsRefresh) {
            final AsyncCallback<Calendar> callback = new AsyncCallback<Calendar>() {
                @Override
                public void onSuccess(Calendar result) {
                    if (needsRefresh) {
                        calendarIndex = 1;
                        calendarStore.removeAll();
                        needsRefresh = false;
                    }

                    // Defines the color index of the calendar
                    result.setStyle(calendarIndex++);

                    calendarStore.add(new CalendarWrapper(result));
                    selectionModel.select(calendarStore.getCount() - 1, true);

                    if (result.isEditable()) {
                        view.getAddEventButton().setEnabled(true);
                    }
                }

                @Override
                public void onFailure(Throwable caught) {
                    Log.debug("Error while loading a calendar", caught);
                }
            };

            // Retrieving the activities
            final ActivityCalendarIdentifier identifier = new ActivityCalendarIdentifier(currentProjectDTO.getId(),
                    I18N.CONSTANTS.logFrameActivities(), I18N.CONSTANTS.logFrameActivitiesCode());
            final GetCalendar getActivityCalendar = new GetCalendar(CalendarType.Activity, identifier);
            dispatcher.execute(getActivityCalendar, null, callback);

            // Retrieving the events linked to the current project
            final Integer calendarId = currentProjectDTO.getCalendarId();
            if (calendarId != null) {
                final GetCalendar getPersonalCalendar = new GetCalendar(CalendarType.Personal, calendarId);
                dispatcher.execute(getPersonalCalendar, null, callback);
            }

            // Retrieving the reminders
            final ReminderCalendarIdentifier reminderIdentifier = new ReminderCalendarIdentifier(currentProjectDTO
                    .getRemindersList().getId(), I18N.CONSTANTS.reminderPoints(),
                    I18N.CONSTANTS.monitoredPointClosed(), I18N.CONSTANTS.monitoredPointExpectedDate(),
                    I18N.CONSTANTS.monitoredPointDateFormat());
            final GetCalendar getReminderCalendar = new GetCalendar(CalendarType.Reminder, reminderIdentifier);
            dispatcher.execute(getReminderCalendar, null, callback);

            // Retrieving the monitored points
            final MonitoredPointCalendarIdentifier monitoredPointIdentifier = new MonitoredPointCalendarIdentifier(
                    currentProjectDTO.getPointsList().getId(), I18N.CONSTANTS.monitoredPoints(),
                    I18N.CONSTANTS.monitoredPointClosed(), I18N.CONSTANTS.monitoredPointExpectedDate(),
                    I18N.CONSTANTS.monitoredPointDateFormat());
            final GetCalendar getMonitoredPointCalendar = new GetCalendar(CalendarType.MonitoredPoint,
                    monitoredPointIdentifier);
            dispatcher.execute(getMonitoredPointCalendar, null, callback);
        }
    }

}
