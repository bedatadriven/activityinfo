/*
 * All Sigmah code is released under the GNU General Public License v3
 * See COPYRIGHT.txt and LICENSE.txt.
 */

package org.sigmah.server.report.generator.map;

import java.util.List;
/*
 * @author Alex Bertram
 */

public interface FitnessFunctor {
    double score(List<Cluster> clusters);
}
