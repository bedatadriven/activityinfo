package org.sigmah.client.ui;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DecoratedPopupPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel.PositionCallback;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.TreeSet;
import org.sigmah.client.i18n.I18N;
import org.sigmah.shared.domain.calendar.Calendar;
import org.sigmah.shared.domain.calendar.Event;

/**
 * This widget displays a calendar.
 * @author Raphaël Calabro (rcalabro@ideia.fr)
 */
@SuppressWarnings("deprecation")
public class CalendarWidget extends Composite {
    public static final int CELL_DEFAULT_WIDTH = 150;
    public static final int CELL_DEFAULT_HEIGHT = 80;

    public interface CalendarListener {
        public void afterRefresh();
    }

    public interface Delegate {
        void edit(Event event, CalendarWidget calendarWidget);
        void delete(Event event, CalendarWidget calendarWidget);
    }

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
                return getFirstDateOfWeek(date, firstDay);
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
                final Date date = getFirstDateOfWeek(today, firstDay);
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
                return getFirstDateOfWeek(firstDayOfMonth, firstDay);
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

    public final static int NO_HEADERS = 0;
    public final static int COLUMN_HEADERS = 1;
    public final static int ALL_HEADERS = 2;

    private final static int UNDEFINED = -1;
    private final static int EVENT_HEIGHT = 16;
    private int eventLimit = UNDEFINED;
    
    private int firstDayOfWeek;
    private DisplayMode displayMode = DisplayMode.MONTH;
    private int displayHeaders = ALL_HEADERS;
    private boolean displayWeekNumber = true;

    private List<Calendar> calendars;
    
    private Date today;
    private Date startDate;
    
    private DateTimeFormat titleFormatter = DateTimeFormat.getFormat("MMMM y");
    private DateTimeFormat headerFormatter = DateTimeFormat.getFormat("EEEE");
    private DateTimeFormat dayFormatter = DateTimeFormat.getFormat("d");
    private DateTimeFormat hourFormatter = DateTimeFormat.getFormat("HH:mm");

    private CalendarListener listener;
    private Delegate delegate;

    public CalendarWidget(int displayHeaders, boolean displayWeekNumber) {
        this.calendars = new ArrayList<Calendar>();
        this.displayHeaders = displayHeaders;
        this.displayWeekNumber = displayWeekNumber;

//        final SimplePanel container;

        final FlexTable grid = new FlexTable();
        grid.addStyleName("calendar");
        grid.addStyleName(displayMode.getStyleName());

        initWidget(grid);
        
        final Date now = new Date();
        today = new Date(now.getYear(), now.getMonth(), now.getDate());
        startDate = new Date(0, 0, 0);
        
        today();
    }

    public void setDelegate(Delegate delegate) {
        this.delegate = delegate;
    }

    public void setListener(CalendarListener listener) {
        this.listener = listener;
    }

    public void next() {
        displayMode.nextDate(startDate);
        refresh();
    }
    
    public void previous() {
        displayMode.previousDate(startDate);
        refresh();
    }
    
    public final void today() {
        displayMode.firstDay(startDate, today, firstDayOfWeek);
        refresh();
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
        refresh();
    }

    public List<Calendar> getCalendars() {
        return calendars;
    }

    public void setCalendars(List<Calendar> calendars) {
        this.calendars = calendars;
        refresh();
    }

    /**
     * Defines the formatter used to display the title of the calendar.<br>
     * <br>
     * The default format is "<code>MonthName</code> <code>FullYear</code>" (pattern : "N y").
     * @param titleFormatter The formatter to use to display the title of the calendar.
     */
    public void setTitleFormatter(DateTimeFormat titleFormatter) {
        this.titleFormatter = titleFormatter;
        refresh();
    }
    
    /**
     * Defines the formatter used to display the title of each column.<br>
     * <br>
     * The default format is "<code>WeekName</code>" (pattern : "E").
     * @param titleFormatter The formatter to use to display the title of each column.
     */
    public void setHeaderFormatter(DateTimeFormat headerFormatter) {
        this.headerFormatter = headerFormatter;
        refresh();
    }
    
    /**
     * Defines the formatter used to display the title of each cell.<br>
     * <br>
     * The default format is "<code>DayNumber</code>" (pattern : "d").
     * @param titleFormatter The formatter to use to display the title of each cell.
     */
    public void setDayFormatter(DateTimeFormat dayFormatter) {
        this.dayFormatter = dayFormatter;
        refresh();
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

        refresh();
    }
    
