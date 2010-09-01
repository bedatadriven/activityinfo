package org.sigmah.shared.dto.element.handler;

import java.io.Serializable;
import java.util.List;

import org.sigmah.client.page.project.ProjectPresenter;
import org.sigmah.shared.dto.element.FlexibleElementDTO;

import com.google.gwt.event.shared.GwtEvent;

/**
 * Event transmitted to the {@link ProjectPresenter} when a flexible element
 * value changes.
 * 
 * @author Denis Colliot (dcolliot@ideia.fr)
 * 
 */
public class ValueEvent extends GwtEvent<ValueHandler> implements Serializable {
    private final static GwtEvent.Type<ValueHandler> TYPE = new GwtEvent.Type<ValueHandler>();

    private FlexibleElementDTO sourceElement;
    private Serializable value;
    private List<Serializable> values;
    private ChangeType changeType;

    public ValueEvent(FlexibleElementDTO sourceElement, Serializable value, ChangeType changeType) {
        this.sourceElement = sourceElement;
        this.value = value;
        this.changeType = changeType;
    }

    // Not used ?
    public ValueEvent(FlexibleElementDTO sourceElement, List<Serializable> values) {
        this.sourceElement = sourceElement;
        this.values = values;
        this.changeType = null;
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

    public List<Serializable> getValues() {
        return values;
    }

    public void setValues(List<Serializable> values) {
        this.values = values;
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

    public static enum ChangeType {
        ADD, REMOVE, EDIT;
    }
}
