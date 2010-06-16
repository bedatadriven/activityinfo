package org.activityinfo.server.report.generator.map;

import java.awt.*;
import java.util.List;
/*
 * @author Alex Bertram
 */

public class RectFitnessFunctor implements FitnessFunctor {

    private int area(Rectangle r) {
        return r.width * r.height;
    }

    public double score(List<Cluster> clusters) {
        double score = 0;
        for(int i=0; i!=clusters.size(); ++i) {


            // award a score for the presence of this cluster
            // (all things equal, the more markers the better)
            Rectangle iRect = clusters.get(i).getRectangle();
            score += area(iRect);


            // penalize conflicts with other clusters
            for(int j=i+1; j!=clusters.size(); ++j) {

                Rectangle jRect = clusters.get(j).getRectangle();

                if(iRect.intersects(jRect)) {
                    score -= 4.0 * area(iRect.intersection(clusters.get(j).getRectangle()));
                }
            }
        }

        return score;
    }
}
