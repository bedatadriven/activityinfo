/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */
package org.sigmah.server.endpoint.gwtrpc.handler;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import java.util.EnumMap;
import java.util.Map;
import org.sigmah.server.endpoint.gwtrpc.handler.calendar.ActivityCalendarHandler;
import org.sigmah.server.endpoint.gwtrpc.handler.calendar.CalendarHandler;
import org.sigmah.server.endpoint.gwtrpc.handler.calendar.MonitoredPointCalendarHandler;
import org.sigmah.server.endpoint.gwtrpc.handler.calendar.PersonalCalendarHandler;
import org.sigmah.shared.command.GetCalendar;
import org.sigmah.shared.command.handler.CommandHandler;
import org.sigmah.shared.command.result.CommandResult;
import org.sigmah.shared.domain.User;
import org.sigmah.shared.domain.calendar.CalendarType;
import org.sigmah.shared.exception.CommandException;

/**
 * Retrieves calendars and events.
 * 
 * @author Raphaël Calabro (rcalabro@ideia.fr)
 */
@Singleton
public class GetCalendarHandler implements CommandHandler<GetCalendar> {

    /**
     * List of converters. They convert model objects to <code>Calendar</code>s
     * and <code>Event</code>s objects.
     * 
     * @see CalendarHandler
     */
    private final Map<CalendarType, CalendarHandler> handlers;

    @Inject
    public GetCalendarHandler(Injector injector) {
        final EnumMap<CalendarType, CalendarHandler> map = new EnumMap<CalendarType, CalendarHandler>(
                CalendarType.class);
        map.put(CalendarType.Activity, injector.getInstance(ActivityCalendarHandler.class));
        map.put(CalendarType.Personal, injector.getInstance(PersonalCalendarHandler.class));
        map.put(CalendarType.MonitoredPoint, injector.getInstance(MonitoredPointCalendarHandler.class));

        handlers = map;
    }

    public Map<CalendarType, CalendarHandler> getHandlers() {
        return handlers;
    }

    @Override
    public CommandResult execute(GetCalendar cmd, User user) throws CommandException {
        final CalendarHandler handler = handlers.get(cmd.getType());
        return handler.getCalendar(cmd.getIdentifier());
    }
}
