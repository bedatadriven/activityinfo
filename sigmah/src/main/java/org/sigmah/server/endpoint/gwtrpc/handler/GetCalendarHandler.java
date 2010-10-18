/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */
package org.sigmah.server.endpoint.gwtrpc.handler;

import com.google.inject.Inject;
import com.google.inject.Injector;
import java.util.EnumMap;
import java.util.Map;
import org.sigmah.server.endpoint.gwtrpc.handler.calendar.CalendarHandler;
import org.sigmah.server.endpoint.gwtrpc.handler.calendar.DummyCalendarHandler;
import org.sigmah.server.endpoint.gwtrpc.handler.calendar.PersonalCalendarHandler;
import org.sigmah.shared.command.GetCalendar;
import org.sigmah.shared.command.handler.CommandHandler;
import org.sigmah.shared.command.result.CommandResult;
import org.sigmah.shared.domain.User;
import org.sigmah.shared.domain.calendar.CalendarType;
import org.sigmah.shared.exception.CommandException;

/**
 *
 * @author Raphaël Calabro (rcalabro@ideia.fr)
 */
public class GetCalendarHandler implements CommandHandler<GetCalendar> {
    private final Map<CalendarType, CalendarHandler> handlers;

    @Inject
    public GetCalendarHandler(Injector injector) {
        final EnumMap<CalendarType, CalendarHandler> map = new EnumMap<CalendarType, CalendarHandler>(CalendarType.class);
        map.put(CalendarType.Dummy, injector.getInstance(DummyCalendarHandler.class));
        map.put(CalendarType.Personal, injector.getInstance(PersonalCalendarHandler.class));

        handlers = map;
    }

    @Override
    public CommandResult execute(GetCalendar cmd, User user) throws CommandException {
        final CalendarHandler handler = handlers.get(cmd.getType());
        return handler.getCalendar(cmd.getIdentifier());
    }
}
