package org.activityinfo.server.report.generator.map;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import java.util.Arrays;

import org.activityinfo.server.report.generator.map.Jenks.Breaks;

public class JenksBreakerTest {

	private static double VALUES[] = {
		1111, 	6751, 	502, 	1054, 	825, 	868, 	83, 	92, 	16, 	116, 	174, 	148, 	6447, 	
		597, 	1748, 	627, 	2338, 	868, 	441, 	1029, 	1294, 	5184, 	1512, 	232, 	565, 	1564, 	
		270, 	1127, 	2636, 	661, 	288, 	598, 	1389, 	527, 	289, 	825, 	612, 	336
	};
	
	@org.junit.Test
	public void test() {

		Jenks breaker = new Jenks();
		breaker.addValues(VALUES);
		Breaks breaks = breaker.computeBreaks(5);
		
		assertThat(VALUES.length, equalTo(
				breaks.getClassCount(0) + 
				breaks.getClassCount(1) +
				breaks.getClassCount(2) + 
				breaks.getClassCount(3) +
				breaks.getClassCount(4)));
		
		System.out.println(Arrays.toString(breaks.getValues()));
		System.out.println(breaks);
	}
	
	@org.junit.Test
	public void testGva() {
		Jenks breaker = new Jenks();
		for(double value : VALUES) {
			breaker.addValue(value);
		}

		double last = 0;
		for(int i=2;i<=14;++i) {
			double gvf = breaker.computeBreaks(i).gvf();
			
			System.out.print("delta " + i + " = " + (gvf-last) + "\n");
			last = gvf;			
		}
		
		Breaks bestBreaks = breaker.computeBreaks();
		System.out.println(bestBreaks);
		assertThat(bestBreaks.numClassses(), equalTo(3));
		
	}
	
	@org.junit.Test
	public void degenerate1() {
		Jenks breaker = new Jenks();
		breaker.addValues(1,1,1,1,1,1,1,1,1,1);

		
		Breaks bestBreaks = breaker.computeBreaks();
		System.out.println(bestBreaks);
		assertThat(bestBreaks.numClassses(), equalTo(1));
	}
	
	@org.junit.Test
	public void degenerate4() {
		Jenks breaker = new Jenks();
		breaker.addValues(1,1,1,1,1,10,10,30,30,40,40);

		
		Breaks bestBreaks = breaker.computeBreaks();
		System.out.println(bestBreaks);
		assertThat(bestBreaks.numClassses(), equalTo(3));
	}
	
	
	
	
	
}
