package org.sigmah.shared.dto.element.handler;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.sigmah.shared.dto.element.FlexibleElementDTO;

public class ValueEventWrapper implements Serializable {
    private static final long serialVersionUID = 8800087226429558970L;

    private FlexibleElementDTO sourceElement;
    private Serializable value;
    private ArrayList<Serializable> values;

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

    public List<Serializable> getValues() {
        return values;
    }

    public void setValues(Collection<Serializable> values) {
        if(values != null)
            this.values = new ArrayList<Serializable>(values);
        else
            this.values = null;
    }
}