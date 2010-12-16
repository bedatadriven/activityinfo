package org.sigmah.shared.dto.reminder;

import java.util.List;

import org.sigmah.shared.dto.EntityDTO;

import com.extjs.gxt.ui.client.data.BaseModelData;

/**
 * DTO mapping class for entity reminder.ReminderList.
 * 
 * @author tmi
 * 
 */
public class ReminderListDTO extends BaseModelData implements EntityDTO {

    private static final long serialVersionUID = 6658962865288286355L;

    @Override
    public String getEntityName() {
        return "reminder.ReminderList";
    }

    // Id.
    @Override
    public int getId() {
        final Integer id = (Integer) get("id");
        return id != null ? id : -1;
    }

    public void setId(int id) {
        set("id", id);
    }

    // Reminders
    public List<ReminderDTO> getReminders() {
        return get("reminders");
    }

    public void setReminders(List<ReminderDTO> reminders) {
        set("reminders", reminders);
    }
}