package org.sigmah.server.report.generator.map.cluster;

import java.util.List;

import org.sigmah.server.report.generator.map.cluster.auto.CircleFitnessFunctor;
import org.sigmah.server.report.generator.map.cluster.auto.MarkerGraph;
import org.sigmah.server.report.generator.map.cluster.auto.MarkerGraph.IntersectionCalculator;
import org.sigmah.server.report.generator.map.RadiiCalculator;
import org.sigmah.server.report.generator.map.UpperBoundsCalculator;
import org.sigmah.shared.report.model.PointValue;

public class GeneticClusterer implements Clusterer {
	RadiiCalculator radiiCalculator;
	IntersectionCalculator intersectionCalculator;
	private List<PointValue> points;

	public GeneticClusterer(RadiiCalculator radiiCalculator,
			IntersectionCalculator intersectionCalculator,
			List<PointValue> points) {
		super();
		this.radiiCalculator = radiiCalculator;
		this.intersectionCalculator = intersectionCalculator;
		this.points = points;
	}

	/*
	 * Clusters points using a genetic algorithm to determine what nearby points are
	 * logical candidates to cluster
	 * @see org.sigmah.shared.report.model.clustering.Clustering#cluster(java.util.List, org.sigmah.server.report.generator.map.RadiiCalculator)
	 */
	@Override
	public List<Cluster> cluster() {
		List<Cluster> clusters;
		
        MarkerGraph graph = new MarkerGraph(points, intersectionCalculator);

        GeneticSolver solver = new GeneticSolver();
        clusters = solver.solve(
                graph,
                radiiCalculator,
                new CircleFitnessFunctor(),
                UpperBoundsCalculator.calculate(graph,
                        radiiCalculator));
        
        return clusters;
	}
}