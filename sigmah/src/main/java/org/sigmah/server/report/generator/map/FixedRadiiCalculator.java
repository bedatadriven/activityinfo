/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.report.generator.map;

import java.util.List;
/*
 * @author Alex Bertram
 */

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
