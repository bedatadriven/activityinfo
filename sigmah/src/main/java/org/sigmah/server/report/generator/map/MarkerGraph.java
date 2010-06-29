package org.sigmah.server.report.generator.map;

import org.sigmah.shared.report.content.Point;

import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.List;
/*
 * @author Alex Bertram
 */

public class MarkerGraph {
    private List<List<Node>> subgraphs;


    /**
     * Represents an edge between two <code>PointValue</code>s (<code>Node</code>s) on the map.
     * Two <code>PointValue</code> are connected if they potentially overlap.
     */
    public static class Edge {
        public Node a;
        public Node b;
        public int subgraph = -1;

        public Edge(Node a, Node b) {
            this.a = a;
            this.b = b;
        }

        public Node neighbor(Node node) {
            return node == a ? b : a;
        }

        public Point centroid() {
            return new Point( (a.getPoint().getX() + b.getPoint().getX()) / 2,
                              (a.getPoint().getY() + b.getPoint().getY()) / 2 );
        }

        public double length() {
            return a.getPoint().distance(b.getPoint());
        }

        public boolean isVertical() {
            return a.getPoint().getX() == b.getPoint().getY();
        }

        public Line2D line() {
            return new Line2D.Float(a.getPoint().getX(), a.getPoint().getY(),
                                    b.getPoint().getX(), b.getPoint().getY());
        }

        public boolean contains2D(Node node) {
            return line().contains(node.getPoint().getX(), node.getPoint().getY());
        }

        public boolean crosses(Edge other) {

            // in principle we don't accept coincident
            // nodes so equality of reference should be sufficinet

            if(this.a == other.a) {
                return this.contains2D(other.b);
            } else if(this.a == other.b) {
                return this.contains2D(other.a);
            } else if(this.b == other.a) {
                return this.contains2D(other.b);
            } else if(this.b == other.b) {
                return this.contains2D(other.a);
            } else {

                Line2D thisLine = line();
                Line2D otherLine = other.line();

                return thisLine.intersectsLine(otherLine);
            }
        }

    }

    /**
     * Wraps <code>PointValue</code> as a node in the graph
     */
    public static class Node {
        private List<Edge> edges;
        private PointValue pv;
        public int subgraph = -1;

        public Node(PointValue pv) {
            this.pv = pv;
            this.edges = new ArrayList<Edge>();
        }

        public void addEdge(Edge e) {
            edges.add(e);
        }

        public Point getPoint() {
            return pv.px;
        }

        public PointValue getPointValue() {
            return pv;
        }

        public List<Edge> getEdges() {
            return edges;
        }

        public double getValue() {
            return pv.value;
        }
    }

    public interface IntersectionCalculator {

        public boolean intersects(Node a, Node b);

    }

    private List<Node> nodes;
    private List<Edge> edges;

    /**
     * Constructs a graph of <code>PointValue</code>
     *
     * @param points
     */
    public MarkerGraph(List<PointValue> points, IntersectionCalculator icalculator) {

        nodes = new ArrayList<Node>();
        edges = new ArrayList<Edge>();

        for(PointValue pv : points) {
            nodes.add(new Node(pv));
        }

        for(int i=0; i!=nodes.size(); ++i) {
            Node ni = nodes.get(i);

            int j=i+1;
            while(j < nodes.size()) {
                Node nj = nodes.get(j);

                if(icalculator.intersects(ni, nj)) {

                    // check for coincidence, which screws things up
                    // if we leave in place
                    if(ni.getPoint().equals(nj.getPoint())) {
                        // merge nodes in the case of coincidence
                        ni.getPointValue().value += nj.getPointValue().value;
                        for(Edge edge : nj.getEdges()) {
                            edge.neighbor(nj).getEdges().remove(edge);
                            edges.remove(edge);
                        }
                        nodes.remove(j);
                    } else {
                        // otherwise connect them in the graph
                        Edge e = new Edge(ni, nj);
                        ni.addEdge(e);
                        nj.addEdge(e);
                        edges.add(e);
                        j++;
                    }
                } else {
                    j++;
                }
            }
        }

        subgraphs = new ArrayList<List<Node>>();
        
        int nextSubgraph = 0;
        for(Node node : nodes ) {
            if(node.subgraph < 0) {

                List<Node> subgraph = new ArrayList<Node>();
                subgraph.add(node);
                subgraphs.add(subgraph);

                node.subgraph = nextSubgraph++;

                assignToSubgraph(node, subgraph);
            }
        }
    }

    private void assignToSubgraph(Node node, List<Node> subgraph) {
        for(Edge edge : node.getEdges()) {
            if(edge.subgraph < 0) {
                edge.subgraph = node.subgraph;
                Node neighbor = edge.neighbor(node);
                if(neighbor.subgraph < 0) {
                    neighbor.subgraph = node.subgraph;
                    subgraph.add(neighbor);
                    assignToSubgraph(neighbor, subgraph);
                }
            }
        }
    }

    private void removeEdge(Edge edge) {
        edge.a.edges.remove(edge);
        edge.b.edges.remove(edge);
        edges.remove(edge);
    }


//    public List<Edge> getEdgesInSubgraph(int subgraphIndex) {
//        List<Edge> list = new ArrayList<Edge>();
//        for(Edge edge : edges) {
//            if(edge.subgraph == subgraphIndex) {
//                list.add(edge);
//            }
//        }
//        return list;
//    }

//    /**
//     * Cleans up edges by removing those that geometrically
//     * intersect. Good for presentation and preserves subgraphs
//     * (I think) but not necessary running K-means
//     */
//    public void cleanupEdges() {
//
//        for(int sg = 0; sg!=subgraphCount; ++sg) {
//
//            List<Edge> sgEdges = getEdgesInSubgraph(sg);
//
//            // sort the edges by cartesian length; we want
//            // to preserve the most direct links so we'll
//            // put them at the beginning of the list
//            Collections.sort(sgEdges, new Comparator<Edge>() {
//                public int compare(Edge o1, Edge o2) {
//                    return Double.compare(o1.length(), o2.length());
//                }
//            });
//
//            boolean[] removed = new boolean[sgEdges.size()];
//            for(int i=0; i!=sgEdges.size(); ++i) {
//                int j=i+1;
//                while(j < sgEdges.size()) {
//                    if(sgEdges.get(i).crosses(sgEdges.get(j))) {
//                        removeEdge(sgEdges.get(j));
//                        sgEdges.remove(j);
//                    } else {
//                        j++;
//                    }
//                }
//            }
//        }
//    }

    public List<Edge> getEdges() {
        return edges;
    }

    public List<Node> getNodes() {
        return nodes;
    }

    public List<List<Node>> getSubgraphs() {
        return subgraphs;
     }


}
