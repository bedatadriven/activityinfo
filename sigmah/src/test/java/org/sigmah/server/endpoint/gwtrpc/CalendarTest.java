/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.endpoint.gwtrpc;

import com.google.inject.Inject;
import java.util.Date;
import java.util.List;
import java.util.Map;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.sigmah.server.dao.OnDataSet;
import org.sigmah.server.endpoint.gwtrpc.handler.GetCalendarHandler;
import org.sigmah.server.endpoint.gwtrpc.handler.calendar.CalendarHandler;
import org.sigmah.server.endpoint.gwtrpc.handler.calendar.DummyCalendarHandler;
import org.sigmah.shared.command.GetCalendar;
import org.sigmah.shared.domain.User;
import org.sigmah.shared.domain.calendar.Calendar;
import org.sigmah.shared.domain.calendar.CalendarType;
import org.sigmah.shared.domain.calendar.Event;
import org.sigmah.shared.exception.CommandException;
import org.sigmah.test.InjectionSupport;

/**
 *
 * @author Raphaël Calabro (rcalabro@ideia.fr)
 */
@RunWith(InjectionSupport.class)
public class CalendarTest extends CommandTestCase {
    @Inject
    GetCalendarHandler getCalendarHandler;

    @Inject
    DummyCalendarHandler dummyCalendarHandler;

    @Before
    public void configure() {
        final Map<CalendarType, CalendarHandler> map = getCalendarHandler.getHandlers();
        map.put(CalendarType.Dummy, dummyCalendarHandler);
    }

    @Test
    public void testTypeService() throws CommandException {
        User user = new User();
        user.setId(1);
        user.setName("Sigman");

        final GetCalendar command = new GetCalendar(CalendarType.Dummy, "dummy");
        Calendar result = (Calendar) getCalendarHandler.execute(command, user);

        // Verifying the fetch
        Assert.assertNotNull(result);
        Assert.assertEquals(result.getName(), "dummy");

        // Verifying the number of event lists
        Assert.assertEquals(result.getEvents().size(), 9);

        // No events are defined for this date
        List<Event> nullList = result.getEvents().get(new Date(110, 7, 7));
        Assert.assertNull(nullList);

        // Verifying the content of one of the event lists
        List<Event> events = result.getEvents().get(new Date(110, 8, 28));
        Assert.assertNotNull(events);
        Assert.assertEquals(events.size(), 4);
    }

    @Test
    @OnDataSet("/dbunit/calendar.db.xml")
    public void testPersonalCalendar() throws CommandException {
        User user = new User();
        user.setId(1);
        user.setName("Sigman");

        final GetCalendar command = new GetCalendar(CalendarType.Personal, Integer.valueOf(1));
        Calendar result = (Calendar) getCalendarHandler.execute(command, user);

        // Verifying the fetch
        Assert.assertNotNull(result);
        Assert.assertEquals(result.getName(), "debug-calendar");

        // Verifying the number of event lists
        Assert.assertEquals(result.getEvents().size(), 2);

        // Verifying one of the event lists
        List<Event> events = result.getEvents().get(new Date(110, 9, 18));
        Assert.assertNotNull(events);
        Assert.assertEquals(events.size(), 2);

        Event event = events.get(1);
        Assert.assertNotNull(event);
        Assert.assertEquals(event.getSummary(), "Second");
    }
}
