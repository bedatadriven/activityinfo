/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.endpoint.gwtrpc.handler.calendar;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import org.sigmah.shared.domain.calendar.Calendar;
import org.sigmah.shared.domain.calendar.CalendarType;
import org.sigmah.shared.domain.calendar.Event;

/**
 *
 * @author Raphaël Calabro (rcalabro@ideia.fr)
 */
public class DummyCalendarHandler implements CalendarHandler {

    @Override
    public Calendar getCalendar(Serializable identifier) {
        final Calendar calendar = new Calendar();
        calendar.setType(CalendarType.Dummy);

        if(identifier != null)
            calendar.setName(identifier.toString());
        else
            calendar.setName("null");

        final HashMap<Date, List<Event>> events = new HashMap<Date, List<Event>>();
        events.put(new Date(110, 7, 27), Arrays.asList(new Event[]{ new Event("Weekly meeting", "Rien", new Date(110, 7, 27, 10, 5), new Date(110, 7, 27, 11, 0), calendar)}));
        events.put(new Date(110, 8, 1), Arrays.asList(new Event[]{new Event("Fill up the activity report", "Rien", new Date(110, 8, 1, 18, 0), null, calendar)}));
        events.put(new Date(110, 8, 3), Arrays.asList(new Event[]{new Event("Weekly meeting", "Rien", new Date(110, 8, 3, 10, 5), new Date(110, 8, 3, 11, 0), calendar)}));
        events.put(new Date(110, 8, 10), Arrays.asList(new Event[]{new Event("Weekly meeting", "Rien", new Date(110, 8, 10, 10, 5), new Date(110, 8, 10, 11, 0), calendar)}));
        events.put(new Date(110, 8, 17), Arrays.asList(new Event[]{new Event("Weekly meeting", "Rien", new Date(110, 8, 17, 10, 5), new Date(110, 8, 17, 11, 0), calendar)}));
        events.put(new Date(110, 8, 24), Arrays.asList(new Event[]{new Event("Weekly meeting", "Rien", new Date(110, 8, 24, 10, 5), new Date(110, 8, 24, 11, 0), calendar)}));
        events.put(new Date(110, 8, 28), Arrays.asList(new Event[]{new Event("Early duty", "Rien", new Date(110, 8, 28, 7, 30), new Date(110, 8, 28, 12, 0), calendar), new Event("After noon duty", "Rien", new Date(110, 8, 28, 14, 0), null, calendar), new Event("Late duty", "Rien", new Date(110, 8, 28, 17, 45), null, calendar), new Event("Midnight duty", "Rien", new Date(110, 8, 28, 23, 45), null, calendar)}));
        events.put(new Date(110, 9, 1), Arrays.asList(new Event[]{new Event("Weekly meeting", "Rien", new Date(110, 9, 1, 10, 5), new Date(110, 9, 1, 11, 0), calendar), new Event("Fill up the activity report", "Rien", new Date(110, 9, 1, 18, 0), null, calendar)}));
        events.put(new Date(110, 9, 8), Arrays.asList(new Event[]{new Event("Weekly meeting", "Rien", new Date(110, 9, 8, 10, 5), new Date(110, 9, 8, 11, 0), calendar)}));

        calendar.setEvents(events);

        return calendar;
    }

}
