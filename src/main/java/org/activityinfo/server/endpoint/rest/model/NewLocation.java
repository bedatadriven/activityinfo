package org.activityinfo.server.endpoint.rest.model;

import java.util.List;

public class NewLocation {
    private String name;
    private double latitude;
    private double longitude;
    private List<Integer> adminEntityIds;
    
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public double getLatitude() {
        return latitude;
    }
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
    public double getLongitude() {
        return longitude;
    }
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
    public List<Integer> getAdminEntityIds() {
        return adminEntityIds;
    }
    public void setAdminEntityIds(List<Integer> adminEntityIds) {
        this.adminEntityIds = adminEntityIds;
    }
    
}
