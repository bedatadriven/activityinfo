package org.activityinfo.shared.dto;

/**
 * @author Alex Bertram (akbertram@gmail.com)
 */
public class SitePoint implements DTO {

    private double lat;
    private double lng;
    private int siteId;

    private SitePoint() {

    }

    public SitePoint(double lat, double lng, int siteId) {
        this.lat = lat;
        this.lng = lng;
        this.siteId = siteId;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public int getSiteId() {
        return siteId;
    }

    public void setSiteId(int siteId) {
        this.siteId = siteId;
    }
}
