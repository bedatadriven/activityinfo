package org.activityinfo.shared.report.model;

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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activityinfo.shared.dto.SiteDTO;
import org.activityinfo.shared.geom.Rectangle;
import org.activityinfo.shared.report.content.AiLatLng;
import org.activityinfo.shared.report.content.PieMapMarker;
import org.activityinfo.shared.report.content.Point;

public final class PointValue {
    private SiteDTO site;
    private MapSymbol symbol;
    private double value;
    private Point px;
    private Rectangle iconRect;
    private List<PieMapMarker.SliceValue> slices;
    private Map<Integer, Integer> adminMembership = new HashMap<Integer, Integer>();

    public PointValue() {
    }

    public PointValue(SiteDTO site, MapSymbol symbol, double value, Point px) {
        this.site = site;
        this.setSymbol(symbol);
        this.setValue(value);
        this.setPx(px);
    }

    public PointValue(SiteDTO site, Point px, Rectangle iconRect) {
        this.site = site;
        this.setPx(px);
        this.setIconRect(iconRect);
        this.setValue(1);
    }

    public PointValue(SiteDTO site, Point px, Rectangle iconRect, double value) {
        this(site, px, iconRect);

        this.setValue(value);
    }

    public void setAdminMembership(Map<Integer, Integer> adminMembership) {
        this.adminMembership = adminMembership;
    }

    public Map<Integer, Integer> getAdminMembership() {
        return adminMembership;
    }

    public AiLatLng getLatLng() {
        if (site.hasLatLong()) {
            return new AiLatLng(site.getLatitude(), site.getLongitude());
        } else {
            return null;
        }
    }

    public boolean hasLatLng() {
        return getLatLng() != null;
    }

    public boolean hasPoint() {
        return getPx() != null;
    }

    public Point getPoint() {
        return getPx();
    }

    public SiteDTO getSite() {
        return site;
    }

    public void setSite(SiteDTO site) {
        this.site = site;
    }

    public MapSymbol getSymbol() {
        return symbol;
    }

    public void setSymbol(MapSymbol symbol) {
        this.symbol = symbol;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public Point getPx() {
        return px;
    }

    public void setPx(Point px) {
        this.px = px;
    }

    public Rectangle getIconRect() {
        return iconRect;
    }

    public void setIconRect(Rectangle iconRect) {
        this.iconRect = iconRect;
    }

    public List<PieMapMarker.SliceValue> getSlices() {
        return slices;
    }

    public void setSlices(List<PieMapMarker.SliceValue> slices) {
        this.slices = slices;
    }

}