package org.activityinfo.shared.report.content;

/*
 * #%L
 * ActivityInfo Server
 * %%
 * Copyright (C) 2009 - 2013 UNICEF
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

import com.google.common.base.Joiner;
import com.google.common.collect.Maps;

public class AdminOverlay implements Serializable {

    private Map<Integer, AdminMarker> polygons;
    private int adminLevelId;
    private String outlineColor;

    public AdminOverlay() {

    }

    public AdminOverlay(int adminLevelId) {
        super();
        this.adminLevelId = adminLevelId;
        this.polygons = Maps.newHashMap();
    }

    public AdminMarker getPolygon(int adminEntityId) {
        return polygons.get(adminEntityId);
    }

    public Collection<AdminMarker> getPolygons() {
        return polygons.values();
    }

    public void setPolygons(Map<Integer, AdminMarker> polygons) {
        this.polygons = polygons;
    }

    public int getAdminLevelId() {
        return adminLevelId;
    }

    public void setAdminLevelId(int adminLevelId) {
        this.adminLevelId = adminLevelId;
    }

    public void addPolygon(AdminMarker polygon) {
        polygons.put(polygon.getAdminEntityId(), polygon);
    }

    public String getOutlineColor() {
        return outlineColor;
    }

    public void setOutlineColor(String outlineColor) {
        this.outlineColor = outlineColor;
    }

    @Override
    public String toString() {
        return Joiner.on("\n").join(polygons.values());
    }

}
