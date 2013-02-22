

package org.activityinfo.shared.dto;

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

/**
 *
 * Partial DTO of the {@link org.activityinfo.server.database.hibernate.entity.Site Site} domain object and its
 * {@link org.activityinfo.server.database.hibernate.entity.Location Location} location that includes only
 * the id and geographic position.
 *
 * @author Alex Bertram
 */
public final class SitePointDTO implements DTO {

    private int siteId;
    private double y;
    private double x;
    
    private SitePointDTO() {
    }
    
    public SitePointDTO(int siteId, double x, double y) {
        this.y = y;
        this.x = x;
        this.siteId = siteId;
    }
    
    public static SitePointDTO fromSite(SiteDTO site) {
    	return new SitePointDTO(site.getId(), site.getX()== null ? 0: site.getX(), site.getY()==null? 0: site.getY());
    }

    public int getSiteId() {
        return siteId;
    }

    public void setSiteId(int siteId) {
        this.siteId = siteId;
    }

    /**
     * location.x
     * @return the x (longitudinal) coordinate of this Site
     */
    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    /**
     * location.y
     * @return  the y (latitudinal) coordinate of this Site
     */
    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }
}
