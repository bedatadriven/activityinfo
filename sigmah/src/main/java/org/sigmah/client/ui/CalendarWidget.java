package org.sigmah.client.ui;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.Window;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import fr.ideia.gwt.client.date.BasicDateFormat;
import fr.ideia.gwt.client.date.Dates;
import java.util.ArrayList;
import org.sigmah.shared.domain.calendar.Calendar;
import org.sigmah.shared.domain.calendar.Event;

@SuppressWarnings("deprecation")
public class CalendarWidget extends Composite {
    /**
     * Types of displays availables for a calendar.
     * @author rca
     */
    public static enum DisplayMode {
        DAY(1, 1) {
            @Override
            public Date getStartDate(Date date, int firstDay) {
                return new Date(date.getYear(), date.getMonth(), date.getDate());
            }

            @Override
            public void nextDate(Date currentDate) {
                currentDate.setDate(currentDate.getDate()+1);
            }

            @Override
            public void previousDate(Date currentDate) {
                currentDate.setDate(currentDate.getDate()-1);
            }

            @Override
            public void firstDay(Date currentDate, Date today, int firstDay) {
                currentDate.setYear(today.getYear());
                currentDate.setMonth(today.getMonth());
                currentDate.setDate(today.getDate());
            }

            @Override
            public String getStyleName() {
                return "calendar-day";
            }
        },
        WEEK(7, 1) {
            @Override
            public Date getStartDate(Date date, int firstDay) {
                return Dates.getFirstDateOfWeek(date, firstDay);
            }

            @Override
            public void nextDate(Date currentDate) {
                currentDate.setDate(currentDate.getDate()+7);
            }

            @Override
            public void previousDate(Date currentDate) {
                currentDate.setDate(currentDate.getDate()-7);
            }

            @Override
            public void firstDay(Date currentDate, Date today, int firstDay) {
                int decal = (today.getDay()+7-firstDay)%7;
                currentDate.setYear(today.getYear());
                currentDate.setMonth(today.getMonth());
                currentDate.setDate(today.getDate() - decal);
                final Date date = Dates.getFirstDateOfWeek(today, firstDay);
                currentDate.setTime(date.getTime());
            }

            @Override
            public String getStyleName() {
                return "calendar-week";
            }
        },
        MONTH(7, 6) {
            @Override
            public Date getStartDate(Date date, int firstDay) {
                Date firstDayOfMonth = new Date(date.getYear(), date.getMonth(), 1);
                return Dates.getFirstDateOfWeek(firstDayOfMonth, firstDay);
            }

            @Override
            public void nextDate(Date currentDate) {
                currentDate.setMonth(currentDate.getMonth()+1);
            }

            @Override
            public void previousDate(Date currentDate) {
                currentDate.setMonth(currentDate.getMonth()-1);
            }
            
            @Override
            public void firstDay(Date currentDate, Date today, int firstDay) {
                currentDate.setYear(today.getYear());
                currentDate.setMonth(today.getMonth());
                currentDate.setDate(1);
            }

            @Override
            public String getStyleName() {
                return "calendar-month";
            }
        };
        
        private int columns;
        private int rows;
        
        private DisplayMode(int columns, int rows) {
            this.columns = columns;
            this.rows = rows;
        }
        
        public int getRows() {
            return rows;
        }
        public int getColumns() {
            return columns;
        }
        
        public abstract Date getStartDate(Date date, int firstDay);
        public abstract void nextDate(Date currentDate);
        public abstract void previousDate(Date currentDate);
        public abstract void firstDay(Date currentDate, Date today, int firstDay);
        public abstract String getStyleName();
    }

    private final static int UNDEFINED = -1;
    private final static int EVENT_HEIGHT = 16;
    private int eventLimit = UNDEFINED;
    
    private int firstDayOfWeek;
    private DisplayMode displayMode = DisplayMode.MONTH;
    private boolean displayHeaders = true;
    private boolean displayWeekNumber = true;

    private ArrayList<Calendar> calendars;
    
    private Date today;
    private Date startDate;
    
    private BasicDateFormat titleFormatter = new BasicDateFormat("N y");
    private BasicDateFormat headerFormatter = new BasicDateFormat("E");
    private BasicDateFormat dayFormatter = new BasicDateFormat("d");
    private BasicDateFormat hourFormatter = new BasicDateFormat("H:m");
    
