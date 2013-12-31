package org.activityinfo.server.entity.change;

public abstract class PropertyException extends ChangeException {

    private String propertyName;

    public PropertyException(ChangeFailureType type, String propertyName) {
        super(type);
        this.propertyName = propertyName;
    }
    
}
