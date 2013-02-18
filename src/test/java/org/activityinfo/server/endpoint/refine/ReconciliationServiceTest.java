package org.activityinfo.server.endpoint.refine;

import org.apache.commons.codec.language.DoubleMetaphone;
import org.junit.Test;


public class ReconciliationServiceTest {

	@Test
	public void doubleMetaphone() {
		
		DoubleMetaphone encoder = new DoubleMetaphone();
		System.out.println(encoder.doubleMetaphone("Tin-E") + " " + encoder.doubleMetaphone("Youwarou", true));
		System.out.println(encoder.doubleMetaphone("Youvarou"));

	}
	
}
