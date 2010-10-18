/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.page.project.calendar;

import com.allen_sauer.gwt.log.client.Log;
import com.extjs.gxt.ui.client.data.BaseModel;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.grid.CheckBoxSelectionModel;
import com.google.gwt.user.client.rpc.AsyncCallback;
import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.page.project.Presenter;
import org.sigmah.client.page.project.ProjectPresenter;
import org.sigmah.client.ui.CalendarWidget;
import org.sigmah.shared.command.GetCalendar;
import org.sigmah.shared.domain.calendar.Calendar;
import org.sigmah.shared.domain.calendar.CalendarType;
import org.sigmah.shared.dto.ProjectDTO;

/**
 *
 * @author Raphaël Calabro (rcalabro@ideia.fr)
 */
public class ProjectCalendarPresenter implements Presenter {
    private final ProjectPresenter projectPresenter;
    private ProjectDTO currentProjectDTO;

    private ProjectCalendarView view;
    private CalendarWidget calendar;
    private Dispatcher dispatcher;

    private ListStore<CalendarWrapper> calendarStore;
    private CheckBoxSelectionModel<CalendarWrapper> selectionModel;

    private int calendarIndex = 1;

    public static class CalendarWrapper extends BaseModel {
        private Calendar calendar;
        
        public CalendarWrapper(Calendar calendar) {
            this.set("name", calendar.getName());
            this.calendar = calendar;
        }

        public Calendar getCalendar() {
            return calendar;
        }
    }

    public ProjectCalendarPresenter(Dispatcher dispatcher, ProjectPresenter projectPresenter) {
        this.dispatcher = dispatcher;
        this.projectPresenter = projectPresenter;
    }

    @Override
    public Component getView() {
        if(view == null) {
            calendar = new CalendarWidget(CalendarWidget.COLUMN_HEADERS, true);
            calendarStore = new ListStore<CalendarWrapper>();
            selectionModel = new CheckBoxSelectionModel<CalendarWrapper>();
            
            view = new ProjectCalendarView(calendar, calendarStore, selectionModel);
        }

        // If the current project has changed, clear the view
        if(projectPresenter.getCurrentProjectDTO() != currentProjectDTO) {
            view.getAddEventButton().setEnabled(false);
            calendarStore.removeAll(); // Reset the calendar list
            calendar.today(); // Reset the current date
            calendar.setDisplayMode(CalendarWidget.DisplayMode.MONTH);
            calendarIndex = 1; // Reset the styles
            currentProjectDTO = projectPresenter.getCurrentProjectDTO();
        }

        return view;
    }

    @Override
    public void viewDidAppear() {
        // Fetching the calendars
        if(calendarStore.getCount() == 0) {
            final AsyncCallback<Calendar> callback = new AsyncCallback<Calendar>() {
                @Override
                public void onSuccess(Calendar result) {
                    result.setStyle(calendarIndex++); // Defines the color index of the calendar
                    calendarStore.add(new CalendarWrapper(result));
                    selectionModel.select(calendarStore.getCount()-1, true);

                    if(result.getType() == CalendarType.Personal)
                        view.getAddEventButton().setEnabled(true);
                }

                @Override
                public void onFailure(Throwable caught) {
                    Log.debug("Error while loading a calendar", caught);
                }
            };

            final GetCalendar getDummyCalendar = new GetCalendar(CalendarType.Dummy, "Dummy");
            dispatcher.execute(getDummyCalendar, null, callback);

            final Long calendarId = currentProjectDTO.getCalendarId();
            if(calendarId != null) {
                final GetCalendar getPersonalCalendar = new GetCalendar(CalendarType.Personal, calendarId);
                dispatcher.execute(getPersonalCalendar, null, callback);
            }
        }
    }

}
