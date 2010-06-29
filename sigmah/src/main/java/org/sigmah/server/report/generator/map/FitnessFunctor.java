package org.sigmah.server.report.generator.map;

import java.util.List;
/*
 * @author Alex Bertram
 */

public interface FitnessFunctor {
    double score(List<Cluster> clusters);
}
