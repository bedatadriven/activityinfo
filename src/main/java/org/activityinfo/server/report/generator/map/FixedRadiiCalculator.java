/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.server.report.generator.map;

import java.util.List;

import org.activityinfo.server.report.generator.map.cluster.Cluster;

public class FixedRadiiCalculator implements RadiiCalculator {

    private final int radius;

    public FixedRadiiCalculator(int radius) {
        this.radius = radius;
    }

    public void calculate(List<Cluster> clusters) {
        for(Cluster cluster : clusters) {
            cluster.setRadius(radius);
        }
    }
}