    /**
     * Defines the first day of the week and refresh the calendar.
     * @param firstDayOfWeek The first day of the week as an int (Sunday = 0, Saturday = 6)
     */
    public void setFirstDayOfWeek(int firstDayOfWeek) {
        this.firstDayOfWeek = firstDayOfWeek;
        refresh();
    }

    public int getDisplayHeaders() {
        return displayHeaders;
    }

    public void setDisplayHeaders(int displayHeaders) {
        clear();
        this.displayHeaders = displayHeaders;
        refresh();
    }
    
    public boolean isDisplayWeekNumber() {
        return displayWeekNumber;
    }
    
    public void setDisplayWeekNumber(boolean displayWeekNumber) {
        clear();
        this.displayWeekNumber = displayWeekNumber;
        refresh();
    }

    /**
     * Removes all rows. Must be when the structure of the calendar has been changed (display mode) 
     */
    private void clear() {
        final FlexTable grid = (FlexTable) getWidget();
        grid.clear();
        grid.removeAllRows();
    }

    /**
     * Calculates the number of events that can be displayed in a cell.
     */
    public void calibrateCalendar() {
        final FlexTable grid = (FlexTable) getWidget();

        final Element row = grid.getRowFormatter().getElement(displayHeaders);
        row.setId("calendar-row-calibration");

        final Element cell = grid.getCellFormatter().getElement(displayHeaders, displayWeekNumber?1:0);
        cell.setId("calendar-cell-calibration");

        eventLimit = (getCellHeight(CELL_DEFAULT_HEIGHT) / EVENT_HEIGHT) - 2;
        if(eventLimit < 0)
            eventLimit = 0;
    }

    /**
     * Calculates the height of the cell identified by "calendar-cell-calibration".
     * @return height of a cell.
     */
    private native int getCellHeight(int defaultHeight) /*-{
        var height = 0;

        if(!$wnd.getComputedStyle)
            return defaultHeight;

        var row = $wnd.document.getElementById('calendar-row-calibration');

        var style = $wnd.getComputedStyle(row, null);
        height += parseInt(style.height);

        return height;
    }-*/;

    /**
     * Calculates the width of the cell identified by "calendar-cell-calibration".
     * @return width of a cell.
     */
    private native int getCellWidth(int defaultWidth) /*-{
        var width = 0;

        if(!$wnd.getComputedStyle)
            return defaultWidth;

        var cell = $wnd.document.getElementById('calendar-cell-calibration');

        var style = $wnd.getComputedStyle(cell, null);
        width += parseInt(style.width);

        return width;
    }-*/;

    /**
     * Retrieves the current heading of the calendar.
     * @return
     */
    public String getHeading() {
        final String title = titleFormatter.format(startDate);
        return Character.toUpperCase(title.charAt(0)) + title.substring(1);
    }

    /**
     * Render the calendar.
     */
    public void refresh() {
        drawEmptyCells();
        
        if(isAttached()) {
            calibrateCalendar();
            drawEvents();
        }
        if(listener != null)
            listener.afterRefresh();
    }

    /**
     * Render the whole calendar but do not render the events.
     */
    public void drawEmptyCells() {
        final FlexTable grid = (FlexTable) getWidget();

        final int rows = displayMode.getRows() + displayHeaders;
        final int columns = displayMode.getColumns() + (displayWeekNumber?1:0);

        Date date = displayMode.getStartDate(startDate, firstDayOfWeek);

        // Column headers
        if(displayHeaders != NO_HEADERS) {
            if(displayHeaders == ALL_HEADERS) {
                // Header of the calendar
                final Label calendarHeader = new Label(getHeading());
                calendarHeader.addStyleName("calendar-header");
                grid.setWidget(0, 0, calendarHeader);
                grid.getFlexCellFormatter().setColSpan(0, 0, columns+(displayWeekNumber?1:0));
            }

            final Date currentHeader = new Date(date.getTime());
            for(int x = displayWeekNumber?1:0; x < columns; x++) {
                final Label columnHeader = new Label(headerFormatter.format(currentHeader));
                columnHeader.addStyleName("calendar-column-header");
                grid.setWidget(displayHeaders == ALL_HEADERS ? 1:0, x, columnHeader);

                currentHeader.setDate(currentHeader.getDate()+1);
            }
        }

        int currentMonth = startDate.getMonth();
        for(int y = displayHeaders; y < rows; y++) {
            if(displayWeekNumber) {
                grid.getCellFormatter().addStyleName(y, 0, "calendar-row-header");
                grid.setText(y, 0, Integer.toString(getWeekNumber(date, firstDayOfWeek)));
            }

            for(int x = displayWeekNumber?1:0; x < columns; x++) {
                drawCell(y, x, date, currentMonth);
                date.setDate(date.getDate()+1);
            }
        }
    }

