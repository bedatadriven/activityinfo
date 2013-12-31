package org.activityinfo.shared.command;

import org.activityinfo.shared.command.result.VoidResult;
import org.activityinfo.shared.dto.EntityDTO;
import org.activityinfo.shared.dto.ProjectDTO;

import com.extjs.gxt.ui.client.data.RpcMap;

public class RequestChange implements Command<VoidResult> {

    public static final String DELETE = "DELETE";
    public static final String UPDATE = "UPDATE";

    
    private String entityType;
    private String entityId;
    private String changeType;
    private RpcMap propertyMap;
    
    public String getEntityType() {
        return entityType;
    }
    
    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }
    
    public String getEntityId() {
        return entityId;
    }
    
    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    private void setEntityId(int id) {
        setEntityId(Integer.toString(id));
    }
    
    public String getChangeType() {
        return changeType;
    }
    
    public void setChangeType(String changeType) {
        this.changeType = changeType;
    }
    
    public RpcMap getPropertyMap() {
        return propertyMap;
    }
    
    public void setPropertyMap(RpcMap propertyMap) {
        this.propertyMap = propertyMap;
    }
    
    public static RequestChange delete(EntityDTO entity) {
        RequestChange request = new RequestChange();
        request.setChangeType(DELETE);
        request.setEntityId(entity.getId());
        request.setEntityType(entity.getEntityName());
        return request;
    }

    public static RequestChange delete(String entityType, int id) {
        RequestChange request = new RequestChange();
        request.setChangeType(DELETE);
        request.setEntityId(id);
        request.setEntityType(entityType);
        return request;
    }

    public static Command update(EntityDTO model, String... propertiesToChange) {
        RequestChange request = new RequestChange();
        request.setChangeType(UPDATE);
        request.setEntityId(model.getId());
        request.setEntityType(model.getEntityName());
        
        RpcMap map = new RpcMap();
        for(String property : propertiesToChange) {
            map.put(property, model.get(property));
        }
        request.setPropertyMap(map);
        
        return request;
    }
}
