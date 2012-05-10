package org.activityinfo.shared.command;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.activityinfo.shared.command.Filter;
import org.activityinfo.shared.command.FilterUrlSerializer;
import org.activityinfo.shared.report.model.DimensionType;
import org.junit.Test;

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
