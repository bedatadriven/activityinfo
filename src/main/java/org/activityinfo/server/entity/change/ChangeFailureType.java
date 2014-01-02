package org.activityinfo.server.entity.change;

/**
 * Specifies the reason for a change request's failure.
 */
public enum ChangeFailureType {

    /**
     * The entity to be modified or deleted could not be found
     */
    ENTITY_DOES_NOT_EXIST,
    
    /**
     * The requesting user was not authorized to effect the change
     */
    NOT_AUTHORIZED,
    
    /**
     * An unexpected error occurred during the change process.
     */
    SERVER_FAULT, 
    
    /**
     * The provided entity type was not valid
     */
    UNKNOWN_ENTITY_TYPE,
    
    /**
     * A required property is missing
     */
    REQUIRED_PROPERTY_MISSING, 
    
    /**
     * The type of the entity id provided by the change request 
     * did not match the declared entity type id and could not be
     * converted.
     */
    MALFORMED_ID, 
    
    /**
     * The type of a new value for a property was of the wrong type
     * and could not be converted.
     */
    MALFORMED_PROPERTY, 
    
    /**
     * The property referred to by the ChangeRequest does not exist
     */
    PROPERTY_DOES_NOT_EXIST,
    
    /**
     * The property referred to by the ChangeRequest may not be updated by the client
     */
    PROPERTY_NOT_UPDATABLE,
    
    
    /**
     * The new property value violated a constraint on the entity
     */
    CONSTRAINT_VIOLATION
}

