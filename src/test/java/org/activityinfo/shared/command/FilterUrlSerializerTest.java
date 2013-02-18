package org.activityinfo.shared.command;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.activityinfo.shared.command.Filter;
import org.activityinfo.shared.command.FilterUrlSerializer;
import org.activityinfo.shared.report.model.DimensionType;
import org.junit.Test;

import com.bedatadriven.rebar.time.calendar.LocalDate;
import com.google.common.collect.Sets;

public class FilterUrlSerializerTest {

	@Test
	public void activity() {
		Filter filter = new Filter();
		filter.addRestriction(DimensionType.Activity, 33);
		
		verifyCorrectSerde(filter);
	}
	
	@Test
	public void withDates() {
		Filter filter = new Filter();
		filter.getDateRange().setMinDate(new LocalDate(2011, 4, 19));
		filter.getDateRange().setMaxDate(new LocalDate(2012, 3, 31));
		filter.addRestriction(DimensionType.Activity, 1);
		filter.addRestriction(DimensionType.Database, Sets.newHashSet(31,42));
		
		verifyCorrectSerde(filter);
	}
	
	@Test
	public void queryParameter() {
		Filter filter = FilterUrlSerializer.fromQueryParameter("Partner 129-Activity 33");
		Filter expected = new Filter();
		expected.addRestriction(DimensionType.Partner, 129);
		expected.addRestriction(DimensionType.Activity, 33);
		
		assertThat(filter, equalTo(expected));
	}
	
	private void verifyCorrectSerde(Filter filter) {
		String fragment = FilterUrlSerializer.toUrlFragment(filter);
		Filter deserialized = FilterUrlSerializer.fromUrlFragment(fragment);
		
		assertThat("deserialized", deserialized, equalTo(filter));
	}
	
}
