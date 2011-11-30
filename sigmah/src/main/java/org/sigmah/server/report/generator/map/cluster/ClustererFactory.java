package org.sigmah.server.report.generator.map.cluster;

import org.sigmah.server.report.generator.map.RadiiCalculator;
import org.sigmah.server.report.generator.map.cluster.genetic.MarkerGraph.IntersectionCalculator;
import org.sigmah.shared.report.model.clustering.AdministrativeLevelClustering;
import org.sigmah.shared.report.model.clustering.AutomaticClustering;
import org.sigmah.shared.report.model.clustering.Clustering;
import org.sigmah.shared.report.model.clustering.NoClustering;

public class ClustererFactory {
	
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
