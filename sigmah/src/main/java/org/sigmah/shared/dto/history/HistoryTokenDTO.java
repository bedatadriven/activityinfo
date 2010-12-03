package org.sigmah.shared.dto.history;

import java.io.Serializable;

import org.sigmah.shared.dto.element.handler.ValueEvent.ChangeType;

public class HistoryTokenDTO implements Serializable {

    private static final long serialVersionUID = -2644629638564832900L;

    private String value;
    private ChangeType type;

    public HistoryTokenDTO() {
    }

    public HistoryTokenDTO(String value, ChangeType type) {
        this.value = value;
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public ChangeType getType() {
        return type;
    }

    public void setType(ChangeType type) {
        this.type = type;
    }

}
