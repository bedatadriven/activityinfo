/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.activityinfo.server.report.generator.map.cluster.genetic;

import java.util.List;

import org.activityinfo.server.report.generator.map.CircleMath;
import org.activityinfo.server.report.generator.map.cluster.Cluster;

/**
 * Scores the fitness of a circle clustering solution, awarding points
 * for more bubbles, and penalizing solutions in which bubbles overlap.
 *
 */
public class BubbleFitnessFunctor implements FitnessFunctor {

	@Override
	public double score(List<Cluster> clusters) {

        double score = 0;
        for(int i=0; i!=clusters.size(); ++i) {

            // award a score for the presence of this cluster
            // (all things equal, the more markers the better)
            score += CircleMath.area(clusters.get(i).getRadius());

            // penalize conflicts with other clusters
            for(int j=i+1; j!=clusters.size(); ++j) {

                score -= 4.0 * CircleMath.intersectionArea(
                        clusters.get(i).getPoint(),
                        clusters.get(j).getPoint(),
                        clusters.get(i).getRadius(),
                        clusters.get(j).getRadius());
            }
        }
        return score;
	}
}
