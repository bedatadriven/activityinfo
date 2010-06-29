package org.sigmah.server.report.generator.map;

import java.util.*;
/*
 * @author Alex Bertram
 */

public class HillClimber {

    private List<List<MarkerGraph.Node>> subgraphs;
    private Random random = new Random();

    private int[] currentNode;
    private List<List<Cluster>> clusters;
    private double currentScore;

    private final RadiiCalculator radiiCalculator;
    private final FitnessFunctor fitness;

    private Set<int[]> visited;

    private Neighbor lastStep;

    public String nodeToString() {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i!=currentNode.length;++i) {
            sb.append(' ').append(currentNode[i]);
        }
        return sb.toString();
    }


    public class Neighbor {
        private int subgraphIndex;
        private int delta;

        public Neighbor(int subgraphIndex, int delta) {
            this.subgraphIndex = subgraphIndex;
            this.delta = delta;
        }

        public int getSubgraphIndex() {
            return subgraphIndex;
        }

        public int getDelta() {
            return delta;
        }

        public int[] toArray() {
            int[] array = Arrays.copyOf(currentNode, currentNode.length);
            array[subgraphIndex] += delta;
            return array;
        }

        public String toString() {
            return Integer.toString(subgraphIndex) + (delta < 0 ?  "-1" : "+1");
        }
    }

    public HillClimber(MarkerGraph graph, RadiiCalculator radiiCalculator, FitnessFunctor fitness) {

        this.subgraphs = graph.getSubgraphs();
        this.radiiCalculator = radiiCalculator;
        this.fitness = fitness;

        currentNode = new int[subgraphs.size()];

        // generate initial, random state
        for(int i=0; i!=currentNode.length;++i) {
            if(subgraphs.get(i).size() == 1) {
                currentNode[i] = 1;
            } else {
                currentNode[i] = 1 + random.nextInt(subgraphs.get(i).size()-1);
            }
        }

        // do initial clustering
        clusters = new ArrayList<List<Cluster>>();
        for(int i=0; i!= subgraphs.size(); ++i) {
            clusters.add(KMeans.cluster(subgraphs.get(i), currentNode[i]));
        }

        // and assign initial score
        currentScore = score();

        // initialize our visited list and add our initial solution node
        visited = new HashSet<int[]>();
        visited.add(currentNode);

    }

    private double score() {

        // make full list of clusters
        List<Cluster> allClusters = allClusters();

        // calculate radii
        radiiCalculator.calculate(allClusters);

        // score
        return fitness.score(allClusters);
    }

    private double scoreNeighbor(Neighbor neighbor) {

        // update clusters for just the affected subgraph
        int subgraph = neighbor.getSubgraphIndex();

        List<Cluster> subgraphClusters = KMeans.cluster(subgraphs.get(subgraph),
                currentNode[subgraph] + neighbor.getDelta());

        // merge this change into our existing list
        List<Cluster> allClusters = new ArrayList<Cluster>();
        for(int i=0; i!=subgraphs.size(); ++i) {
            if(i == subgraph) {
                allClusters.addAll(subgraphClusters);
            } else {
                allClusters.addAll(clusters.get(i));
            }
        }

        // calculate radii
        radiiCalculator.calculate(allClusters);

        // score
        return fitness.score(allClusters);
    }

    /**
     *
     * @return All "neighboring" solutions
     */
    public List<Neighbor> neighbors() {

        List<Neighbor> neighbors = new ArrayList<Neighbor>(subgraphs.size()*2);

        for(int i=0; i!=subgraphs.size(); ++i) {
            if(currentNode[i] > 1) {
                neighbors.add(new Neighbor(i, -1));
            }
            if(currentNode[i] < subgraphs.get(i).size()) {
                neighbors.add(new Neighbor(i, +1));
            }
        }

        return neighbors;
    }

    public boolean nextStep() {

        double maxScore = currentScore;
        Neighbor maxNeighbor = null;
        int[] maxNode = null;

        List<Neighbor> neighbors = neighbors();
        for(Neighbor neighbor : neighbors) {
            int[] neighborNode = neighbor.toArray();

            double neighborScore = scoreNeighbor(neighbor);

            System.out.println(String.format("neighbor: %s, %+10.0f, %s",
                    neighbor.toString(),
                    neighborScore-currentScore,
                    visited.contains(neighborNode)? "visited" : "unvisited"));


            if(! visited.contains(neighborNode)) {

            //    double neighborScore = scoreNeighbor(neighbor);

                if(neighborScore > maxScore) {
                    maxNeighbor = neighbor;
                    maxNode = neighborNode;
                    maxScore = neighborScore;
                }

                visited.add(neighborNode);
            }
        }

        if(maxNode == null) {
            return false;
        } else {
            updateClusters(maxNeighbor);
            currentNode = maxNode;
            currentScore = maxScore;
            lastStep = maxNeighbor;

            return true;
        }
    }

    public Neighbor getLastStep() {
        return lastStep;
    }

    public void updateClusters(Neighbor neighbor) {

        int subgraph = neighbor.getSubgraphIndex();

        clusters.set(subgraph, KMeans.cluster(subgraphs.get(subgraph),
                        currentNode[subgraph] + neighbor.getDelta()));

        radiiCalculator.calculate(allClusters());
    }


    public List<Cluster> allClusters() {
        List<Cluster> allClusters = new ArrayList<Cluster>();
        for(List<Cluster> subGraphClusters : clusters) {
            allClusters.addAll(subGraphClusters);
        }
        return allClusters;
    }
}
