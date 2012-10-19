/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.shared.util.mapping;

import java.io.Serializable;

import org.activityinfo.shared.report.content.AiLatLng;


/**
 * One-to-one DTO for the {@link org.activityinfo.server.database.hibernate.entity.Bounds} domain object.
 * 
 * @author Alex Bertram
 */
public final class BoundingBoxDTO implements Serializable {
    private static final int LAT_MAX = 90;
	private static final int LNG_MAX = 180;
	private static final int LAT_MIN = -LAT_MAX;
	private static final int LNG_MIN = -180;
	
	private double minLon;
    private double minLat;
    private double maxLon;
    private double maxLat;

    /**
     * 
     * @return an empty bounding box (180, 90, -180, -90)
     */
    public static BoundingBoxDTO empty() {
    	return new BoundingBoxDTO();
    }
    
    /**
     *    
     * @return maximum geographic bounds (-180, -90, 180, 90)
     */
    public static BoundingBoxDTO maxGeoBounds() {
    	return new BoundingBoxDTO(LNG_MIN, LNG_MAX, LAT_MIN, LAT_MAX);
    }
    
    private BoundingBoxDTO() {
        this.minLon = LNG_MAX;
        this.minLat = LAT_MAX;
        this.maxLon = LNG_MIN;
        this.maxLat = LAT_MIN;
    }

    /**
     * 
     * @param x1
     *            Minimum x value (most westernly longitude)
     * @param x2
     *            Maximum x value (most easternly longitude)
     * @param y1
     *            Minimum y value (most southernly latitude)
     * @param y2
     *            Maximum y value (most northernly latitude)
     */
    public BoundingBoxDTO(double x1, double x2, double y1, double y2) {
        this.minLon = x1;
        this.minLat = y1;
        this.maxLon = x2;
        this.maxLat = y2;
    }

    public static BoundingBoxDTO create(double x1, double y1, double x2, double y2) {
    	return new BoundingBoxDTO(x1,x2,y1,y2);
    }
    
    /**
     * Constructs a copy of the given BoundingBoxDTO
     * 
     * @param bounds
     *            the instance to copy
     */
    public BoundingBoxDTO(BoundingBoxDTO bounds) {
        this.minLon = bounds.getMinLon();
        this.minLat = bounds.getMinLat();
        this.maxLon = bounds.getMaxLon();
        this.maxLat = bounds.getMaxLat();
    }

    /**
     * Grows this BoundingBoxDTO to include the point at X, Y
     */
    public void grow(double x, double y) {
        if (x < minLon) {
            minLon = x;
        }
        if (x > maxLon) {
            maxLon = x;
        }
        if (y < minLat) {
            minLat = y;
        }
        if (y > maxLat) {
            maxLat = y;
        }
    }

    /**
     * Grows this BoundingBoxDTO to include the given bounds
     */
	public void grow(BoundingBoxDTO bounds) {
		if(bounds.minLon < minLon) {
			this.minLon = bounds.minLon;
		}
		if(bounds.minLat < minLat) {
			this.minLat = bounds.minLat;
		}
		if(bounds.maxLon > maxLon) {
			this.maxLon = bounds.maxLon;
		}
		if(bounds.maxLat > maxLat) {
			this.maxLat = bounds.maxLat;
		}
	}
    
    /**
     * 
     * @return true if the BoundingBoxDTO is empty
     */
    public boolean isEmpty() {
        return minLon > maxLon || minLat > maxLat;
    }

    /**
     * @return the x (longitude) coordinate of the BoundingBoxDTO's centroid,
     *         (x1+x2)/2
     */
    public double getCenterX() {
        return (minLon + maxLon) / 2;
    }

    /**
     * 
     * @return the y (latitudinal) coordinate of the BoundingBoxDTO's centroid,
     *         (y1+y2)/2
     */
    public double getCenterY() {
        return (minLat + maxLat) / 2;
    }

    /**
     * Calculates the intersection of this BoundingBoxDTO with given
     * BoundingBoxDTO
     * 
     * @param b
     *            another BoundingBoxDTO with which to intersect this
     *            BoundingBoxDTO
     * @return the intersection of the two BoundingBoxDTOs
     */
    public BoundingBoxDTO intersect(BoundingBoxDTO b) {
        return new BoundingBoxDTO(Math.max(minLon, b.minLon), Math.min(maxLon, b.maxLon), Math.max(minLat, b.minLat), Math.min(maxLat, b.maxLat));
    }

