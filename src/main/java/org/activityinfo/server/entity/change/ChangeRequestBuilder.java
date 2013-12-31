package org.activityinfo.server.entity.change;

import java.util.Map;
import java.util.Set;

import org.activityinfo.server.database.hibernate.entity.User;
import org.activityinfo.shared.auth.AuthenticatedUser;

import com.extjs.gxt.ui.client.data.RpcMap;
import com.google.common.collect.Maps;


/**
 * Wraps a legacy Command as a ChangeRequest
 */
public class ChangeRequestBuilder implements ChangeRequest {

    private AuthenticatedUser user;
    private ChangeType changeType;
    private String entityType;
    private String entityId;
    private final Map<String, Object> properties = Maps.newHashMap();
    
    public ChangeRequestBuilder() {
        
        
    }
    
    public static ChangeRequestBuilder delete() {
        return new ChangeRequestBuilder().setChangeType(ChangeType.DELETE);
    }
    
    public AuthenticatedUser getUser() {
        return user;
    }

    public ChangeRequestBuilder setUser(AuthenticatedUser user) {
        this.user = user;
        return this;
    }

    public ChangeRequestBuilder setChangeType(ChangeType changeType) {
        this.changeType = changeType;
        return this;
    }

    public ChangeRequestBuilder setEntityType(String entityType) {
        this.entityType = entityType;
        return this;
    }

    public ChangeRequestBuilder setEntityId(int entityId) {
        return setEntityId(Integer.toString(entityId));
    }
    
    public ChangeRequestBuilder setEntityId(String entityId) {
        this.entityId = entityId;
        return this;
    }

    @Override
    public AuthenticatedUser getRequestingUser() {
        return user;
    }

    @Override
    public ChangeType getChangeType() {
        return changeType;
    }

    @Override
    public String getEntityType() {
        return entityType;
    }

    @Override
    public <T> T getPropertyValue(Class<T> propertyClass, String propertyName) {
        Object value = properties.get(propertyName);
        if(value == null) {
            throw new MissingPropertyException(propertyName);    
        }
        if(!value.getClass().equals(propertyClass)) {
            throw new PropertyConversionException(propertyName);
        }
        return (T)value;
    }
 
    @Override
    public Set<String> getUpdatedProperties() {
        return properties.keySet();
    }

    public ChangeRequestBuilder setProperty(String name, Object value) {
        properties.put(name, value);
        return this;
    }

    @Override
    public String getEntityId() {
        return entityId; 
    }

    public ChangeRequestBuilder setUser(User user) {
        return setUser(new AuthenticatedUser("xyz", user.getId(), user.getEmail()));
    }

    public ChangeRequestBuilder setProperties(Map<String, Object> map) {
        properties.putAll(map);
        return this;
    }
    
}
