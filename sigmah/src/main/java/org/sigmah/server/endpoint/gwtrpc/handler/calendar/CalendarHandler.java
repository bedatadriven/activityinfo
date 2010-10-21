/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.endpoint.gwtrpc.handler.calendar;

import java.io.Serializable;
import org.sigmah.shared.domain.calendar.Calendar;

/**
 * Describes an utility class for fetching a type of Calendar.
 * @author Raphaël Calabro (rcalabro@ideia.fr)
 */
public interface CalendarHandler {
    Calendar getCalendar(Serializable identifier);
}
