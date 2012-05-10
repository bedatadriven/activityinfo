package org.activityinfo.server.report.generator.map.cluster;

import java.util.List;

import org.activityinfo.server.report.generator.map.RadiiCalculator;
import org.activityinfo.server.report.generator.map.TiledMap;
import org.activityinfo.server.report.generator.map.cluster.genetic.BubbleFitnessFunctor;
import org.activityinfo.server.report.generator.map.cluster.genetic.GeneticSolver;
import org.activityinfo.server.report.generator.map.cluster.genetic.MarkerGraph;
import org.activityinfo.server.report.generator.map.cluster.genetic.UpperBoundsCalculator;
import org.activityinfo.server.report.generator.map.cluster.genetic.MarkerGraph.IntersectionCalculator;
import org.activityinfo.shared.dto.SiteDTO;
import org.activityinfo.shared.report.model.PointValue;

/**
 * Clusters PointValues together in an optimal way using
 * a genetic algorithm.
 */
public class GeneticClusterer implements Clusterer {
	private final RadiiCalculator radiiCalculator;
	private final IntersectionCalculator intersectionCalculator;
	
	public GeneticClusterer(RadiiCalculator radiiCalculator,
			IntersectionCalculator intersectionCalculator) {
		super();
		this.radiiCalculator = radiiCalculator;
		this.intersectionCalculator = intersectionCalculator;
	}

	/*
	 * Clusters points using a genetic algorithm to determine what nearby points are
	 * logical candidates to cluster
	 * @see org.activityinfo.shared.report.model.clustering.Clustering#cluster(java.util.List, org.activityinfo.server.report.generator.map.RadiiCalculator)
	 */
	@Override
	public List<Cluster> cluster(TiledMap map, List<PointValue> points) {
		List<Cluster> clusters;
		
        MarkerGraph graph = new MarkerGraph(points, intersectionCalculator);

        GeneticSolver solver = new GeneticSolver();
        clusters = solver.solve(
                graph,
                radiiCalculator,
                new BubbleFitnessFunctor(),
                UpperBoundsCalculator.calculate(graph,
                        radiiCalculator));
        
        return clusters;
	}

	@Override
	public boolean isMapped(SiteDTO site) {
		return site.hasLatLong();
	}
}