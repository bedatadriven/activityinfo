

package org.activityinfo.server.report.generator.map;

/*
 * #%L
 * ActivityInfo Server
 * %%
 * Copyright (C) 2009 - 2013 UNICEF
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the 
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public 
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */

import org.activityinfo.server.report.generator.map.cluster.genetic.GeneticSolver;

public class GeneticTracer implements GeneticSolver.Tracer {


    public void breeding(GeneticSolver solver, int i, int j) {
        System.out.println(String.format("Breeding phenotypes %d and %d", i, j));
    }

    public void evolved(GeneticSolver solver, int generation, int stagnationCount) {
        System.out.println(String.format("Generation %d evolved, fitness = %f", generation,
                solver.getFittest().getFitness()));
    }

    private String phenotypeToString(int[] p) {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i!=p.length; ++i) {
            String value = Integer.toString(p[i]);
            int len = value.length();
            while(len++ < 3) {
                sb.append(' ');
            }
            sb.append(value);
        }
        return sb.toString();
    }

    public void crossover(GeneticSolver solver, int[] p1, int[] p2, int xoverPoint, int[] c1, int[] c2) {
//        System.out.println("Parent 1 = " + phenotypeToString(p1));
//        System.out.println("Parent 2 = " + phenotypeToString(p2));
//        System.out.println("Child  1 = " + phenotypeToString(c1));
//        System.out.println("Child  2 = " + phenotypeToString(c2));
    }
}
