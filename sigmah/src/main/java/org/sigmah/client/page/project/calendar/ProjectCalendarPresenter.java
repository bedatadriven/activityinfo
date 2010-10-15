/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.page.project.calendar;

import com.allen_sauer.gwt.log.client.Log;
import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.data.BaseModel;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.event.SelectionChangedListener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.grid.CheckBoxSelectionModel;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.toolbar.SeparatorToolItem;
import com.extjs.gxt.ui.client.widget.toolbar.ToolBar;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.i18n.client.constants.DateTimeConstants;
import com.google.gwt.user.client.rpc.AsyncCallback;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.sigmah.client.dispatch.Dispatcher;
import org.sigmah.client.i18n.I18N;
import org.sigmah.client.page.project.Presenter;
import org.sigmah.client.page.project.ProjectPresenter;
import org.sigmah.client.ui.CalendarWidget;
import org.sigmah.shared.command.GetCalendar;
import org.sigmah.shared.domain.calendar.Calendar;
import org.sigmah.shared.dto.ProjectDTO;

/**
 *
 * @author Raphaël Calabro (rcalabro@ideia.fr)
 */
public class ProjectCalendarPresenter implements Presenter {
    private final ProjectPresenter projectPresenter;
    private ProjectDTO currentProjectDTO;

    private LayoutContainer view;
    private CalendarWidget calendar;
    private Dispatcher dispatcher;

    private ListStore<CalendarWrapper> calendarStore = new ListStore<CalendarWrapper>();
    private CheckBoxSelectionModel<CalendarWrapper> selectionModel;

    private int calendarIndex = 1;

    private static class CalendarWrapper extends BaseModel {
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
            // TODO: Externalize this

            // Initialization
            
            final BorderLayout borderLayout = new BorderLayout();
            borderLayout.setContainerStyle("x-border-layout-ct main-background");
            view = new LayoutContainer(borderLayout);

            final ContentPanel calendarList = new ContentPanel(new FitLayout());
            calendarList.setHeading(I18N.CONSTANTS.projectTabCalendar());
            final BorderLayoutData calendarListData = new BorderLayoutData(LayoutRegion.WEST, 250);
            calendarListData.setCollapsible(true);
            view.add(calendarList, calendarListData);

            // Calendar list
            selectionModel = new CheckBoxSelectionModel<CalendarWrapper>();

            final ColumnConfig countryName = new ColumnConfig("name", I18N.CONSTANTS.name(), 200);
            final ColumnModel countryColumnModel = new ColumnModel(Arrays.asList(selectionModel.getColumn(), countryName));

            final Grid<CalendarWrapper> calendarGrid = new Grid<CalendarWrapper>(calendarStore, countryColumnModel);
            calendarGrid.setAutoExpandColumn("name");
            calendarGrid.setSelectionModel(selectionModel);
            calendarGrid.addPlugin(selectionModel);

            calendarGrid.getView().setForceFit(true);

            calendarList.add(calendarGrid);

            // Calendar
            calendar = new CalendarWidget(CalendarWidget.COLUMN_HEADERS, true);

            selectionModel.addSelectionChangedListener(new SelectionChangedListener<CalendarWrapper>() {
                @Override
                public void selectionChanged(SelectionChangedEvent<CalendarWrapper> se) {
                    final List<CalendarWrapper> wrappers = se.getSelection();
                    final ArrayList<Calendar> calendars = new ArrayList<Calendar>();
                    for(final CalendarWrapper wrapper : wrappers) {
                        calendars.add(wrapper.getCalendar());
                    }
                    calendar.setCalendars(calendars);
                }
            });

            // Defining the first day of the week
            // LocaleInfo uses 1 for Sunday and 2 for Monday. Substracting 1 since Date starts with 0 for Sunday.
            final DateTimeConstants constants = LocaleInfo.getCurrentLocale().getDateTimeConstants();
            calendar.setFirstDayOfWeek(Integer.parseInt(constants.firstDayOfTheWeek())-1);

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

            calendarView.setTopComponent(toolbar);

            final BorderLayoutData calendarViewData = new BorderLayoutData(LayoutRegion.CENTER);
            calendarViewData.setMargins(new Margins(0, 0, 0, 8));
            calendarView.addStyleName("panel-background");
            calendarView.add(calendar);

            view.add(calendarView, calendarViewData);
        }

        // If the current project has changed, clear the view
        if(projectPresenter.getCurrentProjectDTO() != currentProjectDTO) {
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
                }

                @Override
                public void onFailure(Throwable caught) {
                    Log.debug("Error while loading a calendar", caught);
                }
            };

            final GetCalendar getDummyCalendar = new GetCalendar(GetCalendar.Type.Dummy, "Dummy");
            dispatcher.execute(getDummyCalendar, null, callback);

            final Long calendarId = currentProjectDTO.getCalendarId();
            if(calendarId != null) {
                final GetCalendar getPersonalCalendar = new GetCalendar(GetCalendar.Type.Personal, calendarId);
                dispatcher.execute(getPersonalCalendar, null, callback);
            }
        }
    }

}
