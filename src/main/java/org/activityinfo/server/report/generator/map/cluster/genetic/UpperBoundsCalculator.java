

package org.activityinfo.server.report.generator.map.cluster.genetic;

/*
 * #%L
 * ActivityInfo Server
 * %%
 * Copyright (C) 2009 - 2013 UNICEF
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import java.util.ArrayList;
import java.util.List;

import org.activityinfo.server.report.generator.map.RadiiCalculator;
import org.activityinfo.server.report.generator.map.cluster.Cluster;

public class UpperBoundsCalculator {

    public interface Tracer {
        void onSubgraph(int nodeCount);
        void incremented(int count, List<Cluster> clusters, double fitness);
    }

    public static List<Integer> calculate(MarkerGraph graph, RadiiCalculator radiiCalculator) {
        return calculate(graph, radiiCalculator, null);
    }

    /** Calculates the upper bound of the number of clusters per subgraph
     * based on a minimum possible radius */
    public static List<Integer> calculate(MarkerGraph graph, RadiiCalculator radiiCalculator, Tracer tracer) {

        List<Integer> bounds = new ArrayList<Integer>();
        List<List<MarkerGraph.Node>> subgraphs = graph.getSubgraphs();

        FitnessFunctor ftor = new BubbleFitnessFunctor();

        for(List<MarkerGraph.Node> subgraph : subgraphs) {

            if(tracer != null) {
                tracer.onSubgraph(subgraph.size());
            }

            bounds.add(calcUpperBound(subgraph, radiiCalculator, ftor, tracer));
        }

        return bounds;
    }

    private static int calcUpperBound(List<MarkerGraph.Node> subgraph, RadiiCalculator radiiCalculator, FitnessFunctor ftor, Tracer tracer) {

        for(int i=2;i<=subgraph.size();++i) {
            List<Cluster> clusters = KMeans.cluster(subgraph, i);
            radiiCalculator.calculate(clusters);
            double fitness = ftor.score(clusters);
            if(tracer != null) {
                tracer.incremented(i, clusters, fitness);
            }
            if(fitness <= 0) {
                return i-1;
            }
        }
        return subgraph.size();
    }

}
