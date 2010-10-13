/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.shared.domain.calendar;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.sigmah.shared.command.result.CommandResult;

/**
 *
 * @author Raphaël Calabro (rcalabro@ideia.fr)
 */
public class Calendar implements Serializable, CommandResult {
    private String name;
    private Map<Date, List<Event>> events;
    private Collection<Todo> tasks;

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public Map<Date, List<Event>> getEvents() {
        return events;
    }

    public void setEvents(Map<Date, List<Event>> events) {
        this.events = events;
    }

    public Collection<Todo> getTasks() {
        return tasks;
    }

    public void setTasks(Collection<Todo> tasks) {
        this.tasks = tasks;
    }
}