    /**
     * @return true if this BoundingBoxDTO intersects with <code>b</code>
     */
    public boolean intersects(BoundingBoxDTO b) {
        return !(b.maxLon < minLon || b.minLon > maxLon || b.maxLat < minLat || b.minLat > maxLat);
    }

    /**
     * 
     * @param b
     * @return true if this BoundingBoxDTO contains <code>b</code>
     */
    public boolean contains(BoundingBoxDTO b) {
        return b.minLon >= minLon && b.maxLon <= maxLon && b.minLat >= minLat && b.maxLat <= maxLat;
    }

	public boolean contains(AiLatLng center) {
		return contains(center.getLng(), center.getLat());
	}

    
    /**
     * 
     * @return true if this BoundingBoxDTO contains the point at (x,y)
     */
    public boolean contains(double x, double y) {
        return x >= minLon && x <= maxLon && y >= minLat && y <= maxLat;
    }

    /**
     * Clamps the given x coordinate to this BoundingBoxDTO's limits.
     * 
     * If <code>x</code> is between [x1, x2], return x If <code>x</code> is less
     * than x1, return x1 If <code>x</code> is greater than y1, return y1
     * 
     * @param x
     * @return the clamped value
     */
    public double clampX(double x) {
        if (x < minLon) {
            return minLon;
        }
        if (x > maxLon) {
            return maxLon;
        }
        return x;
    }

    /**
     * Clamps the given x coordinate to this BoundingBoxDTO's limits.
     * 
     * If <code>x</code> is between [x1, x2], return x If <code>x</code> is less
     * than x1, return x1 If <code>x</code> is greater than y1, return y1
     * 
     * @param x
     * @return the clamped value
     */
    public double clampY(double y) {
        if (y < minLat) {
            return minLat;
        }
        if (y > maxLat) {
            return maxLat;
        }
        return y;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof BoundingBoxDTO) {
            BoundingBoxDTO b = (BoundingBoxDTO) o;
            return b.minLon == minLon && b.minLat == minLat && b.maxLon == maxLon && b.maxLat == maxLat;
        }
        return false;
    }
 
    @Override
	public int hashCode() {
    	// probably not a great hash code but we can't use
    	// Double.longBits and this satisfies the contract
		return Double.valueOf(minLon).hashCode();
	}

	/**
     * 
     * @return the minimum x (longitudinal) coordinate
     */
    public double getMinLon() {
        return minLon;
    }

    /**
     * 
     * @return the minimum y (latitudinal) coordinate
     */
    public double getMinLat() {
        return minLat;
    }

    /**
     * 
     * @return the maximum x (longitudinal) coordinate
     */
    public double getMaxLon() {
        return maxLon;
    }

    /**
     * @return the maximum y (latitudinal) coordinate
     */
    public double getMaxLat() {
        return maxLat;
    }

    /**
     * Sets the minimum x (longitudinal) value
     */
    public void setMinLon(double x1) {
        this.minLon = x1;
    }

    /**
     * Sets the minimum y (latitudinal) value
     */
    public void setMinLat(double y1) {
        this.minLat = y1;
    }

    /**
     * Sets the maximum x (longitudinal) value
     */
    public void setMaxLon(double x2) {
        this.maxLon = x2;
    }

    /**
     * Sets the maximum y (latitudinal) value
     */
    public void setMaxLat(double y2) {
        this.maxLat = y2;
    }
   
	
	public double getMinLng() {
		return minLon;
	}
	
	public double getMaxLng() {
		return maxLon;
	}
	
    @Override
    public String toString() {
        return "x1:" + minLon + ";x2:" + maxLon + ";y1:" + minLat + ";y2:" + maxLat;
    }

	public double getWidth() {
		return maxLon-minLon;
	}
	
	public double getHeight() {
		return maxLat-minLat;
	}

	public AiLatLng centroid() {
		return new AiLatLng(getCenterY(), getCenterX());
	}

}
