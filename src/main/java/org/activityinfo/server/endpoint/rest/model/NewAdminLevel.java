package org.activityinfo.server.endpoint.rest.model;

import java.util.List;

public class NewAdminLevel {
    private String name;
    private List<NewAdminEntity> entities;
    
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public List<NewAdminEntity> getEntities() {
        return entities;
    }
    public void setEntities(List<NewAdminEntity> entities) {
        this.entities = entities;
    }
  
}
