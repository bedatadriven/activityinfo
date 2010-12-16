package org.sigmah.shared.dto.reminder;

import java.util.Date;

import org.sigmah.shared.dto.EntityDTO;

import com.extjs.gxt.ui.client.data.BaseModelData;

/**
 * DTO mapping class for entity reminder.Reminder.
 * 
 * @author tmi
 * 
 */
public class ReminderDTO extends BaseModelData implements EntityDTO {

    private static final long serialVersionUID = 6733872261309621888L;

    @Override
    public String getEntityName() {
        return "reminder.Reminder";
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

    // Label
    public String getLabel() {
        return get("label");
    }

    public void setLabel(String label) {
        set("label", label);
    }

    // Expected date
    public Date getExpectedDate() {
        return get("expectedDate");
    }

    public void setExpectedDate(Date expectedDate) {
        set("expectedDate", expectedDate);
    }

    // Completion date
    public Date getCompletionDate() {
        return get("completionDate");
    }

    public void setCompletionDate(Date completionDate) {
        set("completionDate", completionDate);
        setIsCompleted();
    }

    public void setIsCompleted() {
        set("completed", getCompletionDate() != null);
    }

    public boolean getIsCompleted() {
        return (Boolean) get("completed");
    }

    public boolean isCompleted() {
        return getCompletionDate() != null;
    }

    @Override
    public boolean equals(Object obj) {

        if (obj == null) {
            return false;
        }

        if (!(obj instanceof ReminderDTO)) {
            return false;
        }

        final ReminderDTO other = (ReminderDTO) obj;

        return getId() == other.getId();
    }
}
