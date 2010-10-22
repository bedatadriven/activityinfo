/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.policy;

import com.extjs.gxt.ui.client.widget.form.Time;
import com.google.inject.Inject;
import java.util.Date;
import org.sigmah.client.page.project.calendar.ProjectCalendarPresenter.CalendarWrapper;
import org.sigmah.server.dao.PersonalEventDAO;
import org.sigmah.shared.domain.User;
import org.sigmah.shared.domain.calendar.PersonalEvent;

/**
 *
 * @author Raphaël Calabro (rcalabro@ideia.fr)
 */
public class PersonalEventPolicy implements EntityPolicy<PersonalEvent> {
    final PersonalEventDAO dao;

    @Inject
    public PersonalEventPolicy(PersonalEventDAO dao) {
        this.dao = dao;
    }

    @Override
    public Object create(User user, PropertyMap properties) {
        final PersonalEvent event = new PersonalEvent();

        event.setDateCreated(new Date());
        fillEvent(event, properties);

        dao.persist(event);

        return event.getId();
    }

    @Override
    public void update(User user, Object entityId, PropertyMap changes) {
        final PersonalEvent event = dao.findById((Integer)entityId);
        fillEvent(event, changes);

        dao.merge(event);
    }

    private void fillEvent(PersonalEvent event, PropertyMap properties) {
        final CalendarWrapper calendar = (CalendarWrapper) properties.get("calendarId");
        event.setCalendarId((Integer) calendar.getCalendar().getIdentifier());
        
        event.setSummary((String) properties.get("summary"));
        event.setDescription((String) properties.get("description"));

        final Date day = (Date) properties.get("date");
        final Time startHour = (Time) properties.get("startDate");
        final Time endHour = (Time) properties.get("endDate");

        if(startHour != null) {
            event.setStartDate(new Date(day.getYear(), day.getMonth(), day.getDate(), startHour.getHour(), startHour.getMinutes()));
            if(endHour != null)
                event.setEndDate(new Date(day.getYear(), day.getMonth(), day.getDate(), endHour.getHour(), endHour.getMinutes()));
            else
                event.setEndDate(null);
        } else {
            event.setStartDate(new Date(day.getYear(), day.getMonth(), day.getDate()));
            event.setEndDate(new Date(day.getYear(), day.getMonth(), day.getDate()+1));
        }
    }

}
