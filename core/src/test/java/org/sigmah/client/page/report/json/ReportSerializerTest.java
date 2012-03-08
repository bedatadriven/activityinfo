package org.sigmah.client.page.report.json;

import org.junit.Before;
import org.junit.Test;
import com.google.gson.JsonParser;

import static org.junit.Assert.assertNull;

public class ReportSerializerTest {

	private ReportSerializer reportSerializer; 
	
	@Before
	public void setup(){
		reportSerializer = new ReportJsonFactory(new JsonParser()); 
	}
	
	@Test
	public void serializerTest(){
		
		
	}
	
	@Test
	public void deSerializerTest(){
		assertNull(reportSerializer.deserialize(""));
	}
}
