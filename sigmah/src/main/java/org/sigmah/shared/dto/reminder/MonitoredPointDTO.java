package org.sigmah.shared.dto.reminder;

import java.util.Date;

import org.sigmah.shared.domain.value.File;
import org.sigmah.shared.dto.EntityDTO;
import org.sigmah.shared.dto.value.FileDTO;

import com.extjs.gxt.ui.client.data.BaseModelData;

/**
 * DTO mapping class for entity reminder.MonitoredPoint.
 * 
 * @author tmi
 * 
 */
public class MonitoredPointDTO extends BaseModelData implements EntityDTO {

    private static final long serialVersionUID = 1259632326368572850L;

    @Override
    public String getEntityName() {
        return "reminder.MonitoredPoint";
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

    // File
    public FileDTO getFile() {
        return get("file");
    }

    public void setFile(File file) {
        set("file", file);
    }

    public boolean isCompleted() {
        return getCompletionDate() != null;
    }

    @Override
    public boolean equals(Object obj) {

        if (obj == null) {
            return false;
        }

        if (!(obj instanceof MonitoredPointDTO)) {
            return false;
        }

        final MonitoredPointDTO other = (MonitoredPointDTO) obj;

        return getId() == other.getId();
    }
}
