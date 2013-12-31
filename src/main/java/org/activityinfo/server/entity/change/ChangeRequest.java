package org.activityinfo.server.entity.change;

import java.util.Set;

import org.activityinfo.shared.auth.AuthenticatedUser;

/**
 * Encapsulates a change request to a single entity
 * on behalf of an authenticated user.
 */
public interface ChangeRequest {

    AuthenticatedUser getRequestingUser();
    
    ChangeType getChangeType();
    
    String getEntityType();
    
    /**
     * @return the id of the entity to create, modify, or delete
     */
    String getEntityId();
    
    /**
     * 
     * @return set of names of new or updated properties
     */
    Set<String> getUpdatedProperties();
    
    /**
     * Gets the new/updated value for the entity's property named
     * {@code propertyName), converting if possible to {@code propertyClass}
     * 
     * @param propertyClass the Java type in which to return the value.
     * @param propertyName the name of the property to retrieve
     * @return the new value of the property 
     * @throws PropertyException if the ChangeRequest does not include
     * this property.
     */
    <T> T getPropertyValue(Class<T> propertyClass, String propertyName);
}
