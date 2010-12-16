package org.sigmah.shared.command.result;

import java.util.List;

import org.sigmah.shared.dto.reminder.ReminderDTO;

public class RemindersResultList implements CommandResult {

    private static final long serialVersionUID = 2395922480691898565L;

    private List<ReminderDTO> list;

    public RemindersResultList() {
        // serialization
    }

    public RemindersResultList(List<ReminderDTO> list) {
        this.list = list;
    }

    public List<ReminderDTO> getList() {
        return list;
    }

    public void setList(List<ReminderDTO> list) {
        this.list = list;
    }
}