    public CalendarWidget(boolean displayHeaders, boolean displayWeekNumber) {
        this.calendars = new ArrayList<Calendar>();
        this.displayHeaders = displayHeaders;
        this.displayWeekNumber = displayWeekNumber;
        
        final FlexTable grid = new FlexTable();
        grid.addStyleName("calendar");
        grid.addStyleName(displayMode.getStyleName());

        initWidget(grid);
        
        final Date now = new Date();
        today = new Date(now.getYear(), now.getMonth(), now.getDate());
        startDate = new Date(0, 0, 0);
        
        today();
    }
    
    public void next() {
        displayMode.nextDate(startDate);
        drawCalendar();
    }
    
    public void previous() {
        displayMode.previousDate(startDate);
        drawCalendar();
    }
    
    public final void today() {
        displayMode.firstDay(startDate, today, firstDayOfWeek);
        drawCalendar();
    }
    
    /**
     * Retrieves the current start date of the calendar. 
     * @return the current start date of the calendar. 
     */
    public Date getStartDate() {
        return startDate;
    }

    public void addCalendar(Calendar calendar) {
        calendars.add(calendar);
    }

    public ArrayList<Calendar> getCalendars() {
        return calendars;
    }

    /**
     * Defines the formatter used to display the title of the calendar.<br>
     * <br>
     * The default format is "<code>MonthName</code> <code>FullYear</code>" (pattern : "N y").
     * @param titleFormatter The formatter to use to display the title of the calendar.
     */
    public void setTitleFormatter(BasicDateFormat titleFormatter) {
        this.titleFormatter = titleFormatter;
        drawCalendar();
    }
    
    /**
     * Defines the formatter used to display the title of each column.<br>
     * <br>
     * The default format is "<code>WeekName</code>" (pattern : "E").
     * @param titleFormatter The formatter to use to display the title of each column.
     */
    public void setHeaderFormatter(BasicDateFormat headerFormatter) {
        this.headerFormatter = headerFormatter;
        drawCalendar();
    }
    
    /**
     * Defines the formatter used to display the title of each cell.<br>
     * <br>
     * The default format is "<code>DayNumber</code>" (pattern : "d").
     * @param titleFormatter The formatter to use to display the title of each cell.
     */
    public void setDayFormatter(BasicDateFormat dayFormatter) {
        this.dayFormatter = dayFormatter;
        drawCalendar();
    }

    /**
     * Defines the display mode of the calendar and perform a redraw.
     * @param displayMode Style of the calendar (day, week or month).
     * @see CalendarWidget.DisplayMode
     */
    public void setDisplayMode(DisplayMode displayMode) {
        final FlexTable grid = (FlexTable) getWidget();
        
        clear();

        // Resetting the CSS style
        grid.removeStyleName(this.displayMode.getStyleName());

        this.displayMode = displayMode;

        // Applying the CSS style associated with the new display mode
        grid.addStyleName(displayMode.getStyleName());

        drawCalendar();
    }
    
    /**
     * Defines the first day of the week and refresh the calendar.
     * @param firstDayOfWeek The first day of the week as an int (Sunday = 0, Saturday = 6)
     */
    public void setFirstDayOfWeek(int firstDayOfWeek) {
        this.firstDayOfWeek = firstDayOfWeek;
        drawCalendar();
    }
    
    public boolean isDisplayHeaders() {
        return displayHeaders;
    }
    
    public void setDisplayHeaders(boolean displayHeaders) {
        clear();
        this.displayHeaders = displayHeaders;
        drawCalendar();
    }
    
    public boolean isDisplayWeekNumber() {
        return displayWeekNumber;
    }
    
    public void setDisplayWeekNumber(boolean displayWeekNumber) {
        clear();
        this.displayWeekNumber = displayWeekNumber;
        drawCalendar();
    }
    
    /**
     * Removes all rows. Must be when the structure of the calendar has been changed (display mode) 
     */
    private void clear() {
        final FlexTable grid = (FlexTable) getWidget();
        grid.clear();
        grid.removeAllRows();

        eventLimit = UNDEFINED;
    }

    /**
     * Calculates the number of events that can be displayed in a cell.
     */
    public void calibrateCalendar() {
        final FlexTable grid = (FlexTable) getWidget();

        final Element cell = grid.getCellFormatter().getElement(displayHeaders?2:0, displayWeekNumber?1:0);
        cell.setId("calendar-cell-calibration");
        
        eventLimit = (getCellHeight() / EVENT_HEIGHT) - 1;
    }

