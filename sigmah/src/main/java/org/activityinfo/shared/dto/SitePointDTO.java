package org.activityinfo.shared.dto;

/**
 *
 * Partial DTO of the {@link org.activityinfo.server.domain.Site Site} domain object and its
 * {@link org.activityinfo.server.domain.Location Location} location that includes only
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
