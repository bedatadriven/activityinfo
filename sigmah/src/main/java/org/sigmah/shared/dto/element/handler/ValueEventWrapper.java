package org.sigmah.shared.dto.element.handler;

import java.io.Serializable;

import org.sigmah.shared.dto.element.FlexibleElementDTO;
import org.sigmah.shared.dto.element.handler.ValueEvent.ChangeType;

public class ValueEventWrapper implements Serializable {
    private static final long serialVersionUID = 8800087226429558970L;

    private FlexibleElementDTO sourceElement;
    private Serializable value;
    private ValueEvent.ChangeType changeType;

    public ValueEventWrapper() {
    }

    public FlexibleElementDTO getSourceElement() {
        return sourceElement;
    }

    public void setSourceElement(FlexibleElementDTO sourceElement) {
        this.sourceElement = sourceElement;
    }

    public Serializable getValue() {
        return value;
    }

    public void setValue(Serializable value) {
        this.value = value;
    }

    public ChangeType getChangeType() {
        return changeType;
    }

    public void setChangeType(ChangeType changeType) {
        this.changeType = changeType;
    }
}