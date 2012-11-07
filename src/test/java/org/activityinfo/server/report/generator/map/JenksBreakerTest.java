package org.activityinfo.server.report.generator.map;

import org.activityinfo.server.report.generator.map.JenksBreaker.Breaks;


public class JenksBreakerTest {

	private static double VALUES[] = {
		1111, 	6751, 	502, 	1054, 	825, 	868, 	83, 	92, 	16, 	116, 	174, 	148, 	6447, 	
		597, 	1748, 	627, 	2338, 	868, 	441, 	1029, 	1294, 	5184, 	1512, 	232, 	565, 	1564, 	
		270, 	1127, 	2636, 	661, 	288, 	598, 	1389, 	527, 	289, 	825, 	612, 	336
	};
	
	@org.junit.Test
	public void test() {
		JenksBreaker breaker = new JenksBreaker();
		breaker.addValues(VALUES);
		Breaks jenksBreaks = breaker.getJenksBreaks(5);
		System.out.println(jenksBreaks);
	}
	
}
