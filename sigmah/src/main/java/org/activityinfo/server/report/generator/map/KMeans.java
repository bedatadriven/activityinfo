package org.activityinfo.server.report.generator.map;

import org.activityinfo.shared.report.content.Point;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * Finds a given number of cluster centers using the KMeans algorithm.
 *
 *
 * @author Alex Bertram
 */
public class KMeans {


    public static List<Cluster> cluster(List<MarkerGraph.Node> nodes, int numClusters) {

        List<Cluster> clusters = new ArrayList<Cluster>(numClusters);

        // sanity check
        if(numClusters > nodes.size() || nodes.size() ==0) {
            throw new IllegalArgumentException();
        }

        // randomize
        Collections.shuffle(nodes);

        // choose random centers
        Point[] centers = new Point[numClusters];
        for(int i=0; i!=numClusters; ++i) {
            centers[i] = nodes.get(i).getPoint();
        }

        // assign initial cluster membership
        int[] membership = new int[nodes.size()];
        assignClosest(nodes, centers, membership);

        // execute k-means algorithm until we achieve convergence
        boolean changed;
        do {

            computeCenters(nodes, membership, centers);
            changed = assignClosest(nodes, centers, membership);

        } while(changed);

        // create clusters
        for(int i=0; i!=numClusters;++i) {
            clusters.add(new Cluster(centers[i]));
        }
        
        for(int j=0; j!=nodes.size();++j) {
            clusters.get(membership[j]).addNode(nodes.get(j));
        }

        return clusters;
    }

    /**
     * Computes the centers of the assigned clusters
     *
     * @param nodes The list of nodes
     * @param membership An array containing the cluster membership for each node
     * @param centers Array of center points to update
     */
    private static void computeCenters(List<MarkerGraph.Node> nodes, int[] membership, Point[] centers) {
        int[] sumX = new int[centers.length];
        int[] sumY = new int[centers.length];
        int[] counts = new int[centers.length];

        for(int i=0; i!=nodes.size(); ++i) {
            Point point = nodes.get(i).getPoint();
            sumX[membership[i]] += point.getX();
            sumY[membership[i]] += point.getY();
            counts[membership[i]] ++;

        }

        for(int i=0; i!=centers.length;++i) {
            if(counts[i] > 0) {
                centers[i] = new Point(
                        sumX[i] / counts[i],
                        sumY[i] / counts[i]);
            }
        }
    }

    /**
     * Updates the membership array by assigning each node to its closest
     * cluster
     *
     * @param nodes
     * @param centers
     * @param membership
     * @return True if cluster membership has changed
     */
    private static boolean assignClosest(List<MarkerGraph.Node> nodes, Point[] centers, int[] membership) {
        boolean changed = false;
        for(int i=0; i!=nodes.size();++i) {

            // for this node, find the closest
            // cluster center

            double minDist = Double.MAX_VALUE;
            int closest=0;

            for(int j=0; j!=centers.length;++j) {
                double dist = nodes.get(i).getPoint().distance(centers[j]);
                if(dist < minDist) {
                    minDist = dist;
                    closest = j;
                }
            }

            // update membership if necessary

            if(membership[i] != closest) {
                membership[i] = closest;
                changed= true;
            }
        }

        return changed;
    }
}
