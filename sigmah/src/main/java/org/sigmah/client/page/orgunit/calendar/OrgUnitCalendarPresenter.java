/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.page.orgunit.calendar;

import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.dispatch.remote.Authentication;
import org.sigmah.client.page.orgunit.OrgUnitPresenter;
import org.sigmah.client.page.project.SubPresenter;
import org.sigmah.client.page.project.calendar.ProjectCalendarPresenter.CalendarWrapper;
import org.sigmah.client.page.project.calendar.ProjectCalendarView;
import org.sigmah.client.ui.CalendarWidget;
import org.sigmah.shared.command.GetCalendar;
import org.sigmah.shared.domain.calendar.Calendar;
import org.sigmah.shared.domain.calendar.CalendarType;
import org.sigmah.shared.dto.OrgUnitDTO;

import com.allen_sauer.gwt.log.client.Log;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.grid.CheckBoxSelectionModel;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * 
 * @author tmi
 */
public class OrgUnitCalendarPresenter implements SubPresenter {

    private final OrgUnitPresenter mainPresenter;
    private OrgUnitDTO currentOrgUnitDTO;

    private ProjectCalendarView view;
    private CalendarWidget calendar;
    private Dispatcher dispatcher;
    private Authentication authentication;

    private ListStore<CalendarWrapper> calendarStore;
    private CheckBoxSelectionModel<CalendarWrapper> selectionModel;

    private int calendarIndex = 1;

    public OrgUnitCalendarPresenter(Dispatcher dispatcher, Authentication authentication, OrgUnitPresenter mainPresenter) {
        this.dispatcher = dispatcher;
        this.authentication = authentication;
        this.mainPresenter = mainPresenter;
    }

    @Override
    public Component getView() {
        if (view == null) {
            calendar = new CalendarWidget(CalendarWidget.COLUMN_HEADERS, true);
            calendarStore = new ListStore<CalendarWrapper>();
            selectionModel = new CheckBoxSelectionModel<CalendarWrapper>();

            view = new ProjectCalendarView(calendar, calendarStore, selectionModel, dispatcher, authentication);
        }

        // If the current org unit has changed, clear the view
        if (!mainPresenter.getCurrentOrgUnitDTO().equals(currentOrgUnitDTO)) {
            view.getAddEventButton().setEnabled(false);
            calendarStore.removeAll(); // Reset the calendar list
            calendar.today(); // Reset the current date
            calendar.setDisplayMode(CalendarWidget.DisplayMode.MONTH);
            calendarIndex = 1; // Reset the styles
            currentOrgUnitDTO = mainPresenter.getCurrentOrgUnitDTO();
        }

        return view;
    }

    @Override
    public void viewDidAppear() {
        // Fetching the calendars
        if (calendarStore.getCount() == 0) {
            final AsyncCallback<Calendar> callback = new AsyncCallback<Calendar>() {
                @Override
                public void onSuccess(Calendar result) {
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

            // Retrieving the events linked to the current org unit
            final Integer calendarId = currentOrgUnitDTO.getCalendarId();
            if (calendarId != null) {
                final GetCalendar getPersonalCalendar = new GetCalendar(CalendarType.Personal, calendarId);
                dispatcher.execute(getPersonalCalendar, null, callback);
            }
        }
    }

}
