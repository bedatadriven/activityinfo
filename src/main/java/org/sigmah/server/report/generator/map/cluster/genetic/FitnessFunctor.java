/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.report.generator.map.cluster.genetic;

import java.util.List;

import org.sigmah.server.report.generator.map.cluster.Cluster;

/**
 * Scores the fitness of a particular clustering solution.
 */
public interface FitnessFunctor {
    double score(List<Cluster> clusters);
}
