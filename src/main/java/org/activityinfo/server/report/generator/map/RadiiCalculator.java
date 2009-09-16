package org.activityinfo.server.report.generator.map;

import org.activityinfo.server.report.generator.map.Cluster;

import java.util.List;
/*
 * @author Alex Bertram
 */

/**
     * Calculates the radii of clusters prior to the
 * evaluation of fitness.
 */
public interface RadiiCalculator {

    /**
     * Updates the radius of each of the clusters in the list
     *
     * @param clusters
     */
    public void calculate(List<Cluster> clusters);

}
