package org.sigmah.shared.command;

import java.util.List;

import org.sigmah.shared.command.result.MonitoredPointsResultList;
import org.sigmah.shared.dto.reminder.MonitoredPointDTO;

public class UpdateMonitoredPoints implements Command<MonitoredPointsResultList> {

    private static final long serialVersionUID = -7462687463958809651L;

    private List<MonitoredPointDTO> list;

    public UpdateMonitoredPoints() {
        // Serialization
    }

    public UpdateMonitoredPoints(List<MonitoredPointDTO> list) {
        this.list = list;
    }

    public List<MonitoredPointDTO> getList() {
        return list;
    }

    public void setList(List<MonitoredPointDTO> list) {
        this.list = list;
    }
}
