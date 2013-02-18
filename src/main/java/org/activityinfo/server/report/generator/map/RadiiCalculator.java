/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.server.report.generator.map;

import java.util.List;

import org.activityinfo.server.report.generator.map.cluster.Cluster;

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
