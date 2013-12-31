package org.activityinfo.server.entity.change;

/**
 * Thrown when a required property is missing from a ChangeRequest.
 */
public class MissingPropertyException extends PropertyException {

    public MissingPropertyException(String propertyName) {
        super(ChangeFailureType.REQUIRED_PROPERTY_MISSING, propertyName);
    }

}
