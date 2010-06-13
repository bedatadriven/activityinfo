package org.activityinfo.server.report.generator.map;


import java.util.*;
/*
 * @author Alex Bertram
 */

public class GeneticSolver {

    private RadiiCalculator radiiCalculator;
    private FitnessFunctor fitnessFunctor;
    private List<List<MarkerGraph.Node>> subgraphs;
    private List<Integer> upperBounds;
    private List<Phenotype> population;
    private Random random;

    private List<Cluster> simpleClusters;

    private double solutionFitness;


    /**
     * Probablity that a single chromosome will mutate
     * over the course of a generation
     */
    private static final double PROB_MUTATE = 0.15;

    /**
     * The root value of the Cumulative Density Function (CDF)
     * of the random variable used to select phenotypes with
     * probability proportionate to fitness.
     *
     * Should be between 0 and 1: the lower the value, the greater
     * the preference for the most fit. The greater the value, the
     * more likely that unfit phenotypes will reproduce.
     */
    private static final double SELECTION_CDF_ROOT = 0.1;

    public interface Tracer {
        void breeding(GeneticSolver solver, int i, int j);
        void evolved(GeneticSolver solver, int generation, int stagnationCount);
        void crossover(GeneticSolver solver, int[] p1, int[] p2, int xoverPoint, int[] c1, int[] c2);
    }

    private Tracer tracer;

    public class Phenotype {

        private int[] chromosomes;
        private List<Cluster> clusters;
        private double fitness;

        public Phenotype(int[] chromosomes) {
            this.chromosomes = chromosomes;
            this.clusters = new ArrayList<Cluster>();
            for(int i=0;i!=chromosomes.length;++i){
                this.clusters.addAll(KMeans.cluster(subgraphs.get(i), chromosomes[i]));
            }
            this.clusters.addAll(simpleClusters);
            radiiCalculator.calculate(this.clusters);
            fitness = fitnessFunctor.score(clusters);
        }

        public double getFitness() {
            return fitness;
        }

        public List<Cluster> getClusters() {
            return clusters;
        }

        public int[] getChromosomes() {
            return chromosomes;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append('[');
            for(int i=0; i!=chromosomes.length;++i) {
                sb.append(' ').append(String.format("%3d", chromosomes[i]));
            }
            sb.append(" ]; fitness=").append(String.format("%+10.0f", fitness));
            return sb.toString();
        }
    }

    public List<Cluster> solve(MarkerGraph graph, RadiiCalculator radiiCalculator,
                         FitnessFunctor fitnessFunctor, List<Integer> allUpperBounds) {

        this.radiiCalculator = radiiCalculator;
        this.fitnessFunctor = fitnessFunctor;

        this.random = new Random();

        // for subgraphs that contain only one node, create a cluster
        // for them right away and keep them out of the problem.
        // this will make crossover / mutations more effective

        List<List<MarkerGraph.Node>> allSubGraphs = graph.getSubgraphs();
        int popSize = 1;
        subgraphs = new ArrayList<List<MarkerGraph.Node>>(allSubGraphs.size());
        simpleClusters = new ArrayList<Cluster>(allSubGraphs.size());
        upperBounds = new ArrayList<Integer>(allSubGraphs.size());

        for (int i = 0; i != allSubGraphs.size(); i++) {
            List<MarkerGraph.Node> subGraph = allSubGraphs.get(i);
            if (subGraph.size()==1) {
                simpleClusters.add(new Cluster(subGraph.get(0)));
            } else if(allUpperBounds.get(i) == 1) {
                simpleClusters.addAll(KMeans.cluster(subGraph, 1));
            } else {
                upperBounds.add(allUpperBounds.get(i));
                subgraphs.add(subGraph);
                popSize += 1 + (int) Math.log(allUpperBounds.get(i) * 32);
            }
        }
        if(popSize > 50) {
            popSize = 50;
        }

        this.population = new ArrayList<Phenotype>(popSize);
        addRandomPhenotypes(this.population, popSize);
        orderPopulation();

        double lastFitnessScore = 0;
        int generationsStagnated = 0;

        if(subgraphs.size() > 0) {
            for(int generation=0; generation < 100; ++generation) {

                double fitness = evolve(generationsStagnated);

                if(tracer != null) {
                    tracer.evolved(this, generation, generationsStagnated);
                }

                if(fitness != lastFitnessScore) {
                    lastFitnessScore = fitness;
                    generationsStagnated = 0;
                } else {
                    generationsStagnated ++;
                }

                if(generationsStagnated == 8) {
                    break;
                }
            }
        }

        solutionFitness = getFittest().getFitness();

        return getFittest().getClusters();
    }

