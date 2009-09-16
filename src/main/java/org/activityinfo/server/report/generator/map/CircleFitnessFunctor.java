package org.activityinfo.server.report.generator.map;

import java.util.List;
/*
 * @author Alex Bertram
 */

public class CircleFitnessFunctor implements FitnessFunctor {


    public double score(List<Cluster> clusters) {

        double score = 0;
        for(int i=0; i!=clusters.size(); ++i) {

            // award a score for the presence of this cluster
            // (all things equal, the more markers the better)
            score += CircleMath.area(clusters.get(i).getRadius());
  
            // penalize clusters whose circle does not include
            // all of its nodes
//            for(MarkerGraph.Node node : clusters.get(i).getNodes()) {
//                double d = centroids[i].distance(node.getPoint());
//                if(d > radii[i]) {
//                    score -= (CircleMath.area(d)-CircleMath.area(radii[i]));
//                }
//            }

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
