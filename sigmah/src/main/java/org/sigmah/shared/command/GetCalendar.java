/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.shared.command;

import java.io.Serializable;
import org.sigmah.shared.domain.calendar.Calendar;

/**
 * Command used to ask for events.
 * @author Raphaël Calabro (rcalabro@ideia.fr)
 */
public class GetCalendar implements Command<Calendar> {
    public static enum Type {
        Activity,
        Personal,
        Dummy
    }

    private Type type;
    private Serializable identifier;

    public GetCalendar(Type type, Serializable identifier) {
        this.type = type;
        this.identifier = identifier;
    }

    public Type getType() {
        return type;
    }

    public Serializable getIdentifier() {
        return identifier;
    }
}
