package org.sigmah.server.report.generator.map;

import java.util.ArrayList;
import java.util.List;
/*
 * @author Alex Bertram
 */

public class UpperBoundsCalculator {

    public interface Tracer {

        void onSubgraph(int nodeCount);
        void incremented(int count, List<Cluster> clusters, double fitness);

    }

    public static List<Integer> calculate(MarkerGraph graph, RadiiCalculator radiiCalculator) {
        return calculate(graph, radiiCalculator, null);
    }


    /**
     * Calculates the upper bound of the number of clusters per subgraph
     * based on a minimum possible radius
     *
     * @param graph
     * @param radiiCalculator
     * @return
     */
    public static List<Integer> calculate(MarkerGraph graph, RadiiCalculator radiiCalculator, Tracer tracer) {

        List<Integer> bounds = new ArrayList<Integer>();
        List<List<MarkerGraph.Node>> subgraphs = graph.getSubgraphs();

        FitnessFunctor ftor = new CircleFitnessFunctor();

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
