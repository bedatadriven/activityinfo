package org.activityinfo.server.entity.change;

/**
 * Exception thrown when a property in the ChangeRequest cannot be
 * converted to the appropriate type.
 *
 */
public class PropertyConversionException extends PropertyException {

    public PropertyConversionException(String propertyName) {
        super(ChangeFailureType.MALFORMED_PROPERTY, propertyName);
    }

}
