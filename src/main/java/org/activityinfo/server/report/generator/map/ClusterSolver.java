package org.activityinfo.server.report.generator.map;

import java.util.List;
/*
 * @author Alex Bertram
 */

public interface ClusterSolver {

    void init(MarkerGraph graph,
              RadiiCalculator radiiCalculator,
              FitnessFunctor fitnessFunctor,
              List<Integer> upperBounds);

}
