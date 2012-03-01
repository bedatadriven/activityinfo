package org.sigmah.client.page.entry.place;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.sigmah.client.page.entry.grouping.AdminGroupingModel;
import org.sigmah.shared.command.Filter;
import org.sigmah.shared.report.model.DimensionType;

public class DataEntryPlaceParserTest {

	@Test
	public void empty() {
		verifyCorrectSerde(new DataEntryPlace());
	}
	
	@Test
	public void activityFiltered() {
		Filter filter = new Filter();
		filter.addRestriction(DimensionType.Activity, 33);
		
		verifyCorrectSerde(new DataEntryPlace(filter));
	}
	
	@Test
	public void activityFilteredAndGrouped() {
		Filter filter = new Filter();
		filter.addRestriction(DimensionType.Activity, 33);
		AdminGroupingModel grouping = new AdminGroupingModel(1);
		
		verifyCorrectSerde(new DataEntryPlace(grouping, filter));
	}
	
	private void verifyCorrectSerde(DataEntryPlace place) {
		String fragment = place.serializeAsHistoryToken();
		System.out.println(place + " => " + fragment);

		DataEntryPlace deserialized = new DataEntryPlaceParser().parse(fragment);
		
		assertThat(deserialized, equalTo(place));
	}
}
