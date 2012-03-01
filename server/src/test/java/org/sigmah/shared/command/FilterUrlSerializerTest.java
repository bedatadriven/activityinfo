package org.sigmah.shared.command;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.sigmah.shared.report.model.DimensionType;

public class FilterUrlSerializerTest {

	@Test
	public void activity() {
		Filter filter = new Filter();
		filter.addRestriction(DimensionType.Activity, 33);
		
		verifyCorrectSerde(filter);
	}
	
	private void verifyCorrectSerde(Filter filter) {
		String fragment = FilterUrlSerializer.toUrlFragment(filter);
		Filter deserialized = FilterUrlSerializer.fromUrlFragment(fragment);
		
		assertThat("deserialized", deserialized, equalTo(filter));
	
	}
}