    private void addRandomPhenotypes(List<Phenotype> population, int count) {
        for(int i=0;i!= count;++i){
            int[] chromosomes = new int[subgraphs.size()];
            for(int j=0;j!=chromosomes.length;++j) {
                chromosomes[j] = randomChromosome(j);
            }
            population.add(new Phenotype(chromosomes));
        }
    }

    private int randomChromosome(int i) {
        return 1 + random.nextInt(upperBounds.get(i)-1);
    }

    /**
     * Wipes out all but the fittest + 1 random member of the population
     * and replaces with random phenotypes.
     *
     * Originally implemented as a last ditch effort as we converge, but
     * in fact doesn't help because the totally random population tends
     * to be pretty mediocre in fitness terms.
     *
     */
    private void plagueOnBothOfYourHouses() {
        List<Phenotype> survivors = new ArrayList<Phenotype>();
        survivors.add(getFittest());
        survivors.add(population.get(randomPhenotype()));
        addRandomPhenotypes(survivors, population.size()-2);

        population = survivors;
    }


    public double evolve(int generationsStagnated) {

        List<Phenotype> nextgen = new ArrayList<Phenotype>(population.size());

        // preserve the best solution from the current generation
        // to avoid regressing backwords...
        nextgen.add(population.get(0));

        while(nextgen.size() < population.size()) {

            List<int[]> parents = selectParents();
            int[] child1 = Arrays.copyOf(parents.get(0), parents.get(0).length);
            int[] child2 = Arrays.copyOf(parents.get(1), parents.get(1).length);

            crossover(child1, child2);
            double pMutate = Math.pow( PROB_MUTATE, 1+(generationsStagnated/2.0));
            mutate(child1, pMutate );
            mutate(child2, pMutate );

            nextgen.add(new Phenotype(child1));
            nextgen.add(new Phenotype(child2));
        }

        population = nextgen;
        orderPopulation();

        return population.get(0).getFitness();
    }

    private void orderPopulation() {
        Collections.sort(population, new Comparator<Phenotype>() {
            public int compare(Phenotype o1, Phenotype o2) {
                return -Double.compare(o1.getFitness(), o2.getFitness());
            }
        });
    }

    public void setTracer(Tracer tracer) {
        this.tracer = tracer;
    }

    /**
     * Performs a "crossover" operation on two phenotypes. Supposed to be
     * analogous to what happens in biological reproduction.
     *
     * See http://en.wikipedia.org/wiki/Crossover_%28genetic_algorithm%29
     *
     * @param p1 First phenotype (array of chromosomes)
     * @param p2 Second phenotype (array of chromosomes)
     * @return
     */
    public List<int[]> crossover(int[] p1, int[] p2) {

        List<int[]> children = new ArrayList<int[]>(2);
        children.add(Arrays.copyOf(p1, p1.length));
        children.add(Arrays.copyOf(p2, p2.length));

        int xoverPoint = random.nextInt(subgraphs.size());

        swap(children.get(0), children.get(1), xoverPoint);

        if(tracer != null) {
            tracer.crossover(this, p1, p2, xoverPoint, children.get(0), children.get(1));
        }

        return children;
    }

    private void swap(int[] a, int[] b, int xoverPoint) {
        int[] tmp = Arrays.copyOfRange(a, 0, xoverPoint);
        System.arraycopy(b, 0, a, 0, xoverPoint);
        System.arraycopy(tmp, 0, b, 0, xoverPoint);
    }

    /**
     *
     * @param chromosomes
     * @param pMutate Probability that an individual chromosome will mutate
     */
    private void mutate(int[] chromosomes, double pMutate) {
        for(int i=0;i!=chromosomes.length;++i) {
            if(random.nextDouble() < pMutate) {
                chromosomes[i] = randomChromosome(i);
            }
        }
    }

    /**
     *
     * @return  The index of a random phenotype, selected with probabality proportionate
     * to its fitness rank
     */
    public int randomPhenotype() {
        return (int)Math.round((1-Math.pow(random.nextDouble(), SELECTION_CDF_ROOT)) * (double)(population.size()-1));
    }

    /**
     *
     * @return A random pair of <code>Phenotype</code>s, selected with probability proportionate
     * to fitness rank.
     */
    public List<int[]> selectParents() {

        int i, j;
        do {
            i = randomPhenotype();
            j = randomPhenotype();

        } while(i==j);

        if(tracer != null) {
            tracer.breeding(this, i, j);
        }

        List<int[]> parents = new ArrayList<int[]>(2);
        parents.add(population.get(i).getChromosomes());
        parents.add(population.get(j).getChromosomes());
        return parents;
    }

    public List<Phenotype> getPopulation() {
        return population;
    }

    public Phenotype getFittest() {
        return population.get(0);
    }


    public double getSolutionFitness() {
        return solutionFitness;
    }
}
