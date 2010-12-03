package org.sigmah.shared.dto.element.handler;

import java.io.Serializable;

import org.sigmah.shared.dto.element.FlexibleElementDTO;
import org.sigmah.shared.dto.element.handler.ValueEvent.ChangeType;
import org.sigmah.shared.dto.value.ListEntityDTO;

public class ValueEventWrapper implements Serializable {

    private static final long serialVersionUID = 8800087226429558970L;

    private FlexibleElementDTO sourceElement;
    private ListEntityDTO listValue;
    private String singleValue;
    private ValueEvent.ChangeType changeType;

    public ValueEventWrapper() {
    }

    public FlexibleElementDTO getSourceElement() {
        return sourceElement;
    }

    public void setSourceElement(FlexibleElementDTO sourceElement) {
        this.sourceElement = sourceElement;
    }

    public ChangeType getChangeType() {
        return changeType;
    }

    public void setChangeType(ChangeType changeType) {
        this.changeType = changeType;
    }

    public ListEntityDTO getListValue() {
        return listValue;
    }

    public void setListValue(ListEntityDTO listValue) {
        this.listValue = listValue;
    }

    public String getSingleValue() {
        return singleValue;
    }

    public void setSingleValue(String singleValue) {
        this.singleValue = singleValue;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((changeType == null) ? 0 : changeType.hashCode());
        result = prime * result + ((listValue == null) ? 0 : listValue.hashCode());
        result = prime * result + ((singleValue == null) ? 0 : singleValue.hashCode());
        result = prime * result + ((sourceElement == null) ? 0 : sourceElement.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ValueEventWrapper other = (ValueEventWrapper) obj;
        if (changeType != other.changeType)
            return false;
        if (listValue == null) {
            if (other.listValue != null)
                return false;
        } else if (!listValue.equals(other.listValue))
            return false;
        if (singleValue == null) {
            if (other.singleValue != null)
                return false;
        } else if (!singleValue.equals(other.singleValue))
            return false;
        if (sourceElement == null) {
            if (other.sourceElement != null)
                return false;
        } else if (!sourceElement.equals(other.sourceElement))
            return false;
        return true;
    }
}