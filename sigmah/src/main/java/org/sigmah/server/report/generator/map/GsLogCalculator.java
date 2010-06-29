/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.report.generator.map;

import java.util.List;
/*
 * @author Alex Bertram
 */

public class GsLogCalculator implements RadiiCalculator {

    private double minRadius;
    private double maxRadius;

    public GsLogCalculator(double minRadius, double maxRadius) {
        this.minRadius = minRadius;
        this.maxRadius = maxRadius;
    }

    public void calculate(List<Cluster> clusters) {

        // FIRST PASS:
        // calculate min and max of the markers
        // calculate the weightedCentroid of the clusters

        double[] values = new double[clusters.size()];
        double minValue = Double.MAX_VALUE;
        double maxValue = Double.MIN_VALUE;
        
        for(int i=0; i!=clusters.size(); ++i) {

            values[i] = clusters.get(i).sumValues();

            if( values[i] > maxValue) {
                maxValue = values[i];
            }
            if( values[i] < minValue) {
                minValue = values[i];
            }
        }

        /// SECOND PASS: calculate symbol size

        double logMin = Math.log(minValue);
        double logMax = Math.log(maxValue);
        double logRange = logMax - logMin;
        double symbolRange = maxRadius - minRadius;


        for(int i=0; i!=clusters.size();++i) {

            double logValue = Math.log(values[i]);

            double p = ((logValue - logMin) / logRange);

            clusters.get(i).setRadius(Math.round(minRadius + (symbolRange * p)));
        }
    }
}