    /**
     * Render the events for every cells.
     */
    public void drawEvents() {
        final int rows = displayMode.getRows() + displayHeaders;
        final int columns = displayMode.getColumns() + (displayWeekNumber?1:0);

        Date date = displayMode.getStartDate(startDate, firstDayOfWeek);

        for(int y = displayHeaders; y < rows; y++) {
            for(int x = displayWeekNumber?1:0; x < columns; x++) {
                drawEvents(y, x, date);
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
        
        FlowPanel cell = (FlowPanel) grid.getWidget(row, column);
        if(cell == null) {
            // New cell
            cell = new FlowPanel();
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
    }

    /**
     * Display the events for the cell located at <code>column</code>, <code>row</code>
     * @param row
     * @param column
     * @param date
     * @param currentMonth
     */
    private void drawEvents(int row, int column, final Date date) {
        final FlexTable grid = (FlexTable) getWidget();

//        final VerticalPanel cell = (VerticalPanel) grid.getWidget(row, column);
        final FlowPanel cell = (FlowPanel) grid.getWidget(row, column);

        if(cell == null)
            throw new NullPointerException("The specified cell ("+row+','+column+") doesn't exist.");

        // Displaying events
        final TreeSet<Event> sortedEvents = new TreeSet<Event>(new Comparator<Event>() {
            @Override
            public int compare(Event o1, Event o2) {
                int compare = 0;

                if(o1 == null && o2 == null)
                    return 0;
                else if(o2 == null)
                    return 1;
                else if(o1 == null)
                    return -1;

                if(compare == 0 && o1.getDtstart() != null && o2.getDtstart() != null) {
                    long o1Start = o1.getDtstart().getTime();
                    long o2Start = o2.getDtstart().getTime();
                    
                    if(o1Start < o2Start)
                        compare = -1;
                    else if(o1Start > o2Start)
                        compare = 1;
                }

                if(compare == 0 && o1.getSummary() != null && o2.getSummary() != null)
                    compare = o1.getSummary().compareTo(o2.getSummary());

                return compare;
            }
        });

        for(final Calendar calendar : calendars) {
            final Map<Date, List<Event>> eventMap = calendar.getEvents();
            final List<Event> events = eventMap.get(date);

            if(events != null) {
                sortedEvents.addAll(events);
            }
        }

        final Iterator<Event> iterator = sortedEvents.iterator();
        for(int i = 0; iterator.hasNext() && i < eventLimit; i++) {
            final Event event = iterator.next();

            final ClickableFlowPanel flowPanel = new ClickableFlowPanel();
            flowPanel.addStyleName("calendar-event");

            boolean fullDayEvent = false;

            final StringBuilder eventDate = new StringBuilder();
            eventDate.append(hourFormatter.format(event.getDtstart()));
            if(event.getDtend() != null) {
                eventDate.append(" ");
                eventDate.append(hourFormatter.format(event.getDtend()));

                if(event.getDtstart().getDate() != event.getDtend().getDate() ||
                        event.getDtstart().getMonth() != event.getDtend().getMonth() ||
                        event.getDtstart().getYear() != event.getDtend().getYear()) {
                    fullDayEvent = true;
                    flowPanel.addStyleName("calendar-fullday-event");
                }
            }

            final InlineLabel dateLabel = new InlineLabel(eventDate.toString());
            dateLabel.addStyleName("calendar-event-date");

            final InlineLabel eventLabel = new InlineLabel(event.getSummary());
            eventLabel.addStyleName("calendar-event-label");

            if(fullDayEvent)
                flowPanel.addStyleName("calendar-fullday-event-" + event.getParent().getStyle());
            else
                eventLabel.addStyleName("calendar-event-" + event.getParent().getStyle());

            if(!fullDayEvent)
                flowPanel.add(dateLabel);
            flowPanel.add(eventLabel);

            final DecoratedPopupPanel detailPopup = new DecoratedPopupPanel(true);

                final Grid popupContent = new Grid(event.getParent().isEditable()?5:3, 1);
                popupContent.setText(0, 0, event.getSummary());
                popupContent.getCellFormatter().addStyleName(0, 0, "calendar-popup-header");

                if(!fullDayEvent) {
                    popupContent.getCellFormatter().addStyleName(1, 0, "calendar-popup-date");
                    popupContent.getCellFormatter().addStyleName(1, 0, "calendar-event-" + event.getParent().getStyle());
                    popupContent.setText(1, 0, eventDate.toString());
                }
                else
                    popupContent.setText(1, 0, "");

                if(event.getDescription() != null && !"".equals(event.getDescription())) {
                    popupContent.getCellFormatter().addStyleName(2, 0, "calendar-popup-description");
                    popupContent.setText(2, 0, event.getDescription());
                }
                else
                    popupContent.setText(2, 0, "");
                
                if(event.getParent().isEditable()) {
                    final Anchor editAnchor = new Anchor(I18N.CONSTANTS.calendarEditEvent());
                    editAnchor.addClickHandler(new ClickHandler() {
                    @Override
                        public void onClick(ClickEvent clickEvent) {
                            delegate.edit(event, CalendarWidget.this);
                        }
                    });

                    final Anchor deleteAnchor = new Anchor(I18N.CONSTANTS.calendarDeleteEvent());
                    deleteAnchor.addClickHandler(new ClickHandler() {
                    @Override
                        public void onClick(ClickEvent clickEvent) {
                            delegate.delete(event, CalendarWidget.this);
                            detailPopup.hide();
                        }
                    });

                    popupContent.setWidget(3, 0, editAnchor);
                    popupContent.setWidget(4, 0, deleteAnchor);
                }

            detailPopup.setWidget(popupContent);

            flowPanel.addClickHandler(new ClickHandler() {
                @Override
                public void onClick(ClickEvent event) {
                    final int left = flowPanel.getAbsoluteLeft() - 10;
                    final int bottom = Window.getClientHeight() - flowPanel.getAbsoluteTop();

                    detailPopup.setWidth((getCellWidth(CELL_DEFAULT_WIDTH)+20)+"px");

                    // Show the popup
                    detailPopup.setPopupPositionAndShow(new PositionCallback() {
                        @Override
                        public void setPosition(int offsetWidth, int offsetHeight) {
                            detailPopup.getElement().getStyle().setPropertyPx("left", left);
                            detailPopup.getElement().getStyle().setProperty("top", "");
                            detailPopup.getElement().getStyle().setPropertyPx("bottom", bottom);
                        }
                    });
                }
            });

            cell.add(flowPanel);
        }

        if(eventLimit != UNDEFINED && sortedEvents.size() > eventLimit) {
            final Anchor eventLabel = new Anchor("\u25BC");
            final Date thisDate = new Date(date.getTime());
            eventLabel.addClickHandler(new ClickHandler() {
                @Override
                public void onClick(ClickEvent event) {
                    startDate = thisDate;
                    setDisplayMode(displayMode.WEEK);
                }
            });
            eventLabel.addStyleName("calendar-event-limit");
            cell.add(eventLabel);
        }
    }

    /**
     * Returns the first date of the week that includes the given date.
     * @param day A date
     * @param firstDay The first day of the week (such as {@link #SUNDAY}, {@link #MONDAY} or anything else).
     * @return The first date of the week that includes <code>day</day>, as a {@link Date}.
     * @see #MONDAY
     * @see #TUESDAY
     * @see #WEDNESDAY
     * @see #THURSDAY
     * @see #FRIDAY
     * @see #SATURDAY
     * @see #SUNDAY
     */
    @SuppressWarnings("deprecation")
    private static Date getFirstDateOfWeek(Date day, int firstDay) {
        final int decal = (day.getDay()+7-firstDay)%7;
        return new Date(day.getYear(), day.getMonth(), day.getDate() - decal);
    }

    /**
     * Calculates the number of the week that includes the given date.
     * @param date A date
     * @param firstDay The first day of the week (such as {@link #SUNDAY}, {@link #MONDAY} or anything else).
     * @return The number of the week that includes <code>date</code>.
     */
    @SuppressWarnings("deprecation")
    private static int getWeekNumber(Date date, int firstDay) {
        int daysToThursday = 4 - date.getDay();

        if(date.getDay() < firstDay)
            daysToThursday -= 7;

        final Date thursday = new Date(date.getYear(), date.getMonth(), date.getDate() + daysToThursday);

        final Date januaryFourth = new Date(thursday.getYear(), 0, 4);
        final int daysToMonday = 1 - januaryFourth.getDay(); // Essayer avec le 1er jour de la semaine
        final Date monday = new Date(thursday.getYear(), 0, 4+daysToMonday);

        final double diff = Math.floor((thursday.getTime() - monday.getTime()) / (1000 * 60 * 60 * 24));
        return (int) Math.ceil(diff / 7.0);
    }
}
