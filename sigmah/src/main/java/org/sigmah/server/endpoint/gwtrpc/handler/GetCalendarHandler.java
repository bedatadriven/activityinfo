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
import org.sigmah.shared.exception.CommandException;

/**
 *
 * @author Raphaël Calabro (rcalabro@ideia.fr)
 */
public class GetCalendarHandler implements CommandHandler<GetCalendar> {
    private final Map<GetCalendar.Type, CalendarHandler> handlers;

    @Inject
    public GetCalendarHandler(Injector injector) {
        final EnumMap<GetCalendar.Type, CalendarHandler> map = new EnumMap<GetCalendar.Type, CalendarHandler>(GetCalendar.Type.class);
        map.put(GetCalendar.Type.Dummy, injector.getInstance(DummyCalendarHandler.class));
        map.put(GetCalendar.Type.Personal, injector.getInstance(PersonalCalendarHandler.class));

        handlers = map;
    }

    @Override
    public CommandResult execute(GetCalendar cmd, User user) throws CommandException {
        final CalendarHandler handler = handlers.get(cmd.getType());
        return handler.getCalendar(cmd.getIdentifier());
    }
}
