package org.sigmah.shared.dto.element.handler;

import java.io.Serializable;

import org.sigmah.client.page.project.ProjectPresenter;
import org.sigmah.shared.dto.element.FlexibleElementDTO;
import org.sigmah.shared.dto.value.ListEntityDTO;

import com.google.gwt.event.shared.GwtEvent;

/**
 * Event transmitted to the {@link ProjectPresenter} when a flexible element
 * value changes.
 * 
 * @author Denis Colliot (dcolliot@ideia.fr)
 * 
 */
public class ValueEvent extends GwtEvent<ValueHandler> implements Serializable {

    private static final long serialVersionUID = -6920472009097129066L;

    private final static GwtEvent.Type<ValueHandler> TYPE = new GwtEvent.Type<ValueHandler>();

    private FlexibleElementDTO sourceElement;
    private Serializable value;
    // Only used for the elements part of a list.
    private ChangeType changeType;

    public ValueEvent(FlexibleElementDTO sourceElement, Serializable value) {
        this.sourceElement = sourceElement;
        this.value = value;
        if (value instanceof ListEntityDTO) {
            this.changeType = ChangeType.ADD;
        }
    }

    public ValueEvent(FlexibleElementDTO sourceElement, Serializable value, ChangeType changeType) {
        this.sourceElement = sourceElement;
        this.value = value;
        if (value instanceof ListEntityDTO) {
            if (changeType == null) {
                this.changeType = ChangeType.ADD;
            } else {
                this.changeType = changeType;
            }
        }
    }

    @Override
    protected void dispatch(ValueHandler handler) {
        handler.onValueChange(this);
    }

    @Override
    public GwtEvent.Type<ValueHandler> getAssociatedType() {
        return TYPE;
    }

    public static Type<ValueHandler> getType() {
        return TYPE;
    }

    public Serializable getValue() {
        return value;
    }

    public void setValue(Serializable value) {
        this.value = value;
    }

    public void setSourceElement(FlexibleElementDTO sourceElement) {
        this.sourceElement = sourceElement;
    }

    public FlexibleElementDTO getSourceElement() {
        return sourceElement;
    }

    public ChangeType getChangeType() {
        return changeType;
    }

    public void setChangeType(ChangeType changeType) {
        this.changeType = changeType;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("ValueEvent [");
        sb.append("source=#");
        sb.append(sourceElement.getId());
        sb.append(",value=");
        sb.append(value);
        sb.append(",changeType=");
        sb.append(changeType);
        sb.append("]\n");
        return sb.toString();
    }

    public static enum ChangeType {
        ADD, REMOVE, EDIT;
    }
}
