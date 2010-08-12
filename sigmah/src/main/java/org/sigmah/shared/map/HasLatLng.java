/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.shared.map;

/**
 * Interface to an object which may have latitude
 */
public interface HasLatLng {

    /**
     *
     * @return true if the object has non-null latitude and longitude
     */
    boolean hasLatLong();

    /**
     * @return the object's longitude (x) value
     */
	double getLongitude();

    /**
     * @return  the object's latitude (y) value
     */
	double getLatitude();
}

