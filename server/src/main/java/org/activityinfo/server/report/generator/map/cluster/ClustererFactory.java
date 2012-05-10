package org.activityinfo.server.report.generator.map.cluster;

import org.activityinfo.server.report.generator.map.RadiiCalculator;
import org.activityinfo.server.report.generator.map.cluster.genetic.MarkerGraph.IntersectionCalculator;
import org.activityinfo.shared.report.model.clustering.AdministrativeLevelClustering;
import org.activityinfo.shared.report.model.clustering.AutomaticClustering;
import org.activityinfo.shared.report.model.clustering.Clustering;
import org.activityinfo.shared.report.model.clustering.NoClustering;

public final class ClustererFactory {
	
	private ClustererFactory() {}
	
    public static Clusterer fromClustering(Clustering clustering, RadiiCalculator radiiCalculator, 
    		IntersectionCalculator intersectionCalculator) {
    	if (clustering instanceof NoClustering) {
    		return new NullClusterer(radiiCalculator);
    	} else if (clustering instanceof AutomaticClustering) {
    		return new GeneticClusterer(radiiCalculator, intersectionCalculator);
    	} else if(clustering instanceof AdministrativeLevelClustering) { 
    		return new AdminLevelClusterer((AdministrativeLevelClustering) clustering, radiiCalculator);
    	}
    	
    	return new NullClusterer(radiiCalculator); 
    }
}
