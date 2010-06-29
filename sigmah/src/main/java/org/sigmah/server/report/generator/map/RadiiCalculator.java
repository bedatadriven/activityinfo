/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.report.generator.map;

import java.util.List;
/*
 * @author Alex Bertram
 */

/**
     * Calculates the radii of clusters prior to the
 * evaluation of fitness.
 */
public interface RadiiCalculator {

    /**
     * Updates the radius of each of the clusters in the list
     *
     * @param clusters
     */
    public void calculate(List<Cluster> clusters);

}
