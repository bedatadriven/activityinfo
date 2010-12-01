package org.sigmah.shared.command.result;

import java.util.List;

import org.sigmah.shared.dto.reminder.MonitoredPointDTO;

public class MonitoredPointsResultList implements CommandResult {

    private static final long serialVersionUID = 2395922480691898565L;

    private List<MonitoredPointDTO> list;

    public MonitoredPointsResultList() {
        // serialization
    }

    public MonitoredPointsResultList(List<MonitoredPointDTO> list) {
        this.list = list;
    }

    public List<MonitoredPointDTO> getList() {
        return list;
    }

    public void setList(List<MonitoredPointDTO> list) {
        this.list = list;
    }
}
