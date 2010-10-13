/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.client.page.project.calendar;

import com.extjs.gxt.ui.client.Style.LayoutRegion;
import com.extjs.gxt.ui.client.data.BaseModel;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.grid.CheckBoxSelectionModel;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.layout.BorderLayout;
import com.extjs.gxt.ui.client.widget.layout.BorderLayoutData;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import java.util.Arrays;
import org.sigmah.client.i18n.I18N;
import org.sigmah.client.page.project.Presenter;
import org.sigmah.client.ui.CalendarWidget;
import org.sigmah.shared.domain.calendar.Calendar;

/**
 *
 * @author Raphaël Calabro (rcalabro@ideia.fr)
 */
public class ProjectCalendarPresenter implements Presenter {
    private LayoutContainer view;
    private CalendarWidget calendar;

    private static class CalendarWrapper extends BaseModel {

        public CalendarWrapper(Calendar cal) {
            this.set("name", cal.getName());
        }
        
    }
    
    @Override
    public Component getView() {
        if(view == null) {
            final BorderLayout borderLayout = new BorderLayout();
            borderLayout.setContainerStyle("x-border-layout-ct main-background");
            view = new LayoutContainer(borderLayout);

            final ContentPanel calendarList = new ContentPanel(new FitLayout());
            calendarList.setTitle("Calendriers");
            final BorderLayoutData calendarListData = new BorderLayoutData(LayoutRegion.WEST, 250);
            view.add(calendarList, calendarListData);

            // Calendar list
            final CheckBoxSelectionModel<CalendarWrapper> selectionModel = new CheckBoxSelectionModel<CalendarWrapper>();

            final ColumnConfig countryName = new ColumnConfig("name", I18N.CONSTANTS.projectTabCalendar(), 200);
            final ColumnModel countryColumnModel = new ColumnModel(Arrays.asList(selectionModel.getColumn(), countryName));

            final Grid<CalendarWrapper> calendarGrid = new Grid<CalendarWrapper>(new ListStore<CalendarWrapper>(), countryColumnModel);
            calendarGrid.setAutoExpandColumn("name");
            calendarGrid.setSelectionModel(selectionModel);
            calendarGrid.addPlugin(selectionModel);

            calendarList.add(calendarGrid);

            // Calendar
            calendar = new CalendarWidget(true, true);
            calendar.setFirstDayOfWeek(Integer.parseInt(I18N.CONSTANTS.firstDayOfWeek()));

            LayoutContainer calendarView = new LayoutContainer(new FitLayout());
            final BorderLayoutData calendarViewData = new BorderLayoutData(LayoutRegion.CENTER);
            calendarViewData.setMargins(new Margins(0, 0, 0, 8));
            calendarView.addStyleName("panel-background");
            calendarView.add(calendar);

            view.add(calendarView, calendarViewData);
        }
        return view;
    }

    @Override
    public void viewDidAppear() {
        calendar.calibrateCalendar();
    }

}
