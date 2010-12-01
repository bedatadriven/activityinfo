package org.sigmah.shared.dto.reminder;

import java.util.List;

import org.sigmah.shared.dto.EntityDTO;

import com.extjs.gxt.ui.client.data.BaseModelData;

/**
 * DTO mapping class for entity reminder.MonitoredPointList.
 * 
 * @author tmi
 * 
 */
public class MonitoredPointListDTO extends BaseModelData implements EntityDTO {

    private static final long serialVersionUID = 1001655394559887157L;

    @Override
    public String getEntityName() {
        return "reminder.MonitoredPointList";
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

    // Monitored points
    public List<MonitoredPointDTO> getPoints() {
        return get("points");
    }

    public void setPoints(List<MonitoredPointDTO> points) {
        set("points", points);
    }
}
