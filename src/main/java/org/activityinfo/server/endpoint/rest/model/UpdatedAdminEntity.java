package org.activityinfo.server.endpoint.rest.model;

import org.activityinfo.server.database.hibernate.entity.Bounds;

public class UpdatedAdminEntity {
    private int id;
    private Integer parentId;
    private String name;
    private String code;
    private Bounds bounds;
    
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public Integer getParentId() {
        return parentId;
    }
    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    public Bounds getBounds() {
        return bounds;
    }
    public void setBounds(Bounds bounds) {
        this.bounds = bounds;
    }
    
    
    
}
