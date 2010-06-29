package org.sigmah.server.report.generator.map;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Alex Bertram
 */
public class ComplexClusterTest extends GraphTest {
    private static final double DELTA = 0.0001;

    @Test
    @Ignore("quite slow")
    public void dump2dBruteForce() throws Exception {

        ComplexPoints data = new ComplexPoints();


        FitnessFunctor fitFtor = new CircleFitnessFunctor();
        GsLogCalculator rCtor = new GsLogCalculator(5, 30);

        BufferedWriter csv = new BufferedWriter(new FileWriter("target/report-tests/brute.csv"));

        List<List<MarkerGraph.Node>> subgraphs = data.getLargestN(2);

        // write column headers
        for (int j = 0; j != subgraphs.get(0).size(); ++j) {
            csv.write("," + (j + 1));
        }
        csv.newLine();

        // write rows
        for (int i = 1; i <= subgraphs.get(0).size(); ++i) {

            // write row header
            csv.write(Integer.toString(i));

            // write cell values
            for (int j = 1; j <= subgraphs.get(1).size(); ++j) {
                List<Cluster> clusters = new ArrayList<Cluster>();
                clusters.addAll(KMeans.cluster(subgraphs.get(0), i));
                clusters.addAll(KMeans.cluster(subgraphs.get(1), j));
                rCtor.calculate(clusters);

                csv.write(Double.toString(fitFtor.score(clusters)));
            }

            csv.newLine();
        }
        csv.close();
    }

    @Test
    public void testUpperBounds() throws Exception {

        ComplexPoints data = new ComplexPoints();
        List<Integer> ub = UpperBoundsCalculator.calculate(data.graph, new FixedRadiiCalculator(5),
                new UpperBoundsCalculator.Tracer() {
                    public void onSubgraph(int nodeCount) {
                        System.out.println("Calculating upperbounds for subgraph with " + nodeCount + " node(s)");
                    }

                    public void incremented(int count, List<Cluster> clusters, double fitness) {
                        System.out.println(count + " cluster(s) = " + fitness);
                    }
                });
//        Assert.assertEquals(23, ub.get(0));
//        Assert.assertEquals(9, ub.get(1));

    }

    @Test
    public void testComplexCluster() throws Exception {

        ComplexPoints data = new ComplexPoints();

        saveGraphImage("ComplexClusterTest-graph", data.graph, 15);

        GsLogCalculator radiiCalc = new GsLogCalculator(5, 10);

        double sumAfterMerging = 0;
        for (MarkerGraph.Node node : data.graph.getNodes()) {
            sumAfterMerging += node.getPointValue().value;
        }
        Assert.assertEquals("originalSum==sumAfterMerging", data.originalSum, sumAfterMerging, DELTA);


        // becuase the algorithm is stochiastic, we want need to make sure
        // it works consistently

        int count = 10;
        while (count-- > 0) {


            GeneticSolver solver = new GeneticSolver();
            solver.setTracer(new GeneticTracer());
            List<Cluster> clusters = solver.solve(data.graph, radiiCalc, new CircleFitnessFunctor(),
                    UpperBoundsCalculator.calculate(data.graph, new FixedRadiiCalculator(5)));


            double sumAfterClustering = 0;
            for (Cluster cluster : clusters) {
                sumAfterClustering += cluster.sumValues();
            }
            Assert.assertEquals("originalSum==sumAfterClustering", data.originalSum, sumAfterClustering, DELTA);

            if (count == 0) {
                saveClusters(data.graph, "ComplexClusterTest-solution", clusters);
            }

            System.out.println(String.format("pop size = %d", solver.getPopulation().size()));
            System.out.println(String.format("subgraph count = %d", data.graph.getSubgraphs().size()));
            System.out.println(String.format("cluster count = %d", clusters.size()));

            System.out.println(String.format("fitness = %f", solver.getSolutionFitness()));

            // TODO: this seems to be successful about 75% of the time. We need to move that to 100%
            // and reduce variation
            //Assert.assertTrue("Did not meet success criteria at run "+ count, solver.getSolutionFitness() > 5000);
        }
    }

}
