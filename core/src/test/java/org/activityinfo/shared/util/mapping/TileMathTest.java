package org.activityinfo.shared.util.mapping;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.activityinfo.shared.report.content.AiLatLng;
import org.activityinfo.shared.report.content.Point;
import org.activityinfo.shared.util.mapping.TileMath;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Test;
import org.junit.internal.matchers.TypeSafeMatcher;

public class TileMathTest {

	@Test
	public void inverse() {
		AiLatLng latlng = new AiLatLng(15, 30);
		Point px = TileMath.fromLatLngToPixel(latlng, 6);
		
		AiLatLng inverse = TileMath.inverse(px, 6);
		
		assertThat("longitude", inverse.getLng(), equalTo(latlng.getLng()));
		assertThat("latitude", inverse.getLat(), closeTo(latlng.getLat(), 0.0001));
	}

	private Matcher<Double> closeTo(final double x, final double epsilon) {
		return new TypeSafeMatcher<Double>() {

			@Override
			public void describeTo(Description d) {
				d.appendText("within ").appendValue(d)
					.appendText(" of ").appendValue(x);
			}

			@Override
			public boolean matchesSafely(Double item) {
				return Math.abs(item - x) < epsilon;
			}
			
		};
	}
	
}