    private native int getCellHeight() /*-{
        var height = 0;

        var cell = $wnd.document.getElementById('calendar-cell-calibration');

        var style = $wnd.getComputedStyle(cell, null);
        height += parseInt(style.height);

        return height;
    }-*/;
    
    /**
     * Render the calendar
     */
    private void drawCalendar() {
        final FlexTable grid = (FlexTable) getWidget();
        
        final int rows = displayMode.getRows() + (displayHeaders?2:0);
        final int columns = displayMode.getColumns() + (displayWeekNumber?1:0);
        
        Date date = displayMode.getStartDate(startDate, firstDayOfWeek);
        
        // Column headers
        if(displayHeaders) {
            // Header of the calendar
            final Label calendarHeader = new Label(titleFormatter.format(startDate));
            calendarHeader.addStyleName("calendar-header");
            grid.setWidget(0, 0, calendarHeader);
            grid.getFlexCellFormatter().setColSpan(0, 0, columns+(displayWeekNumber?1:0));
            
            Date currentHeader = new Date(date.getTime());
            for(int x = displayWeekNumber?1:0; x < columns; x++) {
                final Label columnHeader = new Label(headerFormatter.format(currentHeader));
                columnHeader.addStyleName("calendar-column-header");
                grid.setWidget(1, x, columnHeader);
                
                currentHeader.setDate(currentHeader.getDate()+1);
            }
        }
        
        int currentMonth = startDate.getMonth();
        for(int y = displayHeaders?2:0; y < rows; y++) {
            if(displayWeekNumber) {
                grid.getCellFormatter().addStyleName(y, 0, "calendar-row-header");
                grid.setText(y, 0, Integer.toString(Dates.getWeekNumber(date, firstDayOfWeek)));
            }
            
            for(int x = displayWeekNumber?1:0; x < columns; x++) {
                drawCell(y, x, date, currentMonth);
                date.setDate(date.getDate()+1);
            }
        }
    }
    
    /**
     * Render the cell located at <code>column</code>, <code>row</code> 
     * @param row
     * @param column
     * @param date
     * @param currentMonth
     */
    private void drawCell(int row, int column, Date date, int currentMonth) {
        final Label header = new Label(dayFormatter.format(date));
        header.addStyleName("calendar-cell-header");
        
        final FlexTable grid = (FlexTable) getWidget();
        
        grid.getCellFormatter().setStyleName(row, column, "calendar-cell");
        
        VerticalPanel cell = (VerticalPanel) grid.getWidget(row, column);
        if(cell == null) {
            // New cell
            cell = new VerticalPanel();
            cell.setWidth("100%");
    
            grid.setWidget(row, column, cell);
            
        } else {
            // Reusing an existing cell
            cell.clear();
        }
        
        if(currentMonth != date.getMonth())
            grid.getCellFormatter().addStyleName(row, column, "calendar-cell-other-month");
        if(date.equals(today))
            grid.getCellFormatter().addStyleName(row, column, "calendar-cell-today");
        
        cell.add(header);

        // Displaying events
        int total = 0;
        
        int style = 1;
        for(final Calendar calendar : calendars) {
            Map<Date, List<Event>> eventMap = calendar.getEvents();
            final List<Event> events = eventMap.get(date);

            if(events != null) {
                for(int i = 0; i < events.size() && total + i < eventLimit; i++) {
                    final Event event = events.get(i);
                    
                    StringBuilder name = new StringBuilder();
                    name.append(hourFormatter.format(event.getDtstart()));
                    name.append(" ");
                    name.append(hourFormatter.format(event.getDtend()));
                    name.append(" ");
                    name.append(event.getSummary());

                    final Label eventLabel = new Label(name.toString());
                    eventLabel.addStyleName("calendar-event");
                    eventLabel.addStyleName("calendar-event-" + style);
                    cell.add(eventLabel);
                }
                total += events.size();
            }
            
            style++;
        }

        if(eventLimit != UNDEFINED && total > eventLimit) {
            final Label eventLabel = new Label("...");
            eventLabel.addStyleName("calendar-event-limit");
            cell.add(eventLabel);
        }
    }
}
