package org.sigmah.server.report.generator.map;

import org.junit.Test;
import org.sigmah.shared.report.content.Point;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
/*
 * @author Alex Bertram
 */

public class GraphTest {


    @Test
    public void testGraph() throws Exception {

        List<PointValue> points = new ArrayList<PointValue>();
        points.add(new PointValue(null, null, 100, new Point(380, 530)));
        points.add(new PointValue(null, null, 200, new Point(500, 500)));
        points.add(new PointValue(null, null, 50, new Point(650, 550)));
        points.add(new PointValue(null, null, 300, new Point(600, 600)));
        points.add(new PointValue(null, null, 200, new Point(650, 650)));

        points.add(new PointValue(null, null, 30, new Point(500, 1300)));

        points.add(new PointValue(null, null, 150, new Point(200, 200)));
        points.add(new PointValue(null, null, 200, new Point(350, 200)));
        points.add(new PointValue(null, null, 150, new Point(500, 200)));
        points.add(new PointValue(null, null, 30, new Point(700, 200)));

      //  MarkerGraph graph = new MarkerGraph(points, new GraduatedLogCalculator(50, 100), 100);



    }

    protected void saveGraphImage(String testName, MarkerGraph graph, int maxRadius) throws Exception {

        File outputDir = new File("target/report-tests");
        outputDir.mkdirs();

        File outputFile = new File("target/report-tests/" + testName + ".svg");

        FileWriter svg = new FileWriter(outputFile);

        svg.write("<svg width='100%' height='100%' transform='scale(50)' version='1.1'  xmlns='http://www.w3.org/2000/svg'>");
        for(MarkerGraph.Edge edge : graph.getEdges()) {
            svg.write(String.format("<line x1='%d' y1='%d' x2='%d' y2='%d' " +
                        "style='stroke:rgb(99,99,99);stroke-width:1'/>",
                        edge.a.getPoint().getX(),
                         edge.a.getPoint().getY(),
                         edge.b.getPoint().getX(),
                         edge.b.getPoint().getY()));
        }

        for(MarkerGraph.Node node : graph.getNodes()) {
            svg.write(String.format("<circle cx='%d' cy='%d' r='1.5' " +
                        "style='stroke:none; fill: blue'/>",
                         node.getPoint().getX(),
                         node.getPoint().getY()));
//            svg.write(String.format("<circle cx='%d' cy='%d' r='%d' " +
//                        "style='stroke:blue; stroke-width:1; fill: none' title='node%d'/>",
//                         node.getPoint().getX(),
//                         node.getPoint().getY(),
//                         maxRadius,
//                         node.index));

        }
        svg.write("</svg>");
        svg.close();

    }

    protected void saveClusters(MarkerGraph graph, String fileName, List<Cluster> clusters) throws IOException {


        File outputDir = new File("target/report-tests");
        outputDir.mkdirs();
        
        File outputFile = new File("target/report-tests/" + fileName + ".svg");

        FileWriter svg = new FileWriter(outputFile);

        svg.write("<svg width='100%' height='100%' transform='scale(500)' version='1.1'  " +
                "xmlns='http://www.w3.org/2000/svg' xmlns:xlink='http://www.w3.org/1999/xlink' >\n");

        for(MarkerGraph.Edge edge : graph.getEdges()) {
            svg.write(String.format("<path d='M%d %d L%d %d' " +
                        "style='stroke:rgb(92,92,92);stroke-width:0.25'/>\n",
                         edge.a.getPoint().getX(),
                         edge.a.getPoint().getY(),
                         edge.b.getPoint().getX(),
                         edge.b.getPoint().getY()));
        }

        String[] colors = new String[] {
                "antiquewhite", "blue", "brown", "chartreuse", "cornflowerblue",
                "crimson", "darkkhaki", "darkorange", "darkorchid", "lightpink",
                "lightseagreen", "lightskyblue", "lime", "limegreen", "magenta",
                "mediumaqua" };

        int colorIndex =0;
        for(int i=0; i!= clusters.size();++i) {
            Cluster cluster = clusters.get(i);
            svg.write(String.format("<circle cx='%d' cy='%d' r='%f' " +
                         "style='fill: %s; fill-opacity: 0.5; stroke:none'/>\n",
                        cluster.getPoint().getX(),
                        cluster.getPoint().getY(),
                        cluster.getRadius(),
                        colors[colorIndex]));


            for(PointValue pointValue : cluster.getPointValues()) {
                svg.write(String.format("<circle cx='%d' cy='%d' r='1.5' " +
                            "style='stroke:rgb(92,92,92); stroke-width:0.1; fill: %s' title='%f'/>\n",
                             pointValue.px.getX(),
                             pointValue.px.getY(),
                             colors[colorIndex],
                             pointValue.value));
            }

            colorIndex++;
            if(colorIndex == colors.length) {
                colorIndex = 0;
            }

        }



        // label subgraphs
        List<List<MarkerGraph.Node>> subgraphs = graph.getSubgraphs();
        for(int i=0; i!=subgraphs.size();++i) {
            Point p = findLR(subgraphs.get(i));

            svg.write(String.format("<text x='%d' y='%d'>%d</text>\n",
                    p.getX() + 5,
                    p.getY(),
                    i));
        }
        
        svg.write("</svg>\n");
        svg.close();


    }

    private Point findLR(List<MarkerGraph.Node> nodes) {
        Point lr = new Point(Integer.MIN_VALUE, 0);
        for(MarkerGraph.Node node : nodes) {
            if(node.getPoint().getX() > lr.getX()) {
                lr = node.getPoint();
            }
        }
        return lr;
    }

}
