/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.endpoint.gwtrpc.handler.calendar;

import com.google.inject.Inject;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.persistence.Query;
import javax.persistence.EntityManager;
import org.sigmah.shared.domain.calendar.Calendar;
import org.sigmah.shared.domain.calendar.CalendarType;
import org.sigmah.shared.domain.calendar.Event;
import org.sigmah.shared.domain.calendar.PersonalCalendar;
import org.sigmah.shared.domain.calendar.PersonalEvent;

/**
 *
 * @author Raphaël Calabro (rcalabro@ideia.fr)
 */
public class PersonalCalendarHandler implements CalendarHandler {
    private EntityManager em;

    @Inject
    public PersonalCalendarHandler(EntityManager em) {
        this.em = em;
    }

    private Date normalize(Date date) {
        return new Date(date.getYear(), date.getMonth(), date.getDate());
    }

    @Override
    public Calendar getCalendar(Serializable identifier) {
        if(!(identifier instanceof Integer))
            throw new IllegalArgumentException();

        // Fetching the calendar
        final Integer id = (Integer) identifier;
        final Query calendarQuery = em.createQuery("SELECT c FROM PersonalCalendar c WHERE c.id = :calendarId");
        calendarQuery.setParameter("calendarId", id);

        final PersonalCalendar personalCalendar = (PersonalCalendar) calendarQuery.getSingleResult();

        // Fetching the events
        final Query eventQuery = em.createQuery("SELECT p FROM PersonalEvent p WHERE p.calendarId = :calendarId");
        eventQuery.setParameter("calendarId", id);

        final List<PersonalEvent> events = eventQuery.getResultList();

        final Calendar calendar = new Calendar();
        calendar.setType(CalendarType.Personal);
        calendar.setName(personalCalendar.getName());
        calendar.setIdentifier(identifier);
        calendar.setEditable(true);

        if(events != null) {
            final HashMap<Date, List<Event>> eventMap = new HashMap<Date, List<Event>>();

            for(final PersonalEvent event : events) {
                final Date key = normalize(event.getStartDate());

                List<Event> list = eventMap.get(key);
                if(list == null) {
                    list = new ArrayList<Event>();
                    eventMap.put(key, list);
                }
                
                final Event calendarEvent = new Event();
                calendarEvent.setIdentifier(event.getId());
                calendarEvent.setParent(calendar);
                calendarEvent.setSummary(event.getSummary());
                calendarEvent.setDescription(event.getDescription());
                calendarEvent.setDtstart(new Date(event.getStartDate().getTime()));
                if(event.getEndDate() != null)
                    calendarEvent.setDtend(new Date(event.getEndDate().getTime()));
                
                list.add(calendarEvent);
            }

            calendar.setEvents(eventMap);
        }

        return calendar;
    }
    
}
