package org.sigmah.shared.command;

import java.util.List;

import org.sigmah.shared.command.result.RemindersResultList;
import org.sigmah.shared.dto.reminder.ReminderDTO;

public class UpdateReminders implements Command<RemindersResultList> {

    private static final long serialVersionUID = -7462687463958809651L;

    private List<ReminderDTO> list;

    public UpdateReminders() {
        // Serialization
    }

    public UpdateReminders(List<ReminderDTO> list) {
        this.list = list;
    }

    public List<ReminderDTO> getList() {
        return list;
    }

    public void setList(List<ReminderDTO> list) {
        this.list = list;
    }
}