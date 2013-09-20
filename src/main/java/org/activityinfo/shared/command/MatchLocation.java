package org.activityinfo.shared.command;

import java.util.Map;

import org.activityinfo.shared.dto.LocationDTO;

import com.google.common.collect.Maps;


public class MatchLocation implements Command<LocationDTO> {
    private int locationType;
    private Double latitude;
    private Double longitude;
    private String name;
    private Map<Integer, String> adminLevels = Maps.newHashMap();
    
    public int getLocationType() {
        return locationType;
    }
    public void setLocationType(int locationType) {
        this.locationType = locationType;
    }
    public Double getLatitude() {
        return latitude;
    }
    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }
    public Double getLongitude() {
        return longitude;
    }
    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Map<Integer, String> getAdminLevels() {
        return adminLevels;
    }
    public void setAdminLevels(Map<Integer, String> adminLevels) {
        this.adminLevels = adminLevels;
    }   
}
