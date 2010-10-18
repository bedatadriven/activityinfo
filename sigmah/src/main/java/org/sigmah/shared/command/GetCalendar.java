/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.shared.command;

import java.io.Serializable;
import org.sigmah.shared.domain.calendar.Calendar;
import org.sigmah.shared.domain.calendar.CalendarType;

/**
 * Command used to ask for events.
 * @author Raphaël Calabro (rcalabro@ideia.fr)
 */
public class GetCalendar implements Command<Calendar> {
    private CalendarType type;
    private Serializable identifier;

    public GetCalendar() {}

    public GetCalendar(CalendarType type, Serializable identifier) {
        this.type = type;
        this.identifier = identifier;
    }

    public CalendarType getType() {
        return type;
    }

    public void setType(CalendarType type) {
        this.type = type;
    }


    public Serializable getIdentifier() {
        return identifier;
    }

    public void setIdentifier(Serializable identifier) {
        this.identifier = identifier;
    }
}
